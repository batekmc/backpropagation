import java.util.Arrays;


public class Backpropagation {
	
	private int layers[];
	private Neuron neurons[];
	private int numOfNeurons;
	private int numOfInputs;
	private double learningK;
	private double neco;
	
	private double testInput[];
		
	public Backpropagation( ){
		//TESTING ONLY
		createNeurons();
		System.out.println(Arrays.toString(layers));
		System.out.println("//////////////////////////////////////////");
		excitation(testInput);
		System.out.println(Arrays.toString(testInput));
		for( int i = 0 ; i < neurons.length ; i++ )
			System.out.println("i: " + neurons[i].getOutput() + "\n");		
	}
	
	private void initFromFile(){
		FileParser f = new FileParser("/home/batek/Stažené/lekar.txt", null);
		
		//init layers where 1st layer in input, last output
		int a = f.getPocetVstupu();
		numOfInputs = a;
		int b[] = f.getPocetVeVrstvach();
		layers = new int[b.length];
		System.arraycopy(b, 0, layers, 0, b.length);
		
		this.learningK = f.getKoefUceni();
		//TODO - parse other arguments
		
		testInput = f.getTestovaci();
		
		neco = 1.0d;
		
	}
	
	private void createNeurons(){
		this.initFromFile();
		this.numOfNeurons = setNumOfNeurons();
		neurons = new Neuron[numOfNeurons];
		int index = 0;
		for( int j = 0 ; j < layers.length ; j++){
			for( int i = 0; i < layers[j]; i++){
				if(j == (layers.length - 1)){
					neurons[index + i] = new Neuron(layers[j-1],  true);
				}
				else{
					if(j == 0)
						neurons[index + i] = new Neuron(numOfInputs,  false);
					else
						neurons[index + i] = new Neuron(layers[j-1],  false);					
				}
					
			}//for i
			index += layers[j];
		}//for j		
	}
	

	/**
	 * 
	 * @param input - X parameter given by input layer
	 * 
	 * ouptup of eacxh neuron is saved in Neuron.output
	 */
	private void excitation( double input[]){
		
		int index = 0, index2 = 0;
		for( int j = 0 ; j < layers.length ; j++){
			for( int i = 0 ; i < layers[j] ; i++){
				if(i == 0 && j > 0)
				{
					//get output calculated by neurons of lower layer
					input = new double[ layers[j-1] ];
					for( int k = 0; k < layers[j-1] ; k++)
						input[k] = neurons[index2 + k].getOutput();
					index2+= layers[j-1];
				}
				if( j == 0)
					neurons[index + i].midLayerExcitation(input);
				else
				{
					if(neurons[index+i].isOutputLayer())
						neurons[index+i].outputExcitation(input);
					else
						neurons[index + i].midLayerExcitation(input);
				}
			}
			index += layers[j];
		}//for
	}
	
	
	//TODO - backpropagation:)
	private void learning(double  expectedOutput[], double input[]){
		
		int index = this.numOfNeurons - layers[layers.length - 1], index2;
		double delta;
		double tmp;
		double tmpW[];
		double tmpSum;
		
		for( int j = layers.length-1 ; j >= 0 ; j ++){
			for( int i = 0 ; i < layers[j] ; i++ ){
				
				//output layer
				if( neurons[index + i].isOutputLayer()){
					
					tmp = neurons[index + i].getOutput();
					delta =  neco * tmp * ( 1 - tmp)*( expectedOutput[i] - tmp);
					neurons[index + i].setDelta(delta);
					
					tmpW = new double[neurons[ index + i].getWeights().length - 1];
					for( int a = neurons[ index + i].getWeights().length - 2 ; a >=0 ; a--)
						tmpW[a] = neurons[index - a - 1].getOutput();
					neurons[index + i].setdW(tmpW, learningK);
				}				
				//mid layer
				else {
					
					tmp = neurons[index + i].getOutput();
					tmpSum = 0;
					index2 = index + layers[j+1];
					
					//get sum of delta*w from neurons of higher layer
					for( int a = 0 ; a < layers[j+1] ; a++)
						tmpSum += neurons[index2 + a].getDelta() * neurons[index2 + a].getWeights()[i];
					delta = neco * tmp * (1 - tmp)*(tmpSum);
					
					//is is not the last layer?
					if( j> 0){
						tmpW = new double[neurons[ index + i].getWeights().length - 1];
						for( int a = neurons[ index + i].getWeights().length - 2 ; a >=0 ; a--)
							tmpW[a] = neurons[index - a - 1].getOutput();
						neurons[index + i].setdW(tmpW, learningK);
					}
					
					//if it is last layer, then calculate dW with input(x1 ... xN)
					else{
						neurons[index + i].setdW(input, learningK);
					}
					
				}
				
			}//for
			if(j > 0)
				index -= layers[j -1];
		}//for	
		
		updateWeights();
	}
	
	private void updateWeights(){
		
		for(int i = 0 ; i < neurons.length ; i ++)
			neurons[i].updateWeights();
		
	}
	
	private int setNumOfNeurons(){
		int sum = 0;
		for( int i = 0 ; i < layers.length; i++)
			sum += layers[i];
		return sum;
	}
	
}
