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
import java.util.List;

@SuppressWarnings("serial")
public class GraphixFunctions extends JFrame {
	
	private JList<Edge> edges;
	private JList<Vertex> vertices;
	
	private GraphPanel drawing;	// Used for storing the canvas so it can be edited
	
	private Vertex currVertex;
	
	Graphix backend;
	Graphix mwst;
	boolean isMWST;
	
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
		getContentPane().setLayout(new GridLayout(2, 3));
	}
	
	/**
	 * Overloaded constructor that places and sizes the window based on the
	 * position and size of Graphix
	 */
	public GraphixFunctions(int width,
							int height,
							Vertex[] vArr,
							Edge[] eArr,
							GraphPanel canvas,
							Graphix backendObj) {
		this();
		
		this.setBounds(100, 100, width / 4, height / 4);
		
		vertices = new JList<Vertex>(vArr);
		vertices.addListSelectionListener(new ListSelectionListener() {
			@Override
			// Save the currently selected vertex so it can be edited
			public void valueChanged(ListSelectionEvent e) {
				if (vertices.getSelectedValue() != null) {
					currVertex = vertices.getSelectedValue();
					
					// Initialize values in editor text boxes
					// Editor is initialized (below) before this event could ever be called
					xBox.setText(Integer.toString(currVertex.getX()));
					yBox.setText(Integer.toString(currVertex.getY()));
					nameBox.setText(currVertex.getName());
				}
			}
		});
		
		edges = new JList<Edge>(eArr);
		
		drawing = canvas;
		backend = backendObj;
		
		// Add the JLists to the pane
		getContentPane().add(vertices);
		getContentPane().add(edges);
		getContentPane().add(new EditorPanel());
		getContentPane().add(new ButtonPanel());
		
		this.setVisible(true);
	}
	
	
	/**
	 * Local extension of JPanel for miscellaneous buttons
	 */
	class ButtonPanel extends JPanel {
		
		/**
		 * Constructor
		 */
		public ButtonPanel() {
			this.setLayout(new GridLayout(3, 2));
			
			JButton btnToggleMWST = new JButton("Toggle MWST");
			btnToggleMWST.addActionListener(new ActionListener() {
				// Action listener toggles the MWST display
				// Each time it is pressed, it will switch the displayed graph between the
				// original graph and the graph of the MWST
				@Override
				public void actionPerformed(ActionEvent e) {
					mwst = backend.MWST();
					if (isMWST == false) {
						isMWST = true;
						// Replace the displayed graph with the MWST graph
						updateVertexList(mwst);
						updateEdgeList(mwst);
						drawing.setArrays(mwst.orderedKeyArray(), mwst.orderedEdgeArray());
					} else if (isMWST == true) {
						isMWST = false;
						// Replace the displayed graph with the backend graph
						updateVertexList(backend);
						updateEdgeList(backend);
						drawing.setArrays(backend.orderedKeyArray(), backend.orderedEdgeArray());
					}
				}	
			});
			
			JButton btnIsTree = new JButton("Is Tree");
			btnIsTree.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					backend.isTree();
				}
			});
			
			JButton btnSaveToFile = new JButton("Save Graph To File");
			btnSaveToFile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Saves the graph on the screen to a file, with all edits that
					// have been added since it was first loaded
					// TODO: Add method call when Will adds it to Graphix
				}
			});
			
			JButton btnAddEdge = new JButton("Add Edge");
			btnAddEdge.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// If too few vertices are selected, the error is absorbed by the
					// try-finally block and nothing happens.
					// If too many vertices are selected, we only use the first two.
					try {
						List<Vertex> newEdge = vertices.getSelectedValuesList();
						backend.addEdge(newEdge.get(0), newEdge.get(1));
					} finally {
						updateVertexList(backend);
						updateEdgeList(backend);
						drawing.setArrays(backend.orderedKeyArray(), backend.orderedEdgeArray());
					}
				}
			});
			
			JButton btnRemoveVertex = new JButton("Remove Vertex");
			btnRemoveVertex.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Remove the selected vertex from the list
					// TODO: Add method call when Will adds it to Graphix
				}
			});
			
			JButton btnRemoveEdge = new JButton("Remove Edge");
			btnRemoveEdge.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Remove the selected edge from the list
					// TODO: Add method call when Will adds it to Graphix
				}
			});
			
			this.add(btnToggleMWST);
			this.add(btnIsTree);
			this.add(btnSaveToFile);
			this.add(btnAddEdge);
			this.add(btnRemoveVertex);
			this.add(btnRemoveEdge);
		}
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
					applyVertexEdit(currVertex);
					
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
	private void applyVertexEdit(Vertex newVertex) {
		// Apply the edit through the backend
		// If the name in the nameBox is not the name of a preexisting vertex,
		// create a new one.  Otherwise, edit an existing vertex.
		boolean add = true;
		
		for (Vertex v : backend.orderedKeyArray()) {
			if (v.getName() == nameBox.getText())
				add = false;
		}
		
		if (add == false) {
			backend.changeVertex(newVertex, 
								 Integer.parseInt(xBox.getText()),
								 Integer.parseInt(yBox.getText()));
		} else if (add == true) {
			backend.addVertex(new Vertex(nameBox.getText(),
										 Integer.parseInt(xBox.getText()),
										 Integer.parseInt(yBox.getText())));
		}
		
		// Update the JLists
		this.updateVertexList(backend);
		this.updateEdgeList(backend);
		
		// Update the drawing canvas
		drawing.setArrays(backend.orderedKeyArray(), backend.orderedEdgeArray());
	}
	
	
	/**
	 * Accepts an array of vertices and uses it to update the contents
	 * of the JList of vertices
	 * @param vArr
	 */
	public void updateVertexList(Graphix source) {
		Vertex[] vArr = source.orderedKeyArray();
		
		DefaultListModel<Vertex> model = new DefaultListModel<Vertex>();
		
		for (Vertex v : vArr)
			model.addElement(v);
		
		vertices.setModel(model);
	}
	
	
	/**
	 * Accepts an array of edges and uses it to update the contents
	 * of the JList of edges
	 */
	public void updateEdgeList(Graphix source) {
		Edge[] eArr = source.orderedEdgeArray();
		
		DefaultListModel<Edge> model = new DefaultListModel<Edge>();
		
		for (Edge e : eArr)
			model.addElement(e);
		
		edges.setModel(model);
	}

}
