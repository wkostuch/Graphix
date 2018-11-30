package frontend;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JFrame;

import backend.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class GraphixVisuals {

	private JFrame frame;
	private GraphPanel canvas;
	static Graphix backend;
	Vertex[] vertexList;
	Edge[] edgeList;
	
	/**
	 * MouseListener override
	 */
	MouseListener caspian = new MouseListener() {
		@Override
		public void mouseReleased(MouseEvent e) {
			int mouseX = e.getX();
			int mouseY = e.getY();
			
			if (e.getButton() == MouseEvent.BUTTON2) {
				Vertex v = new Vertex("", e.getX(), e.getY());
			}
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	// Size of the computer screen
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphixVisuals window = new GraphixVisuals();
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
	public GraphixVisuals() {
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
		
		initializeGraph();
		
		canvas = new GraphPanel(vertexList, edgeList);
		canvas.addMouseListener(caspian);
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		canvas.repaint();
		
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
		
		Graphix.setWindowSize(new Dimension((int)((sWidth / 2)
											- (frame.getSize().width / 2)),
						  	  				(int)((sHeight / 2)
						  	  				- (frame.getSize().height / 2))));
	}
	
	
	/**
	 * Initializes the backend and draws the graph
	 */
	private void initializeGraph() {
		
		// Start the backend running
		backend = new Graphix();
		
		// Read in a file selected by the user using a file chooser
		backend.readGraph(getFilePath());
		
		// Initialize the JLists of graph components
				vertexList = backend.orderedKeyArray();
				edgeList = backend.orderedEdgeArray();
		
		/*
		// Test configuration
		backend = Graphix.testGraph();
		displayBufferedImage(backend.getImage());
		*/
	}
	
	
	/**
	 * Uses a GUI popup window to allow the user to select a file from the filesystem
	 * to use as the graph file
	 * 
	 * Borrowed code from http://www.cs.cornell.edu/courses/JavaAndDS/files/io6JFileChooser.pdf
	 */
	private String getFilePath() {
		JFileChooser jd = new JFileChooser();
		jd.setDialogTitle("Choose output file");
		int returnVal= jd.showSaveDialog(null);
		if (returnVal != JFileChooser.APPROVE_OPTION) 
			return null;
		return String.valueOf(jd.getSelectedFile().toPath());
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
	
	
	/**
	 * Returns the size of the canvas
	 */
	Dimension getCanvasSize() {
		return canvas.getSize();
	}
	
}
