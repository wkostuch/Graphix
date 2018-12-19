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
import java.awt.image.BufferedImage;

public class GraphixVisuals {

	private JFrame frame;
	private GraphPanel canvas;
	static Graphix backend;
	private GraphixTextOutput output;
	
	// These are only used to feed the GraphPanel constructor,
	// they are not used anywhere else in this class
	Vertex[] vertexList;
	Edge[] edgeList;
	
	// Size of the computer screen
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


	/**
	 * Launch the application.
	 */
	public static void start(String[] args) {
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
		
		// Initialize an empty graph by default
		initializeGraph(false);
		
		JButton btnFunctions = new JButton("Show Graph Editor");
		btnFunctions.setPreferredSize(new Dimension((int)((sWidth * 0.66) / 20),
													(int)((sHeight * 0.66) / 20)));
		btnFunctions.addActionListener(new ActionListener() {
			@Override
			// Open the GraphixFunctions window
			public void actionPerformed(ActionEvent e) {
				GraphixFunctions funcWindow = new GraphixFunctions(sWidth,
																   sHeight,
																   backend.orderedKeyArray(),
																   backend.orderedEdgeArray(),
																   canvas,
																   backend,
																   output);
			}
		});
		frame.getContentPane().add(btnFunctions, BorderLayout.SOUTH);
		
		JButton btnFileSelect = new JButton("Display Graph From File");
		btnFileSelect.setPreferredSize(new Dimension((int)((sWidth * 0.66) / 20),
													 (int)((sHeight * 0.66) / 20)));
		btnFileSelect.addActionListener(new ActionListener() {
			@Override
			// Use the file chooser to open a graph from a file
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().remove(canvas);
				initializeGraph(true);
				canvas.revalidate();
				canvas.repaint();
			}
		});
		frame.getContentPane().add(btnFileSelect, BorderLayout.NORTH);
		
		Graphix.setWindowSize(new Dimension((int)((sWidth / 2) - (frame.getSize().width / 2)),
						  	  				(int)((sHeight / 2) - (frame.getSize().height / 2))));
	}
	
	
	/**
	 * Initializes the backend and draws the graph
	 */
	private void initializeGraph(boolean fromFile) {
		// Create a text output box
		output = new GraphixTextOutput();
		
		// Start the backend running
		backend = new Graphix(output);
			
		if (fromFile == true) {
			// Read in a file selected by the user using a file chooser
			backend.readGraph(getFilePath());
		}
				
		// Initialize the JLists of graph components
		vertexList = backend.orderedKeyArray();
		edgeList = backend.orderedEdgeArray();
		
		canvas = new GraphPanel(vertexList, edgeList);
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		frame.revalidate();
		frame.repaint();
	}
	
	
	/**
	 * Uses a GUI popup window to allow the user to select a file from the filesystem
	 * to use as the graph file
	 * 
	 * Borrowed code from http://www.cs.cornell.edu/courses/JavaAndDS/files/io6JFileChooser.pdf
	 */
	private String getFilePath() {
		JFileChooser jd = new JFileChooser(System.getProperty("user.dir"));
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
