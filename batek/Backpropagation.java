package batek;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.List;
import java.util.Arrays;

public class Backpropagation implements Runnable {

	private final int MAX_ITERATIONS = 100000000;

	// Num of neurons in each layer. Highest index is hte last layer
	private int layers[];
	private Neuron neurons[];
	private int numOfNeurons;
	private int numOfInputs;
	private double learningC;
	// TODO - specify later
	private double prevStepC;
	// network Error
	private double bpError;
	private double maxError;

	// TODO
	private double testSet[];
	private int testCount;
	private double trainSet[];
	private int trainCount;

	// for GUI
	private static volatile boolean stop;
	private final List<String> queue;
	private boolean isRunning;
	private FileParser flp;
	private boolean usrErr = false;

	/**
	 * 
	 * @param file
	 *            - file in given file format
	 * @param q
	 *            - synchronizedList - must be thread safe!
	 */
	public Backpropagation(File file, List<String> q) {
		flp = new FileParser(null, file);
		queue = q;
		isRunning = false;
		initFromNetworkData(flp);
	}

	/**
	 * For loading network from file
	 */
	public Backpropagation() {
		queue = null;
		isRunning = true;
		flp = null;
	}

	/**
	 * 
	 * @return true if network is learning
	 */
	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * Run backpropagation algorithm with given settings
	 */
	@Override
	public void run() {
		runBackPropagation();
	}

	public boolean testInput(double[] input) {
		if (input.length == numOfInputs)
			return true;
		return false;
	}

