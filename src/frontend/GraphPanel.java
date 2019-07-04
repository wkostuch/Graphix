package frontend;

import java.awt.Graphics;

import javax.swing.JPanel;

import backend.*;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel {
	
	Vertex[] vertexArr;
	Edge[] edgeArr;
	private static int diameter = 10;
	
	
	/**
	 * constructor which accepts a vertexList and an edgeList
	 */
	public GraphPanel(Vertex[] v, Edge[] e) {
		vertexArr = v;
		edgeArr = e;
	}
	
	
	/**
	 * Override of paintComponent
	 * Draws vertices, edges, and labels from arrays
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		this.removeAll();
		
		// We have a check up here too just to be safe.
		if (vertexArr.length > 0) {
			for (Vertex v : vertexArr) {
				g.fillOval(v.getX(), v.getY(), diameter, diameter);
				g.drawString(v.getName(), v.getX() + diameter, v.getY());
			}
		}
		
		// The for loop was trying to access an empty edge array, which led to null pointer
		// exceptions.  Not fun.  So we have a check now.
		if (edgeArr.length > 0) {
			for (Edge e : edgeArr) {
				g.drawLine(e.getV1().getX() + diameter / 2,
						   e.getV1().getY() + diameter / 2,
						   e.getV2().getX() + diameter / 2,
						   e.getV2().getY() + diameter / 2);
				g.drawString(Integer.toString((int) e.getWeight()),
							 e.getMidpoint().width - (diameter / 2),
							 e.getMidpoint().height + diameter);
			}
		}
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
	
	
	/**
	 * Returns diameter of dots used for vertices
	 * Used in MouseListener used in vertex editing
	 */
	public int getDiameter() {
		return diameter;
	}

}
