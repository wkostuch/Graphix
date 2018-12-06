package frontend;

import javax.swing.DefaultListModel;
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
	
	// Fields used in EditorPanel
	// Text boxes
	JTextField xBox;
	JTextField yBox;
	JTextField nameBox;
						
	// Labels for text boxes
	JLabel xLabel;
	JLabel yLabel;
			
	// Button
	JButton applyEditBtn;

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
				// Initialize values in editor text boxes
				// Editor is initialized (below) before this event could ever be called
				xBox.setText(Integer.toString(vertices.getSelectedValue().getX()));
				yBox.setText(Integer.toString(vertices.getSelectedValue().getY()));
				nameBox.setText(vertices.getSelectedValue().getName());
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
		
		/**
		 * Constructor
		 */
		public EditorPanel() {
			this.setLayout(new GridLayout(3, 2));
			
			xBox = new JTextField();
			yBox = new JTextField();
			nameBox = new JTextField();
								
			xLabel = new JLabel("x");
			yLabel = new JLabel("y");
					
			applyEditBtn = new JButton("Apply Edits");
			
			this.add(xLabel);
			this.add(xBox);
			this.add(yLabel);
			this.add(yBox);
			this.add(nameBox);
			
			applyEditBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					applyVertexEdit(vertices.getSelectedValue(),
									new Vertex(nameBox.getText(),
							   				   Integer.parseInt(xBox.getText()),
							   				   Integer.parseInt(yBox.getText())));
					
					xBox.setText("");
					yBox.setText("");
					nameBox.setText("");
				}
			});
			
			this.add(applyEditBtn);
		}
	}
	
	
	/**
	 * Used for applying vertex edits
	 */
	private void applyVertexEdit(Vertex oldVertex, Vertex newVertex) {
		// Apply edits, accept the return
		drawing.changeVertex(oldVertex, newVertex, this);
		
		drawing.repaint();
		this.repaint();
	}
	
	
	/**
	 * Accepts an array of vertices and uses it to update the contents
	 * of the JList of vertices
	 * @param vArr
	 */
	public void updateVertexList(Vertex[] vArr) {
		DefaultListModel<Vertex> model = new DefaultListModel<Vertex>();
		
		for (Vertex v : vArr)
			model.addElement(v);
		
		vertices.setModel(model);
	}
	
	
	/**
	 * Accepts an array of edges and uses it to update the contents
	 * of the JList of edges
	 */
	public void updateEdgeList(Edge[] eArr) {
		DefaultListModel<Edge> model = new DefaultListModel<Edge>();
		
		for (Edge e : eArr)
			model.addElement(e);
		
		edges.setModel(model);
	}

}
