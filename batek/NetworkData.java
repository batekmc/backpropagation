package batek;
public abstract class NetworkData {

	// Num of neurons in each layer. Highest index is last layer
	protected int layers[];
	//number of inputs of network
	protected int numOfInputs;
	//learning coefficient (0-1)
	protected double learningC;
	//Coefficient of influence from previous step
	protected double fromLastC;
	// TODO - specify later
	protected double neco;
	//test set in format of [inputs(1) , expected output, ... , inputs(testCount)] 
	protected double testSet[];
	//count of input sets
	protected int testCount;
	//train set in format of [inputs(1) , expected output, ... , inputs(trainCount)]
	protected double trainSet[];
	//count of train sets
	protected int trainCount;
	
	public double getFromLastC() {
		return fromLastC;
	}

	public int[] getLayers() {
		return layers;
	}
	
	public int getNumOfInputs() {
		return numOfInputs;
	}
	
	public double getLearningC() {
		return learningC;
	}
	
	public double[] getTestSet() {
		return testSet;
	}
	
	public int getTestCount() {
		return testCount;
	}
	
	public double[] getTrainSet() {
		return trainSet;
	}
	
	public int getTrainCount() {
		return trainCount;
	}

}