	public boolean setMaxError(double val) {
		if (val > 0.0d && val < setMinErrorToStopLearning(1.0d)) {
			bpError = val;
			usrErr = true;
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @param val
	 *            learning rate
	 * @return true if set
	 */
	public boolean setLearningC(double val) {
		if (val > 0.0d) {
			this.learningC = val;
			return true;
		}
		return false;

	}

	/**
	 * 
	 * @param val
	 *            previous step influence
	 * @return true if set
	 */
	public boolean setPrevStepC(double val) {
		if (val >= 0.0d && val < 1.0d) {
			this.prevStepC = val;
			return true;
		}
		return false;

	}

	/**
	 * stop learning
	 */
	public void stopLearning() {
		this.stop = true;
	}

	public boolean saveNetwork(String fs) {

		try {
			if (neurons == null || layers == null)
				return false;
			PrintWriter writer = new PrintWriter(fs, "UTF-8");
			String tmp = "";
			for (int i = 0; i < layers.length; i++)
				tmp += layers[i] + " ";
			writer.println(tmp);
			tmp = "";
			for (int i = 0; i < neurons.length; i++) {
				for (int j = 0; j < neurons[i].getWeights().length; j++)
					tmp += neurons[i].getWeights()[j] + " ";
				writer.println(tmp);
				tmp = "";
			}
			writer.close();
			return true;

		} catch (FileNotFoundException e) {
			// TODO - should not happened... ever...
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			// TODO - don't use stupid OS | editor...
			e.printStackTrace();
			return false;
		}
	}

	public boolean loadNetwork(File net) {

		try {
			BufferedReader bfrd = new BufferedReader(new FileReader(net));
			String tmp;
			String[] spl;
			int count = 0;
			double nWeights[];
			while ((tmp = bfrd.readLine()) != null) {
				if (tmp.isEmpty())
					continue;
				if (count == 0) {
					spl = tmp.split("\\s+");
					this.layers = new int[spl.length];
					for (int i = 0; i < spl.length; i++) {
						layers[i] = Integer.parseInt(spl[i]);
					}
					setNumOfNeurons();
					count++;
				} else {
					if (count == 1) {
						// create network with random weights
						createNeurons();
					}
					spl = tmp.split("\\s+");
					nWeights = new double[spl.length];
					for (int i = 0; i < spl.length; i++) {
						nWeights[i] = Double.parseDouble(spl[i]);
					}
					if (count - 1 < neurons.length)
						neurons[count - 1].setWeights(nWeights);
					else
						return false;
					count++;
				}

			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public String[] testFileData() {
		String tmp[] = new String[testCount * 2];
		double testInput[] = new double[numOfInputs];
		int index = 0, index2 = numOfNeurons - layers[layers.length - 1];
		double testInput2[] = new double[layers[layers.length - 1]];
		for (int i = 0; i < testCount; i++) {
			for (int j = 0; j < numOfInputs; j++)
				testInput[j] = testSet[index + j];
			excitation(testInput);
			for (int a = 0; a < numOfNeurons - index2; a++)
				testInput2[a] = neurons[index2 + a].getOutput();
			tmp[i * 2] = "--Input: " + Arrays.toString(testInput) + "\n";
			tmp[i * 2 + 1] = "++Output: " + Arrays.toString(testInput2) + "\n";
			index += numOfInputs;
		}
		return tmp;
	}

	/**
	 * Use network.
	 * 
	 * @param input
	 *            input to network
	 * @return output from network to the input
	 */
	public double[] testData(double input[]) {
		flp.inputsTo1(input);
		double d[] = new double[layers[layers.length - 1]];
		int index = numOfNeurons - layers[layers.length - 1];
		excitation(input);
		for (int i = 0; i < d.length; i++)
			d[i] = neurons[index + i].getOutput();
		return d;

	}

	/**
	 * Use network.
	 * 
	 * @param input
	 *            input to network
	 * @return output from network to the input
	 */
	public double[] carServerOutput(double input[]) {
		double d[] = new double[layers[layers.length - 1]];
		int index = numOfNeurons - layers[layers.length - 1];
		excitation(input);
		for (int i = 0; i < d.length; i++)
			d[i] = neurons[index + i].getOutput();
		return d;

	}

	/**
	 * 
	 * @param val
	 *            array of network topology
	 * @return true, if input is used, otherwise flase
	 */
	public boolean setLayers(int val[]) {

		if (layers != null && val.length > 1) {
			if (val[val.length - 1] == layers[layers.length - 1]) {
				this.layers = val;
				return true;
			}
		}

		return false;

	}

	public void runBackPropagation() {

		// create network
		createNeurons();
		stop = false;
		isRunning = true;
		if (!usrErr)
			maxError = setMinErrorToStopLearning(0.1);
		int outputsCount = layers[layers.length - 1];
		double input[] = new double[numOfInputs];
		double expectedOutput[] = new double[outputsCount];

		long debug = 0;
		bpError = 0;

		while (true) {
			// if(debug == 10)break;
			for (int i = 0; i < trainCount; i++) {

				System.arraycopy(trainSet, i * (outputsCount + numOfInputs),
						input, 0, input.length);
				System.arraycopy(trainSet, i * (outputsCount + numOfInputs)
						+ numOfInputs, expectedOutput, 0, expectedOutput.length);
				excitation(input);
				learning(expectedOutput, input);

				setBPError(expectedOutput);
				// System.out.println("#I:" + i);
				// System.out.println("#input" + Arrays.toString(input));
				// System.out.println("#output" +
				// Arrays.toString(expectedOutput));

			}// for
			bpError *= 0.5d;
			if (bpError <= maxError || stop) {
				if (queue != null) {
					queue.add("##Finished! Error: " + bpError
							+ " , iteration : " + debug + "\n");
					// Tell gui thread that it stopped
					queue.add("EOF");
				}
				isRunning = false;
				return;
			}
			// GUI
			if (debug % 100 == 0)
				if (queue != null)
					queue.add("@Error: " + bpError + " , iteration : " + debug
							+ "\n");
			bpError = 0;
			debug++;

		}// loop
	}

	/**
	 * calculate error of network given by expected output of training data and
	 * real response form neural network
	 * 
	 * @param expectedOut
	 *            - expected output from training input
	 * @return true if network response is the same as expected from training
	 *         data, else false
	 */
	private void setBPError(double expectedOut[]) {
		int index = numOfNeurons - layers[layers.length - 1];
		for (int i = 0; i < expectedOut.length; i++) {
			bpError += (neurons[index + i].getOutput() - expectedOut[i])
					* (neurons[index + i].getOutput() - expectedOut[i]);
		}
	}

	/**
	 * set error size, if error of network is smaller than this siz, learning
	 * will be stopped
	 * 
	 * @param diff
	 *            - error in percents/100 - fxp 0.1 == 10 %
	 * @return
	 */
	private double setMinErrorToStopLearning(double diff) {
		return 0.5d * (trainCount * layers[layers.length - 1] * diff * diff);
	}

	/**
	 * 
	 * @param input
	 *            - X parameter given by input layer
	 * 
	 *            output of each neuron is saved in Neuron.output
	 */
	private void excitation(double input[]) {
		double input2[] = new double[input.length];
		System.arraycopy(input, 0, input2, 0, input2.length);

		int index = 0, index2 = 0;
		for (int j = 0; j < layers.length; j++) {
			for (int i = 0; i < layers[j]; i++) {
				if (i == 0 && j > 0) {
					// get output calculated by neurons of lower layer
					input2 = new double[layers[j - 1]];
					for (int k = 0; k < layers[j - 1]; k++)
						input2[k] = neurons[index2 + k].getOutput();
					index2 += layers[j - 1];
				}
				neurons[index + i].midLayerExcitation(input2);
			}
			index += layers[j];
		}// for
	}

	/**
	 * 
	 * @param expectedOutput
	 *            - expected answer to training data
	 * @param input
	 *            - input layer data (x1 ... xN) from training data
	 */
	private void learning(double expectedOutput[], double input[]) {

		int index = this.numOfNeurons - layers[layers.length - 1], index2 = 0;
		double delta;
		double tmp;
		double tmpW[];
		double tmpSum;

		for (int j = layers.length - 1; j >= 0; j--) {
			for (int i = 0; i < layers[j]; i++) {

				// output layer
				if (neurons[index + i].isOutputLayer()) {

					tmp = neurons[index + i].getOutput();
					delta = tmp * (1 - tmp) * (expectedOutput[i] - tmp);
					neurons[index + i].setDelta(delta);

					tmpW = new double[layers[j - 1]];
					// deltas of lower layer
					for (int a = 0; a < layers[j - 1]; a++)
						tmpW[a] = neurons[index - layers[j - 1] + i]
								.getOutput();
					neurons[index + i].setdW(tmpW);
				}
				// mid layer
				else {

					tmp = neurons[index + i].getOutput();
					tmpSum = 0;
					index2 = index + layers[j];

					// get sum of delta*w from neurons of higher layer
					for (int a = 0; a < layers[j + 1]; a++)
						tmpSum += (neurons[index2 + a].getDelta()
								* neurons[index2 + a].getWeights()[i]);
					delta = tmp * (1 - tmp) * (tmpSum);
					neurons[index + i].setDelta(delta);

					// is is not the last layer?
					if (j > 0) {
						tmpW = new double[layers[j - 1]];
						for (int a = 0; a < layers[j - 1]; a++)
							tmpW[a] = neurons[index - layers[j - 1] + i]
									.getOutput();
						neurons[index + i].setdW(tmpW);
					}
					// in the last layer calculate dW with input (x1 ... xN)
					else {
						neurons[index + i].setdW(input);
					}

				}

			}// for
			if (j > 0)
				index -= layers[j - 1];

		}// for

		updateWeights();
	}

	private void updateWeights() {

		for (int i = 0; i < neurons.length; i++)
			neurons[i].updateWeights();

	}

	private int setNumOfNeurons() {
		int sum = 0;
		for (int i = 0; i < layers.length; i++)
			sum += layers[i];
		return sum;
	}

	private void printArr(double ar[]) {
		System.out.println(Arrays.toString(ar));
	}

	private void print(String ar) {
		System.out.println(ar);
	}

	private void initFromNetworkData(NetworkData f) {

		numOfInputs = f.getNumOfInputs();

		layers = f.getLayers();

		learningC = f.getLearningC();

		testSet = f.getTestSet();
		testCount = f.getTestCount();

		trainSet = f.getTrainSet();
		trainCount = f.getTrainCount();

		prevStepC = f.getFromLastC();

	}

	private void createNeurons() {
		this.numOfNeurons = setNumOfNeurons();
		neurons = new Neuron[numOfNeurons];
		int index = 0;
		for (int j = 0; j < layers.length; j++) {
			for (int i = 0; i < layers[j]; i++) {
				if (j == (layers.length - 1)) {
					neurons[index + i] = new Neuron(layers[j - 1], true,
							learningC, prevStepC);
				} else {
					if (j == 0)
						neurons[index + i] = new Neuron(numOfInputs, false,
								learningC, prevStepC);
					else
						neurons[index + i] = new Neuron(layers[j - 1], false,
								learningC, prevStepC);
				}

			}// for i
			index += layers[j];
		}// for j
	}

}
