package frontend;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import backend.*;

/*
 * We implement mouse handling functions in this class.
 * The class can be instantiated wherever MouseListener would be instantiated elsewhere in the code.
 * All the custom features that use mouse clicks will be implemented here.
 */
public class GraphixMouseHandler implements MouseListener {
	
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
	public GraphixMouseHandler(Graphix backendObj, GraphPanel canvas) {
		backend = backendObj;
		drawing = canvas;
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
	
	
	/*
	 * Checks to see if there is an existing Vertex at the position the user clicked.  If there is a
	 * match, it returns the Vertex in that location.
	 */
	private Vertex getMatchingVertex(int x, int y) {
		// Get the array of existing vertices from the backend.
		Vertex[] vArr = backend.orderedKeyArray();
		
		Vertex returnVertex = null;
				
		// Walk through the array of vertices.
		// If the position of the click matches the position of one of the vertices, we didn't click on
		// empty space, so return false.
		for (Vertex v : vArr) {
			if ((x >= v.getX() && x <= v.getX() + drawing.getDiameter()) &&
				(y >= v.getY() && y <= v.getY() + drawing.getDiameter()))
				returnVertex = v;
		}
		
		return returnVertex;
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
	 * 
	 * Programmer's note: Don't use double-click functionality, it is too hard to click fast enough
	 * for Java to recognize it.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// Get the x and y values of the mouse location
		int x = e.getX();
		int y = e.getY();
		
		// We'll need this as well
		Vertex v = null;
		
		// First we check whether the left or right button was clicked.  Each button will perform
		// a different action.
		// Anything that happens on a left click goes on in here.
		// Left-click creates new vertices and edges.
		if (e.getButton() == MouseEvent.BUTTON1) {
			// First we check whether or not the user clicked on an empty space.
			if (isEmptySpace(e) == true) {
				if (e.getClickCount() == 1) {
					// A single click, by default, creates a single Vertex.
					// So, we first want to check whether another Vertex was recently clicked on.  If not,
					// we'll create a new one.
					if (backend.isSavedVertex() == false)
						backend.addVertex(x, y);
					
					// If another existing Vertex was recently selected, but this click is on an empty space,
					// we'll move the Vertex to the empty space.
					else if (backend.isSavedVertex() == true) {
						v = backend.getSavedVertex();
						backend.changeVertex(v, x, y);
						// After this, we'll clear the saved Vertex to avoid unintentional Edge creation
						backend.saveVertex(null);
					}
				}
			}
			
			// If the user clicks on an existing Vertex, we save it (if none is already saved) or we connect
			// it to a saved Vertex with an Edge.
			else if (isEmptySpace(e) == false) {
				if (e.getClickCount() == 1) {
					// First we get the Vertex the user clicked on
					v = getMatchingVertex(x, y);
					
					// If there is no saved Vertex, we save the one the user clicked on
					if (backend.isSavedVertex() == false)
						backend.saveVertex(v);
					
					// If a Vertex is already saved, a single click on another Vertex connects the two
					else if (backend.isSavedVertex() == true) {
						Vertex changedVertex = backend.getSavedVertex();
						backend.addEdge(v, changedVertex);
						// Clear the saved Vertex to be safe.
						backend.saveVertex(null);
					}
				}
			}
		}
		
		// Anything that happens on a right click goes on in here.
		// Right-click deletes existing vertices and edges.
		else if (e.getButton() == MouseEvent.BUTTON3) {
			// Right click doesn't do anything to an empty space.
			// Right click also doesn't support double click functionality.
			if (isEmptySpace(e) == false) {
				// TODO: Add Edge detection to detect what edge the user may be trying to delete.
				
				// For now, if a user left clicks on one Vertex and right clicks on another, the Edge between
				// the two will be removed.
				if (backend.isSavedVertex() == true) {
					// Get both the saved Vertex and the Vertex just right-clicked on.
					Vertex savedVertex = backend.getSavedVertex();
					v = getMatchingVertex(x, y);
					
					// If there's an edge between the two Vertices, remove them.  Otherwise, do nothing.
					if (backend.hasEdge(v, savedVertex) == true)
						backend.removeEdge(v, savedVertex);
					
					// In either case, clear the saved Vertex.
					backend.saveVertex(null);
				}
				
				// If no Vertex is saved, a right click simply removes the Vertex clicked on.
				else if (backend.isSavedVertex() == false) {
					// Get the Vertex that's been clicked on
					v = getMatchingVertex(x, y);
						
					// Remove the selected Vertex
					backend.removeVertex(v);
				}
			}
		}
		
		drawing.setArrays(backend.orderedKeyArray(), backend.orderedEdgeArray());
	}
}
