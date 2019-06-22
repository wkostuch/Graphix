package frontend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import backend.*;

/*
 * We implement mouse handling functions in this class.
 * The class can be instantiated wherever MouseListener would be instantiated elsewhere in the code.
 * All the custom features that use mouse clicks will be implemented here.
 */
public class GraphixMouseHandlers implements MouseListener {
	
	// Fields to hold graphical items so this class can edit them.
	Graphix backend;
	GraphPanel drawing;
	GraphixTextOutput output;
	
	
	/**
	 * Default constructor for this class.  Accepts items from the caller to permit modification
	 * of the graphical elements of the program.
	 * @param backendObj
	 * @param canvas
	 * @param outputBox
	 */
	public GraphixMouseHandlers(Graphix backendObj, GraphPanel canvas, GraphixTextOutput outputBox) {
		backend = backendObj;
		drawing = canvas;
		output = outputBox;
	}
	
	
	private boolean foundMatchingVertex = false;
	private Vertex matchingVertex;
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	
	/**
	 * Supporting method to check whether a given set of coordinates matches the coordinates of any
	 * existing vertex.  Returns true if a match is found, and false if no match is found.
	 */
	private boolean matchingVertex(int x, int y) {
		// Get the array of existing vertices from the backend.
		Vertex[] vArr = backend.orderedKeyArray();
		
		// Walk through the array of vertices.
		// If the position of the click matches the position of one of the vertices, we didn't click on
		// empty space, so return false.
		for (Vertex v : vArr) {
			if ((x >= v.getX() && x <= v.getX() + drawing.getDiameter()) &&
				(y >= v.getY() && y <= v.getY() + drawing.getDiameter()))
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * Supporting method to check whether the mouse has clicked on an existing vertex or edge
	 * Returns a Boolean that is true if mouse clicked on empty space, and false if it clicked on an
	 * existing vertex or edge.
	 */
	private boolean isEmptySpace(MouseEvent e) {
		// Get the click position from the MouseEvent instance.
		int x = e.getX();
		int y = e.getY();
		
		// This will be the return value of the method.
		boolean empty = true;
		
		empty = !matchingVertex(x, y);
		
		// TODO: Add check for edges
		
		// If no matches are found in the loop above, return true.
		return empty;
	}
	
	
	/**
	 * Handles what to do when the mouse is released.
	 * When the mouse is released, either a vertex is selected,
	 * or an (x,y) pair is put into the text boxes, allowing
	 * a new vertex to be added.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// First we check whether the left or right button was clicked.  Each button will perform
		// a different action.
		// Anything that happens on a left click goes on in here.
		// Left-click creates new vertices and edges.
		if (e.getButton() == MouseEvent.BUTTON1) {
			// This flag will be used to determine whether we're making new vertices and edges, or
			// editing existing ones.
			boolean newItem;
			
			// This check will flag the newItem boolean as true if the user did not click on an existing
			// vertex or edge, and it will flag newItem as false if the user clicked on an existing vertex
			// or edge.
			newItem = isEmptySpace(e);
			
			// If the mouse was clicked on empty space, we'll make new items.
			if (newItem == true) {
				// A click should either trigger the beginning of a chain of vertices and edges, or continue
				// a chain that has already begun.
				// We're going to start by asking whether or not a chain is already in progress.  A backend
				// field will act as a flag.
				if (backend.getActiveChain() == false) {
					// If we're not continuing a chain, we're beginning one, so we'll set this flag to true in 
					// that instance.
					backend.flipActiveChain();
					
					// We're going to start a new chain now.  We'll start by making a new vertex and assigning
					// it a name.
					int x = e.getX();
					int y = e.getY();
					backend.addVertex(x, y);
				}
				
				// If we're continuing a chain, we create a new vertex and connect it to the last one created
				else if (backend.getActiveChain() == true) {
					// Add a new Vertex and get the most recently added one
					Vertex lastVertex = backend.getLastVertex();
					Vertex nextVertex = backend.addVertex(e.getX(), e.getY());
					
					// Create an Edge between those two Vertices
					backend.addEdge(lastVertex, nextVertex);
					
					// If the user double-clicked, we end the chain
					if (e.getClickCount() == 2)
						backend.flipActiveChain();
				}
			}
			
			// If the mouse was clicked on an existing item, we'll edit that item.
			else if (newItem == false) {
				// TODO: Add Vertex editing--single click creates new Vertex, double click moves existing one
				// TODO: Add Edge editing--single click on one Vertex and then another creates an edge
			}
		}
		
		// Anything that happens on a right click goes on in here.
		// Right-click deletes existing vertices and edges.
		else if (e.getButton() == MouseEvent.BUTTON2) {
			// TODO: Add Vertex detection so the program knows what Vertex the user is trying to delete
			// TODO: Add Edge detection to detect what edge the user may be trying to delete
		}
	}
}
