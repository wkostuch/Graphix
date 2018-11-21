package graphics;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Canvas;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Graphix {

	private JFrame frame;
	private Canvas canvas;
	
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
		frame.setSize(new Dimension((int)(sWidth * 0.66), (int)(sHeight * 0.66)));
		frame.setLocation((int)((sWidth / 2) - (frame.getSize().width / 2)),
						  (int)((sHeight / 2) - (frame.getSize().height / 2)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		canvas = new Canvas();
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		
		JButton btnFunctions = new JButton("Functions");
		btnFunctions.setPreferredSize(new Dimension((int)((sWidth * 0.66) / 20),
														  (int)((sHeight * 0.66) / 20)));
		btnFunctions.addActionListener(new ActionListener() {
			@Override
			// Open the GraphixFunctions window
			public void actionPerformed(ActionEvent e) {
				GraphixFunctions funcWindow = new GraphixFunctions(sWidth, sHeight);
			}
		});
		frame.getContentPane().add(btnFunctions, BorderLayout.SOUTH);
	}
	
	/**
	 * Returns size of canvas as a Dimension
	 * Use for sizing bufferedImage
	 */
	public Dimension getCanvasSize() {
		return new Dimension(canvas.getWidth(), canvas.getHeight());
	}
	
	/**
	 * Takes a BufferedImage and displays it in the application window
	 * @param img
	 */
	public void displayBufferedImage(BufferedImage img) {
		Graphics2D g2 = img.createGraphics();
		canvas.printAll(g2);
		g2.dispose();
	}
	
}
