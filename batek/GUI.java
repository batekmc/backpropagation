package batek;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import basicClient.BasicClient;

public class GUI extends JPanel {

	// Frame-----------------------
	private JFrame f;
	private JButton b1;
	private JButton bStop;
	private JButton bStart;
	private JButton bSave;
	private JButton bLoad;
	private JTextArea jt;
	private JScrollPane scroll;

	// User inputs------------------
	private JTextField learningC;
	private JTextField prevStepC;
	private JTextField layersN;
	private JTextField testData;
	private JTextField expErr;
	
	private JTextField connectTF;

	private JLabel learningCA;
	private JLabel prevStepCA;
	private JLabel layersNA;
	private JLabel testDataA;
	private JLabel expErrA;
	
	private JLabel connectL;

	private JButton bLe;
	private JButton bP;
	private JButton bLa;
	private JButton bT;
	private JButton bCh;
	
	private JButton bConnect;

	// Backpropagation-------------
	private Backpropagation bp;
	private List<String> queue;

	// WINDOW SIZE
	private int fWidth = 580;
	private int fHeight = 500;

	public GUI() {

		this.setBackground(Color.lightGray);
		this.setLayout(null);
		mainWindow();

	}

	private void initBackPropagation(File f) {
		this.queue = Collections.synchronizedList(new LinkedList<String>());
		this.bp = new Backpropagation(f, queue);

	}

	private void updateText() {
		Thread t = new Thread() {
			String tmp;

			public void run() {
				{
					while (true) {
						if (!queue.isEmpty()) {
							tmp = queue.remove(0);
							if (tmp.equals("EOF")) {
								String s[] = bp.testFileData();
								for (String a : s) {
									jt.append(a);
									jt.setCaretPosition(jt.getDocument()
											.getLength());
								}
								return;
							}
							jt.append(tmp);
							jt.setCaretPosition(jt.getDocument().getLength());
						}
					}
				}
			}
		};
		t.start();

	}

	/**
	 * Main window of the program. ...
	 */
	private void mainWindow() {
		f = new JFrame("BACKPROPAGATION-BAT0014");
		f.setVisible(true);
		f.setSize(fWidth, fHeight);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);

		// Buttons---------------------------------
		int buttonXpos = 425, buttonYpos = 10;
		// Select File
		b1 = new JButton("Select File");
		b1.setBounds(buttonXpos, buttonYpos, 130, 35);

