import java.util.Arrays;


public class Backpropagation {
	
	private int layers[];
	private Neuron neurons[];
	private int numOfNeurons;
	private int numOfInputs;
	
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
		//TODO - parse other arguments
		
		testInput = f.getTestovaci();
		
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
	 *TODO - inicializace prvni vrstvy, samotna excitace
	 *
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
		}
	}
	
	private int setNumOfNeurons(){
		int sum = 0;
		for( int i = 0 ; i < layers.length; i++)
			sum += layers[i];
		return sum;
	}
	
}
