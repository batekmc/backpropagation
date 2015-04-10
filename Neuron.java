import java.util.Random;

public class Neuron {

	private double weights[];
	// private double vstupy[];
	private boolean outputLayer;
	private final double lambda = -1.0d;

	private double output;
	private double delta;
	private double dW[];
	private double oldDW[];

	private double LEARNING_C;
	private double PREVIOUS_STEP_C;
	private boolean isFirstWeight = true;

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	public double[] getWeights() {
		return weights;
	}

	public boolean isOutputLayer() {
		return outputLayer;
	}

	public double getOutput() {
		return output;
	}

	/**
	 * 
	 * @param numOfWeights
	 *            - how many connection neuron have from lower layer
	 * @param output
	 *            - true, if neuron is in output layer, else set to false
	 * @param LEARNING_C - leakning coefficient
	 * @param PREVIOUS_STREP_C - previous step influence coefficient
	 */
	Neuron(int numOfWeights, boolean output, double LEARNING_C,
			double PREVIOUS_STREP_C) {
		this.outputLayer = output;
		this.LEARNING_C = LEARNING_C;
		this.PREVIOUS_STEP_C = PREVIOUS_STREP_C;

		// need +1 array because of bias
		weights = new double[numOfWeights + 1];
		dW = new double[numOfWeights + 1];
		oldDW = new double[numOfWeights + 1];
		initWeights();
	}

	/**
	 * 
	 * @param W
	 *            - outputs of lower layer neurons
	 */
	public void setdW(double W[]) {
		for (int i = 0; i < W.length; i++)
			this.dW[i] = LEARNING_C * W[i] * delta;
		this.dW[dW.length - 1] = LEARNING_C * delta;
	}

	/**
	 * init weight randomly in interval <-1,1>
	 */
	private void initWeights() {
		Random r = new Random();
		for (int i = 0; i < weights.length; i++) {
			weights[i] = r.nextDouble();
		}
	}

	/**
	 * Excitation of mid layer neuron
	 * 
	 * @param vstupy
	 *            - neuron inputs, X set output to excitation in interval <0, 1>
	 */
	public void midLayerExcitation(double vstupy[]) {
		double sum = 0;
		for (int i = 0; i < weights.length - 1; i++)
			sum += weights[i] * vstupy[i];
		// bias
		sum += weights[weights.length - 1];
		output = ( 1 / (1 + Math.exp(-sum)) );

	}

	/**
	 * Update weights to calculated by backpropagation algorithm w + dW +
	 * oldDW*previous_step_influence.
	 * OldW * PRV..is inertia.
	 */
	public void updateWeights() {
		if (isFirstWeight) {
			isFirstWeight = false;
			for (int i = 0; i < weights.length; i++)
				weights[i] += dW[i];
		} else {
			for (int i = 0; i < weights.length; i++)
				weights[i] += dW[i] + PREVIOUS_STEP_C * oldDW[i];
		}
		if(PREVIOUS_STEP_C != 0.0d)
			System.arraycopy(dW, 0, oldDW, 0, dW.length);
		//oldDW = dW;
	}

}// class
