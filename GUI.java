import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class GUI extends JPanel {

	// Frame-----------------------
	private JFrame f;
	private JButton b1;
	private JButton bStop;
	private JButton bStart;
	private JTextArea jt;
	private JScrollPane scroll;

	// User inputs------------------
	private JTextField learningC;
	private JTextField prevStepC;
	private JTextField layersN;
	private JTextField testData;

	private JLabel learningCA;
	private JLabel prevStepCA;
	private JLabel layersNA;
	private JLabel testDataA;

	private JButton bLe;
	private JButton bP;
	private JButton bLa;
	private JButton bT;

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
							if (tmp.equals("EOF"))
								break;
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

		// Text Area
		jt = new JTextArea();
		jt.setEditable(false);
		scroll = new JScrollPane(jt);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(10, 310, fWidth - 25, 150);
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
		testData = new JTextField("separate with sapce");
		testData.setBounds(a, b, c, d);
		testData.setVisible(true);
		// this.repaint();
		b += e;
		this.add(learningC);
		this.add(prevStepC);
		this.add(layersN);
		this.add(testData);

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
		testDataA = new JLabel("TestData");
		testDataA.setBounds(a, b, c, d);
		testDataA.setVisible(true);

		this.add(learningCA);
		this.add(prevStepCA);
		this.add(layersNA);
		this.add(testDataA);

		a += f;
		a += c + 50;
		b = 10;
		c = 70;
		bLe = new JButton("Set");
		bLe.setBounds(a, b, c, d);
		bLe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// TODO
			}

		});
		b += e;
		bP = new JButton("Set");
		bP.setBounds(a, b, c, d);
		bP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// TODO
			}

		});
		b += e;
		bLa = new JButton("Set");
		bLa.setBounds(a, b, c, d);
		bLa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// TODO
			}

		});
		b += e;
		bT = new JButton("Test");
		bT.setBounds(a, b, c, d);
		bT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// TODO
			}

		});
		this.add(bLe);
		this.add(bP);
		this.add(bLa);
		this.add(bT);

		this.repaint();

	}

}
