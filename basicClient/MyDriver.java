package basicClient;

import java.util.HashMap;

import batek.Backpropagation;

;

public class MyDriver implements DriverInterface {

	private static final int PARAMS = 28;

	Backpropagation bp;
	double res[];
	double input[];
	private boolean setInputSize;
	HashMap<String, Float> mMap;

	/**
	 * 
	 * @param b
	 *            - reference to learned neural network
	 */
	public MyDriver(Backpropagation b) {
		this.bp = b;
		res = new double[2];
		setInputSize = true;
		mMap = new HashMap<String, Float>();
		input = new double[PARAMS];
	}

	@Override
	public HashMap<String, Float> drive(HashMap<String, Float> values) {
		if (bp == null)
			return null;

		input[0] = values.getOrDefault("angle", 0.0f);
		input[1] = values.getOrDefault("speed", 0.0f);
		input[2] = values.getOrDefault("distance0", 0.0f);
		input[3] = values.getOrDefault("distance4", 0.0f);
		input[4] = values.getOrDefault("distance8", 0.0f);
		input[5] = values.getOrDefault("distance16", 0.0f);
		input[6] = values.getOrDefault("distance32", 0.0f);
		input[7] = values.getOrDefault("friction", 0.0f);
		input[8] = values.getOrDefault("skid", 0.0f);
		input[9] = values.getOrDefault("checkpoint", 0.0f);
		input[10] = values.getOrDefault("sensorFrontLeft", 0.0f);
		input[11] = values.getOrDefault("sensorFrontMiddleLeft", 0.0f);
		input[12] = values.getOrDefault("sensorFrontMiddleRight", 0.0f);
		input[13] = values.getOrDefault("sensorFrontRight", 0.0f);
		input[14] = values.getOrDefault("sensorFrontRightCorner1", 0.0f);
		input[15] = values.getOrDefault("sensorFrontRightCorner2", 0.0f);
		input[16] = values.getOrDefault("sensorRight1", 0.0f);
		input[17] = values.getOrDefault("sensorRight2", 0.0f);
		input[18] = values.getOrDefault("sensorRearRightCorner2", 0.0f);
		input[19] = values.getOrDefault("sensorRearRightCorner1", 0.0f);
		input[20] = values.getOrDefault("sensorRearRight", 0.0f);
		input[21] = values.getOrDefault("sensorRearLeft", 0.0f);
		input[22] = values.getOrDefault("sensorRearLeftCorner1", 0.0f);
		input[23] = values.getOrDefault("sensorRearLeftCorner2", 0.0f);
		input[24] = values.getOrDefault("sensorLeft1", 0.0f);
		input[25] = values.getOrDefault("sensorLeft2", 0.0f);
		input[26] = values.getOrDefault("sensorFrontLeftCorner1", 0.0f);
		input[27] = values.getOrDefault("sensorFrontLeftCorner2", 0.0f);

		mMap.clear();
		res = this.bp.carServerOutput(input);
		mMap.put("wheel", (float) res[0]);
		mMap.put("acc", (float) res[1]);
		System.out.println(mMap.toString());
		return mMap;
	}

}
