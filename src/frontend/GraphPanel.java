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

}
