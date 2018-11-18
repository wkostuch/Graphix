package graphics;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Canvas;
import java.awt.Button;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;

public class Graphix {

	private JFrame frame;
	
	// Size of the computer screen
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Graphix window = new Graphix();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Graphix() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int sHeight = (int)screenSize.getHeight();
		int sWidth = (int)screenSize.getWidth();
		frame = new JFrame();
		// Width and height are 3/4 of the computer screen's width and height
		frame.setSize(new Dimension(sWidth * (3/4), sHeight * (3/4)));
		frame.setBounds(sWidth * (1/4), sHeight * (1/4), 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Canvas canvas = new Canvas();
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		
		JButton btnFunctions = new JButton("Functions");
		btnFunctions.addActionListener(ActionEvent e) {
			public void actionPerformed(ActionEvent e) {
				new GraphixFunctions(sWidth, sHeight);
			}
		}
		frame.getContentPane().add(btnFunctions, BorderLayout.SOUTH);
	}
	
}
