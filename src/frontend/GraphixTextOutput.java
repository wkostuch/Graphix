package frontend;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.JTextPane;

public class GraphixTextOutput extends JFrame {

	private JPanel contentPane;
	static JTextPane outputPane;


	/**
	 * Create the frame.
	 */
	public GraphixTextOutput() {
		this(450, 300);
	}
	
	/**
	 * Overloaded constructor that accepts width and height
	 * @param width
	 * @param height
	 */
	public GraphixTextOutput(int width, int height) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, width, height);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		outputPane = new JTextPane();
		contentPane.add(outputPane, BorderLayout.CENTER);
	}
	
	/**
	 * Public method for adding text to the JTextPane
	 * Accepts a string and appends it to the end of the displayed text
	 * @param strOutput
	 */
	public void output(String strOutput) {
		try {
			outputPane.getDocument().insertString(outputPane.getDocument().getLength(),
												  strOutput, 
												  null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
