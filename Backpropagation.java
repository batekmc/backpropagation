import java.util.Arrays;

public class Backpropagation {

	// Num of neurons in each layer. Highest index is last layer
	private int layers[];
	private Neuron neurons[];
	private int numOfNeurons;
	private int numOfInputs;
	private double learningC;
	// TODO - specify later
	private double neco;

	private double testSet[];
	private double testCount;
	private double trainSet[];
	private double trainCount;

	public Backpropagation() {

		runBackPropagation();
	}

	public void runBackPropagation() {

		// create network
		createNeurons();
		int outputsCount = layers[layers.length - 1];
		double input[] = new double[numOfInputs];
		double expectedOutput[] = new double[outputsCount];

		int isFinished = 0;

		int debug = 0;

		while (true) {

			for (int i = 0; i < trainCount; i++) {

				System.arraycopy(trainSet, i * (outputsCount + numOfInputs),
						input, 0, input.length);
				System.arraycopy(trainSet, i * (outputsCount + numOfInputs)
						+ numOfInputs, expectedOutput, 0,
						expectedOutput.length);
				excitation(input);

				if (isResponseGood(expectedOutput)) {
					isFinished++;
				} else {
					learning(expectedOutput, input);
					print("Iterace: " + debug++);
					printArr(input);

				}

			}// for
			if (isFinished == trainCount) {
				return;
			} else {
				isFinished = 0;
			}

		}// while
	}

	/**
	 * 
	 * @param expectedOut
	 *            - expected output from training input
	 * @return true if network response is the same as expected from training
	 *         data, else false
	 */
	private boolean isResponseGood(double expectedOut[]) {
		int index = numOfNeurons - layers[layers.length - 1];
		for (int i = 0; i < expectedOut.length; i++) {
			print(expectedOut[i] + " , " + neurons[index + i].getOutput() );
			if (expectedOut[i] != neurons[index + i].getOutput()) {
				return false;

			}
		}
		return true;

	}


	/**
	 * 
	 * @param input
	 *            - X parameter given by input layer
	 * 
	 *            ouptup of eacxh neuron is saved in Neuron.output
	 */
	private void excitation(double input[]) {

		int index = 0, index2 = 0;
		for (int j = 0; j < layers.length; j++) {
			for (int i = 0; i < layers[j]; i++) {
				if (i == 0 && j > 0) {
					// get output calculated by neurons of lower layer
					input = new double[layers[j - 1]];
					for (int k = 0; k < layers[j - 1]; k++)
						input[k] = neurons[index2 + k].getOutput();
					index2 += layers[j - 1];
				}
				if (j == 0)
					neurons[index + i].midLayerExcitation(input);
				else {
					if (neurons[index + i].isOutputLayer())
						neurons[index + i].outputExcitation(input);
					else
						neurons[index + i].midLayerExcitation(input);
				}
			}
			index += layers[j];
		}// for
	}

	/**
	 * 
	 * @param expectedOutput
	 *            - expected answer to training data
	 * @param input
	 *            - input layer data (x1 ... xN)
	 */
	private void learning(double expectedOutput[], double input[]) {

		int index = this.numOfNeurons - layers[layers.length - 1], index2=0;
		double delta;
		double tmp;
		double tmpW[];
		double tmpSum;

		for (int j = layers.length - 1; j >= 0; j--) {
			for (int i = 0; i < layers[j]; i++) {

				// output layer
				if (neurons[index + i].isOutputLayer()) {

					tmp = neurons[index + i].getOutput();
					delta = neco * tmp * (1 - tmp) * (expectedOutput[i] - tmp);
					neurons[index + i].setDelta(delta);

					tmpW = new double[layers[j-1] ];
					for (int a = 0; a <layers[j -1]; a++)
						tmpW[a] = neurons[index - layers[j -1] + i ].getOutput();
					neurons[index + i].setdW(tmpW, learningC);
				}
				// mid layer
				else {

					tmp = neurons[index + i].getOutput();
					tmpSum = 0;
					index2 = index + layers[j + 1];

					// get sum of delta*w from neurons of higher layer
					for (int a = 0; a < layers[j + 1]; a++)
						tmpSum += neurons[index2 + a].getDelta()
								* neurons[index2 + a].getWeights()[i];
					delta = neco * tmp * (1 - tmp) * (tmpSum);
					neurons[index + i].setDelta(delta);

					// is is not the last layer?
					if (j > 0) {
						tmpW = new double[neurons[index + i].getWeights().length - 1];
						tmpW = new double[layers[j-1] ];
						for (int a = 0; a <layers[j -1]; a++)
							tmpW[a] = neurons[index - layers[j -1] + i ].getOutput();
						neurons[index + i].setdW(tmpW, learningC);
					}

					// if it is last layer, then calculate dW with input(x1 ...
					// xN)
					else {
						neurons[index + i].setdW(input, learningC);
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

	private void initFromFile() {
		FileParser f = new FileParser("/home/batek/Stažené/lekar.txt", null);

		// init layers where 1st layer in input, last output
		int a = f.getPocetVstupu();
		numOfInputs = a;
		int b[] = f.getPocetVeVrstvach();
		layers = new int[b.length];
		System.arraycopy(b, 0, layers, 0, b.length);

		this.learningC = f.getKoefUceni();

		testSet = f.getTestovaci();
		testCount = f.getPocetTestovacich();

		trainSet = f.getTrenovaci();
		trainCount = f.getPocetPrvkuTrenovaci();

		//neco = f.getKoefZpredchoziho();
		neco = 1.0;

	}

	private void createNeurons() {
		this.initFromFile();
		this.numOfNeurons = setNumOfNeurons();
		neurons = new Neuron[numOfNeurons];
		int index = 0;
		for (int j = 0; j < layers.length; j++) {
			for (int i = 0; i < layers[j]; i++) {
				if (j == (layers.length - 1)) {
					neurons[index + i] = new Neuron(layers[j - 1], true);
				} else {
					if (j == 0)
						neurons[index + i] = new Neuron(numOfInputs, false);
					else
						neurons[index + i] = new Neuron(layers[j - 1], false);
				}

			}// for i
			index += layers[j];
		}// for j
	}

}
