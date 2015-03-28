import java.util.Random;

public class Neuron {

	private double weights[];
	// private double vstupy[];
	private boolean outputLayer;
	private static double lambda = 1.0d;

	private double output;

	
	
	
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
	 * Create neuron
	 * 
	 * @param numOfWeights - how many connection neuron have from lower layer
	 * @param output - true, if neuron is in output layer, else set to false
	 */
	public Neuron(int numOfWeights, boolean output) {
		this.outputLayer = output;

		weights = new double[numOfWeights];
		initWeights();
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
		for (int i = 0; i < weights.length; i++)
			sum += weights[i] * input[i];
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
		for (int i = 0; i < weights.length; i++)
			sum += weights[i] * vstupy[i];
		output = 1 / (1 + Math.exp(-lambda * sum));

	}

}// class
