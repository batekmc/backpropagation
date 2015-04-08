import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public class FileParser extends NetworkData {
	
	private double popisVstupu[];
	private String popisVystupu[];


	private File f;
	
	/**
	 * Naparsuje vstupni soubor podle daneho formatu.
	 * Jeden z parametru musi byt null, jinak se pouzije ten druhy tj, File objekt
	 * 
	 * @param path - cesta k souboru
	 * @param ff - File type object of file, you want to open
	 * @throws NullPointerException - if not given valid input
	 */

	public FileParser(String path, File ff) throws NullPointerException{

		if (path != null) {
			f = new File(path);
			readFile(f);
		}
		else {
			if(ff != null){
				f = ff;
				readFile(f);
			}
			else
				throw new NullPointerException();
		}

	}

	/**
	 * Fce nacte informace ze souboru a predeved hodnoty do definovaneho formatu
	 * 
	 * @param file
	 *            - soubor ve sprvnem formatu:)
	 */
	private void readFile(File file) {
		int vrstev = 0;
		
		try {
			BufferedReader bfrd = new BufferedReader(new FileReader(file));
			String tmp;
			String[] spl;
			int count1 = 0, count2 = 0;
			while ((tmp = bfrd.readLine()) != null) {

				if (tmp.isEmpty() || tmp.charAt(0) == '#') {
					continue;
				}

				// pocet vrstev
				if (count1 == 0) {
					vrstev = Integer.parseInt(tmp);
					this.layers = new int[vrstev];
					count1++;
					continue;
				}

				// pocet vstupu
				if (count1 == 1) {
					numOfInputs = Integer.parseInt(tmp);
					popisVstupu = new double[numOfInputs * 2];
					count1++;
					continue;
				}

				// Popis vstupu[]
				if (count1 == 2) {
					spl = tmp.split("\\s+");
					popisVstupu[count2 * 2] = Double.parseDouble(spl[1]);
					popisVstupu[count2 * 2 + 1] = Double.parseDouble(spl[2]);
					count2++;
					if (count2 == numOfInputs) {
						count2 = 0;
						count1++;
					}
					continue;
				}

				// pocet neuronu ve vrstvach
				if (count1 == 3) {
					spl = tmp.split("\\s+");
					for (int i = 0; i < vrstev; i++)
						layers[i] = Integer.parseInt(spl[i]);
					count1++;
					popisVystupu = new String[layers[layers.length - 1]];
					continue;

				}

				// popis vystupu
				if (count1 == 4) {
					popisVystupu[count2] = tmp;
					count2++;
					if (count2 == layers[layers.length - 1]) {
						count1++;
						count2 = 0;

					}
					continue;

				}

				// koeficient uceni
				if (count1 == 5) {
					learningC = Double.parseDouble(tmp);
					count1++;
					continue;
				}

				// koeficient uceni
				if (count1 == 6) {
					fromLastC = Double.parseDouble(tmp);
					count1++;
					continue;
				}

				if (count1 == 7) {
					trainCount = Integer.parseInt(tmp);
					trainSet = new double[trainCount
							* (numOfInputs + layers[layers.length - 1])];
					count1++;
					continue;
				}

				if (count1 == 8) {
					spl = tmp.split("\\s+");
					for (int i = 0; i < numOfInputs
							+ layers[layers.length - 1]; i++) {
						trainSet[i
								+ count2
								* (numOfInputs + layers[layers.length - 1])] = Double
								.parseDouble(spl[i]);
					}
					count2++;
					if (count2 == trainCount) {
						count2 = 0;
						count1++;
						continue;
					}

				}

				if (count1 == 9) {
					testCount = Integer.parseInt(tmp);
					testSet = new double[testCount * numOfInputs];
					count1++;
					continue;
				}

				if (count1 == 10) {
					spl = tmp.split("\\s+");
					for (int i = 0; i < numOfInputs; i++) {
						testSet[i + count2 * numOfInputs] = Double
								.parseDouble(spl[i]);
					}
					count2++;
					if (count2 == testCount) {
						break;
					}

				}

			}// while
			prevedDo1();
			//printTestArr(this.trenovaci);

			bfrd.close();
		

		} catch (Exception e) {
			// Nacita se dobre. A basta!
			e.printStackTrace();
		}

	}// readfile

	private void printTestArr(double[] arr) {
		System.out.println(Arrays.toString(arr));

	}

	/**
	 * fce prevede vstupni hodnoty do rozsahu <0-1> podle vstupniho souboru
	 */
	private void prevedDo1() {

		// Prevod trenovacich dat na interval <0 - 1>
		int cc = 0;
		for (int i = 0; i < this.trainSet.length; i += (numOfInputs + layers[layers.length - 1])) {
			cc = 0;
			for (int j = 0; j < numOfInputs; j++) {
				this.trainSet[i + j] -= popisVstupu[cc];
				this.trainSet[i + j] /= Math.abs(popisVstupu[cc]
						- popisVstupu[cc + 1]);
				cc += 2;
			}

		}// for

		// Prevod testovacich na interval <0 - 1>
		for (int i = 0; i < this.testSet.length; i += numOfInputs) {
			cc = 0;
			for (int j = 0; j < numOfInputs; j++) {
				this.testSet[i + j] -= popisVstupu[cc];
				this.testSet[i + j] /= Math.abs(popisVstupu[cc]
						+ popisVstupu[cc + 1]);
				cc += 2;

			}

		}// for

	}

}