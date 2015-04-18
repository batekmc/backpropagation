package basicClient;

import java.util.HashMap;

import batek.Backpropagation;;

public class MyDriver implements DriverInterface{
	
	Backpropagation bp;
	double res[];
	double input[];
	Double tmpInput[];
	private boolean setInputSize;
	HashMap<String, Float> mMap;
		
	/**
	 * 
	 * @param b - reference to learned neural network
	 */
	public  MyDriver( Backpropagation b) {
		this.bp = b;
		res = new double[2];
		setInputSize = true;
		mMap = new HashMap<String, Float>();
	}

	@Override
	public HashMap<String, Float> drive(HashMap<String, Float> values){
		if( bp == null)
			return null;
		if(setInputSize)
		{
			input = new double[values.size()];
			setInputSize = false;
		}
		mMap.clear();
		tmpInput = values.values().toArray(new Double[values.size()]);
		for(int i = 0 ; i < tmpInput.length ; i ++)
			input[i] = tmpInput[i].doubleValue();
		res = this.bp.testData(input);
		mMap.put("wheel", (float) res[0]);
		mMap.put("acc", (float) res[1]);
		
		return mMap;
		
	}

}
