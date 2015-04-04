import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class GUI extends JPanel {

	// GUI Tridy
	private JFrame f;
	private JButton b1;
	private JTextArea jt;
	private JScrollPane scroll;

	private int fWidth = 580;
	private int fHeight = 500;

	public GUI() {

		this.setBackground(Color.lightGray);
		this.setLayout(null);
		mainWindow();

	}

	private void mainWindow() {
		f = new JFrame("BACKPROPAGATION-BAT0014");
		f.setVisible(true);
		f.setSize(fWidth, fHeight);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(this);

		int buttonXpos = 425;
		// Select File
		b1 = new JButton("Select File");
		b1.setBounds(buttonXpos, 30, 130, 35);

		// Vyber souboru se vstupnimi daty
		b1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();

				}
			}
		});
		this.add(b1);

		// Text Area
		jt = new JTextArea();
		jt.setEditable(false);
		scroll = new JScrollPane(jt);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(10, 310, fWidth - 25, 150);
		scroll.setAutoscrolls(true);
		this.add(scroll);
		scroll.repaint();

	}

}
