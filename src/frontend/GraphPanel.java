package frontend;

import java.awt.Graphics;

import javax.swing.JPanel;

import backend.*;

public class GraphPanel extends JPanel {
	
	Vertex[] vertexArr;
	Edge[] edgeArr;
	
	
	/**
	 * constructor which accepts a vertexList and an edgeList
	 */
	public GraphPanel(Vertex[] v, Edge[] e) {
		vertexArr = v;
		edgeArr = e;
	}
	
	
	@Override
	/**
	 * Override of paintComponent
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		this.removeAll();
		
		int diameter = 10;
		
		for (Vertex v : vertexArr) {
			g.fillOval(v.getX(), v.getY(), diameter, diameter);
		}
		
		for (Edge e : edgeArr) {
			g.drawLine(e.getV1().getX() + diameter / 2,
					   e.getV1().getY() + diameter / 2,
					   e.getV2().getX() + diameter / 2,
					   e.getV2().getY() + diameter / 2);
		}
	}
	
	
	/**
	 * Used by other classes to change what is displayed
	 * Can only be used to replace items, cannot be used to add new items
	 * Returns an array of two objects, the modified array of vertices and
	 * the modified array of edges
	 * @return 
	 */
	public void changeVertex(Vertex oldVertex, Vertex newVertex, GraphixFunctions caller) {
		for (int i = 0; i < vertexArr.length; i++) {
			if (vertexArr[i] == oldVertex)
				vertexArr[i] = newVertex;
		}
		
		//caller.updateEdgeList(changeEdges(oldVertex, newVertex));
		//caller.updateVertexList(vertexArr);
	}
	
	
	/**
	 * Change edges
	 */
	Edge[] changeEdges(Vertex oldVertex, Vertex newVertex) {
		for (int i = 0; i < edgeArr.length; i++) {
			// Replace the old vertex, wherever it appears,
			// with the new one
			if (edgeArr[i].getV1().getName() == oldVertex.getName())
				edgeArr[i] = new Edge(newVertex, edgeArr[i].getV2());
			else if (edgeArr[i].getV2().getName() == oldVertex.getName())
				edgeArr[i] = new Edge(edgeArr[i].getV1(), newVertex);
		}
		
		return edgeArr;
	}
	
	
	/**
	 * Accepts new arrays of vertices and edges, then redraws
	 */
	public void setArrays(Vertex[] vArr, Edge[] eArr) {
		vertexArr = vArr;
		edgeArr = eArr;
		
		this.revalidate();
		this.repaint();
	}

}
