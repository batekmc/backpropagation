import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public class FileParser {

	private int pocetVrstev;
	private int pocetVstupu;
	private double koefUceni;
	private double koefZpredchoziho;
	private double popisVstupu[];
	/*
	 * posledni je vystupni vrstva
	 */
	private int pocetVeVrstvach[];
	private String popisVystupu[];

	/*
	 * Trenovaci
	 */
	private int pocetPrvkuTrenovaci;
	private double trenovaci[];

	/*
	 * Testovaci
	 */
	private int pocetTestovacich;
	private double testovaci[];

	private File f;
	
	/*
	 * GETTRY
	 */	
	public int getPocetVrstev() {
		return pocetVrstev;
	}

	public int getPocetVstupu() {
		return pocetVstupu;
	}

	public double getKoefUceni() {
		return koefUceni;
	}

	public double getKoefZpredchoziho() {
		return koefZpredchoziho;
	}

	public double[] getPopisVstupu() {
		return popisVstupu;
	}

	public int[] getPocetVeVrstvach() {
		return pocetVeVrstvach;
	}

	public String[] getPopisVystupu() {
		return popisVystupu;
	}

	public int getPocetPrvkuTrenovaci() {
		return pocetPrvkuTrenovaci;
	}

	public double[] getTrenovaci() {
		return trenovaci;
	}

	public int getPocetTestovacich() {
		return pocetTestovacich;
	}

	public double[] getTestovaci() {
		return testovaci;
	}

	public File getF() {
		return f;
	}

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
					pocetVrstev = Integer.parseInt(tmp);
					pocetVeVrstvach = new int[pocetVrstev];
					count1++;
					continue;
				}

				// pocet vstupu
				if (count1 == 1) {
					pocetVstupu = Integer.parseInt(tmp);
					popisVstupu = new double[pocetVstupu * 2];
					count1++;
					continue;
				}

				// Popis vstupu[]
				if (count1 == 2) {
					spl = tmp.split("\\s+");
					popisVstupu[count2 * 2] = Double.parseDouble(spl[1]);
					popisVstupu[count2 * 2 + 1] = Double.parseDouble(spl[2]);
					count2++;
					if (count2 == pocetVstupu) {
						count2 = 0;
						count1++;
					}
					continue;
				}

				// pocet neuronu ve vrstvach
				if (count1 == 3) {
					spl = tmp.split("\\s+");
					for (int i = 0; i < pocetVrstev; i++)
						pocetVeVrstvach[i] = Integer.parseInt(spl[i]);
					count1++;
					popisVystupu = new String[pocetVeVrstvach[pocetVeVrstvach.length - 1]];
					continue;

				}

				// popis vystupu
				if (count1 == 4) {
					popisVystupu[count2] = tmp;
					count2++;
					if (count2 == pocetVeVrstvach[pocetVeVrstvach.length - 1]) {
						count1++;
						count2 = 0;

					}
					continue;

				}

				// koeficient uceni
				if (count1 == 5) {
					koefUceni = Double.parseDouble(tmp);
					count1++;
					continue;
				}

				// koeficient uceni
				if (count1 == 6) {
					koefZpredchoziho = Double.parseDouble(tmp);
					count1++;
					continue;
				}

				if (count1 == 7) {
					pocetPrvkuTrenovaci = Integer.parseInt(tmp);
					trenovaci = new double[pocetPrvkuTrenovaci
							* (pocetVstupu + pocetVeVrstvach[pocetVeVrstvach.length - 1])];
					count1++;
					continue;
				}

				if (count1 == 8) {
					spl = tmp.split("\\s+");
					for (int i = 0; i < pocetVstupu
							+ pocetVeVrstvach[pocetVeVrstvach.length - 1]; i++) {
						trenovaci[i
								+ count2
								* (pocetVstupu + pocetVeVrstvach[pocetVeVrstvach.length - 1])] = Double
								.parseDouble(spl[i]);
					}
					count2++;
					if (count2 == pocetPrvkuTrenovaci) {
						count2 = 0;
						count1++;
						continue;
					}

				}

				if (count1 == 9) {
					pocetTestovacich = Integer.parseInt(tmp);
					testovaci = new double[pocetTestovacich * pocetVstupu];
					count1++;
					continue;
				}

				if (count1 == 10) {
					spl = tmp.split("\\s+");
					for (int i = 0; i < pocetVstupu; i++) {
						testovaci[i + count2 * pocetVstupu] = Double
								.parseDouble(spl[i]);
					}
					count2++;
					if (count2 == pocetTestovacich) {
						break;
					}

				}

			}// while
			prevedDo1();
			printTestArr(this.trenovaci);

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
		for (int i = 0; i < this.trenovaci.length; i += (pocetVstupu + pocetVeVrstvach[pocetVeVrstvach.length - 1])) {
			cc = 0;
			for (int j = 0; j < pocetVstupu; j++) {
				this.trenovaci[i + j] -= popisVstupu[cc];
				this.trenovaci[i + j] /= Math.abs(popisVstupu[cc]
						- popisVstupu[cc + 1]);
				cc += 2;
			}

		}// for

		// Prevod testovacich na interval <0 - 1>
		for (int i = 0; i < this.testovaci.length; i += pocetVstupu) {
			cc = 0;
			for (int j = 0; j < pocetVstupu; j++) {
				this.testovaci[i + j] -= popisVstupu[cc];
				this.testovaci[i + j] /= Math.abs(popisVstupu[cc]
						+ popisVstupu[cc + 1]);
				cc += 2;

			}

		}// for

	}

}
