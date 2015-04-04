import java.util.Random;

public class Neuron {

	private double weights[];
	// private double vstupy[];
	private boolean outputLayer;
	private static double lambda = 1.0d;

	private double output;
	private double delta;
	private double dW[];
	

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
	 * Create neuron with randomly initialized weights
	 * 
	 * @param numOfWeights - how many connection neuron have from lower layer
	 * @param output - true, if neuron is in output layer, else set to false
	 */
	public Neuron(int numOfWeights, boolean output) {
		this.outputLayer = output;

		//need +1 array because of bias
		weights = new double[numOfWeights + 1];
		dW = new double[numOfWeights + 1];
		initWeights();
	}
	
	/**
	 * 
	 * @param W - outputs of lower layer neurons
	 * @param learningK - learning coefficient. Usually in interval (0,1>
	 */
	public void setdW(double W[], double learningK) {
		for(int i = 0 ; i < W.length ; i ++)
			this.dW[i] = learningK * W[i] * delta;
		this.dW[dW.length - 1] = learningK * delta;
	}

	/**
	 * init weight randomly in interval <-1,1>
	 */
	private void initWeights() {
		Random r = new Random();
		for (int i = 0; i < weights.length; i++) {
			boolean a = r.nextBoolean();
			if (a)
				weights[i] = r.nextDouble();
			else
				weights[i] = -r.nextDouble();
		}
	}

	/**
	 * Excitation of output layer neurons
	 * 
	 * @param input
	 *            - neuron inputs, X set output to 1 | 0
	 */
	public void outputExcitation(double input[]) {
		double sum = 0;
		for (int i = 0; i < weights.length - 1; i++)
			sum += weights[i] * input[i];
		//bias
		sum += weights[weights.length - 1];
		if (sum <= 0)
			output = 0.0d;
		output = 1.0d;

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
		//bias
		sum += weights[weights.length - 1];
		output = 1 / (1 + Math.exp(lambda * sum));

	}
	
	/**
	 * Update weights to calculated by backpropagation algorithm
	 *  w + dW
	 */
	public void updateWeights(){
		for( int i = 0 ; i < weights.length ; i ++)
			weights[i] += dW[i];
	}

}// class
