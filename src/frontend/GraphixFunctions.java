package frontend;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import backend.*;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphixFunctions extends JFrame {
	
	private JList<Edge> edges;
	private JList<Vertex> vertices;
	
	private Vertex currVertex;	// Used for temporarily storing values to be edited
	private GraphPanel drawing;	// Used for storing the canvas so it can be edited

	/**
	 * Create the frame.
	 */
	public GraphixFunctions() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new GridLayout(2, 2));
	}
	
	/**
	 * Overloaded constructor that places and sizes the window based on the
	 * position and size of Graphix
	 */
	public GraphixFunctions(int width, int height, Vertex[] vArr, Edge[] eArr, GraphPanel canvas) {
		this();
		
		this.setBounds(100, 100, width / 4, height / 4);
		
		vertices = new JList<Vertex>(vArr);
		vertices.addListSelectionListener(new ListSelectionListener() {
			@Override
			// Save the currently selected vertex so it can be edited
			public void valueChanged(ListSelectionEvent e) {
				currVertex = vertices.getSelectedValue();
			}
		});
		
		edges = new JList<Edge>(eArr);
		
		drawing = canvas;
		
		// Add the JLists to the pane
		getContentPane().add(vertices);
		getContentPane().add(edges);
		getContentPane().add(new EditorPanel());
		
		this.setVisible(true);
	}
	
	
	/**
	 * Local extension of JPanel for editing vertices
	 */
	class EditorPanel extends JPanel {
		
		// Text boxes
		JTextField xBox = new JTextField();
		JTextField yBox = new JTextField();
		JTextField nameBox = new JTextField();
					
		// Labels for text boxes
		JLabel xLabel = new JLabel("x");
		JLabel yLabel = new JLabel("y");
		
		// Button
		JButton applyEditBtn = new JButton();
		
		
		/**
		 * Constructor
		 */
		public EditorPanel() {
			this.setLayout(new GridLayout(3, 2));
			
			this.add(xLabel);
			this.add(xBox);
			this.add(yLabel);
			this.add(yBox);
			this.add(nameBox);
			
			applyEditBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					applyVertexEdit(new Vertex(nameBox.getText(),
							   		Integer.parseInt(xBox.getText()),
							   		Integer.parseInt(yBox.getText())));
				}
			});
			
			this.add(applyEditBtn);
		}
	}
	
	
	/**
	 * Used for applying vertex edits
	 */
	private void applyVertexEdit(Vertex newVertex) {
		drawing.changeVertex(currVertex, newVertex);
	}

}