		// Choose training data file
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					initBackPropagation(f);

				}
			}
		});
		this.add(b1);
		buttonYpos += 45;

		bStart = new JButton("Start");
		bStart.setBounds(buttonXpos, buttonYpos, 130, 35);
		bStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (bp != null) {
					if (!bp.isRunning()) {
						Thread t = new Thread(bp);
						t.start();
						updateText();
					}

				}
			}
		});
		this.add(bStart);

		buttonYpos += 45;
		bStop = new JButton("Stop learning");
		bStop.setBounds(buttonXpos, buttonYpos, 130, 35);
		bStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (bp != null) {
					if (bp.isRunning())
						bp.stopLearning();
				}
			}
		});
		this.add(bStop);

		buttonYpos += 45;
		// Save network
		bSave = new JButton("Save");
		bSave.setBounds(buttonXpos, buttonYpos, 130, 35);

		// Save
		bSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(bp != null){
	                JFileChooser saveFile = new JFileChooser();
	                saveFile.showSaveDialog(null);
	                File fs = saveFile.getSelectedFile();
					
					if( !bp.saveNetwork(fs.getAbsolutePath()) ){
						JOptionPane.showMessageDialog(f, "My Goodness, FAIL!");
					}
					else{
						jt.append(fs.getName() + " - Saved");
						jt.setCaretPosition(jt.getDocument()
								.getLength());						
					}
				}
				else
					JOptionPane.showMessageDialog(f, "My Goodness, FAIL!");
			}
		});
		this.add(bSave);
		
		buttonYpos += 45;
		// Save network
		bLoad = new JButton("Load");
		bLoad.setBounds(buttonXpos, buttonYpos, 130, 35);

		// Choose training data file
		bLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File ff = fileChooser.getSelectedFile();
					if(bp == null)
						bp = new Backpropagation();
					
						if(!bp.loadNetwork(ff))
							JOptionPane.showMessageDialog(f, "My Goodness, FAIL!");
						else{
							jt.append(ff.getName() + " - Loaded");
							jt.setCaretPosition(jt.getDocument()
									.getLength());
						}
							
				}
			}
		});
		this.add(bLoad);
		
		buttonYpos += 45;
		// connect to server
		bConnect = new JButton("GO!");
		bConnect.setBounds(buttonXpos, buttonYpos, 130, 35);

		// connect
		bConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String spl[] = connectTF.getText().split("\\s+");
				if(spl.length < 5)
				{
					connectTF.setText("INVALID input");
					connectTF.repaint();
					return;
				}
				try {
					int port = Integer.parseInt(spl[1]);
					BasicClient bc = new BasicClient(spl[0], port, spl[2] , spl[3], spl[4], bp);
					spl[0] = bc.connect();
					connectTF.setText(spl[0]);
					connectTF.repaint();
					
				} catch (Exception e) {
					connectTF.setText("INVALID input");
					connectTF.repaint();
					return;
				}
			}
		});
		this.add(bConnect);
		
		
		
		// Text Area
		jt = new JTextArea();
		jt.setEditable(false);
		scroll = new JScrollPane(jt);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(10, 280, fWidth - 25, 180);
		scroll.setAutoscrolls(true);
		this.add(scroll);
		scroll.repaint();

		userInputComponents();

	}

	private void userInputComponents() {
		// user inputs
		int a = 10, b = 10, c = 130, d = 35, e = 45, f = 145;
		a += f;
		c += 20;

		learningC = new JTextField("(0-1)");
		learningC.setBounds(a, b, c, d);
		learningC.setVisible(true);
		b += e;
		prevStepC = new JTextField("(0-1)");
		prevStepC.setBounds(a, b, c, d);
		prevStepC.setVisible(true);
		b += e;
		layersN = new JTextField("separate with space");
		layersN.setBounds(a, b, c, d);
		layersN.setVisible(true);
		b += e;
		expErr = new JTextField("(0-1)%");
		expErr.setBounds(a, b, c, d);
		expErr.setVisible(true);
		b += e;
		testData = new JTextField("separate with sapce");
		testData.setBounds(a, b, c, d);
		testData.setVisible(true);
		b += e;
		connectTF = new JTextField("localhost 9460 Zavod Batek Fabia");
		connectTF.setBounds(a, b, c + 100, d);
		connectTF.setVisible(true);

		// this.repaint();
		b += e;
		this.add(learningC);
		this.add(prevStepC);
		this.add(layersN);
		this.add(testData);
		this.add(expErr);
		this.add(connectTF);

		// input description
		c -= 40;
		a -= f;
		b = 10;
		learningCA = new JLabel("Learning Coef.:");
		learningCA.setBounds(a, b, c, d);
		learningCA.setVisible(true);
		b += e;
		prevStepCA = new JLabel("Prev. Step. inf.:");
		prevStepCA.setBounds(a, b, c, d);
		prevStepCA.setVisible(true);
		b += e;
		layersNA = new JLabel("Neuron layers");
		layersNA.setBounds(a, b, c, d);
		layersNA.setVisible(true);
		b += e;
		expErrA = new JLabel("Max Error(%)");
		expErrA.setBounds(a, b, c, d);
		expErrA.setVisible(true);
		b += e;
		testDataA = new JLabel("TestData");
		testDataA.setBounds(a, b, c, d);
		testDataA.setVisible(true);
		b += e;
		connectL = new JLabel("Server");
		connectL.setBounds(a, b, c, d);
		connectL.setVisible(true);

		this.add(learningCA);
		this.add(prevStepCA);
		this.add(layersNA);
		this.add(testDataA);
		this.add(expErrA);
		this.add(connectL);

		a += f;
		a += c + 50;
		b = 10;
		c = 70;
		// learning coefficient
		bLe = new JButton("Set");
		bLe.setBounds(a, b, c, d);
		bLe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (bp != null) {
					try {
						String[] input = learningC.getText().split("\\s+");
						double val = Double.parseDouble(input[0]);
						if (bp.setLearningC(val)) {
							learningC.setText("UPDATED to: " + val);
						} else {
							learningC.setText("FAIL!, please try again");
						}
					} catch (NumberFormatException e) {
						learningC.setText("FAIL!, please try again");
					}
				}
				bLe.repaint();
			}

		});
		b += e;
		// previous step coefficient
		bP = new JButton("Set");
		bP.setBounds(a, b, c, d);
		bP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (bp != null) {
					try {
						String[] input = prevStepC.getText().split("\\s+");
						double val = Double.parseDouble(input[0]);
						if (bp.setPrevStepC(val)) {
							prevStepC.setText("UPDATED to: " + val);
						} else {
							prevStepC.setText("FAIL!, please try again");
						}
					} catch (NumberFormatException e) {
						prevStepC.setText("FAIL!, please try again");
					}
				}
				bP.repaint();
			}

		});
		b += e;
		// layers
		bLa = new JButton("Set");
		bLa.setBounds(a, b, c, d);
		bLa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (bp != null) {
					try {
						String[] input = layersN.getText().split("\\s+");
						int val[] = new int[input.length];
						for (int i = 0; i < input.length; i++)
							val[i] = Integer.parseInt(input[i]);
						if (bp.setLayers(val)) {
							layersN.setText("UPDATED to: "
									+ Arrays.toString(val));
						} else {
							layersN.setText("FAIL!, please try again");
						}
					} catch (NumberFormatException e) {
						layersN.setText("FAIL!, please try again");
					}
				}
				bLa.repaint();
			}

		});

		b += e;
		// expected error
		bCh = new JButton("Set");
		bCh.setBounds(a, b, c, d);
		bCh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (bp != null) {
					try {
						String[] input = expErr.getText().split("\\s+");
						double val = Double.parseDouble(input[0]);
						if (bp.setMaxError(val)) {
							expErr.setText("UPDATED to: " + val);
						} else {
							expErr.setText("FAIL!, please try again");
						}
					} catch (NumberFormatException e) {
						expErr.setText("FAIL!, please try again");
					}
				}
				bCh.repaint();
			}

		});

		b += e;
		// test input
		bT = new JButton("Test");
		bT.setBounds(a, b, c, d);
		bT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (bp != null) {
					try {
						String[] input = testData.getText().split("\\s+");
						double val[] = new double[input.length];
						for (int i = 0; i < input.length; i++)
							val[i] = Double.parseDouble(input[i]);
						if (bp.testInput(val)) {
							testData.setText("input OK");
							// display network response
							double[] s = bp.testData(val);
							jt.append("Input: " + Arrays.toString(val)
									+ "\nOutput: " + Arrays.toString(s) + "\n");
							jt.setCaretPosition(jt.getDocument().getLength());

						} else {
							testData.setText("FAIL!, please try again");
						}

					} catch (NumberFormatException e) {
						testData.setText("FAIL!, please try again");
					}
				}
				bT.repaint();
			}

		});
		this.add(bLe);
		this.add(bP);
		this.add(bLa);
		this.add(bT);
		this.add(bCh);

		this.repaint();

	}

}
