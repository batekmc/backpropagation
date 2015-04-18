package basicClient;

import java.util.List;
import java.util.Random;

import batek.Backpropagation;

/**
 * Jednoduchy ukazkovy klient. Pripoji se k zavodnimu serveru a ridi auto.
 * 
 */
public class BasicClient {

	private String url;
	private int port;
	private String driverName;
	private String carSelected;
	private String race;

	private Backpropagation bp;

	/**
	 * 
	 * @param url - host address of server | url
	 * @param port = server port
	 * @param race - race name, case sensitive
	 * @param driver - driver name, optional
	 * @param car - optional, random if null
	 * @param b - backpropagation object which is already learned
	 */
	public BasicClient(String url, int port, String race, String driver,
			String car, Backpropagation b) {
		this.url = url;
		this.port = port;
		this.driverName = driver;
		this.carSelected = car;
		this.race = race;
		this.bp = b;

	}

	/**
	 * 
	 * @return text messages for user if is connected to server or what might be
	 *         wrong
	 */
	public String connect() {

		try {
			if (url == null)
				return "HOST_FAIL";
			RaceConnector raceConnector = new RaceConnector(url, port, null);
			List<String> raceList = raceConnector.listRaces();
			if (!raceList.contains(race))
				return "RACE_NOT_FOUND";
			List<String> carList = raceConnector.listCars(race);
			if (!carList.contains(carSelected))
				carSelected = carList.get(new Random().nextInt(carList.size()));
			if (driverName == null)
				driverName = "Sumachr_" + System.nanoTime();

			raceConnector.setDriver(new MyDriver(bp));
			raceConnector.start(race, driverName, carSelected);
			return "OK";
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}
	}
}
