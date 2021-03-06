package backend;

import java.awt.Dimension;

/*
 * Used for drawing edges, MWST, stuff that requires edges and not just distances
 */
public class Edge
{
	private Vertex v1;
	private Vertex v2;
	private double weight;
	
	/*
	 * Default constructor that gives a weight equal to
	 * the distance between v1 and v2
	 */
	public Edge(Vertex v1, Vertex v2) {
		this(v1, v2, String.valueOf(distance(v1, v2)));
	}
	
	/*
	 * Constructor that assigns weight equal to number value of s
	 * s must be either "d" or a number
	 */
	public Edge(Vertex v1, Vertex v2, String s) {
		this.v1 = v1;
		this.v2 = v2;
		if(s == "d") s = String.valueOf(distance(v1, v2));
		double w = Double.valueOf(s);
		this.weight = w;
	}
	
	/*
	 * Changes the weight of an edge 
	 */
	public void changeWeight(String s) {
		this.weight = this.convertWeight(s);
	}
	
	/*
	 * Converts the string to the weight
	 */
	public double convertWeight(String s) {
		double w = 0;
		//Weight should be distance
		if(s.equals("d") || s.equals("dist") || s.equals("distance")) {
			w = distance(this.getV1(), this.getV2());
		} else {
			try {
				w = Double.parseDouble(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return w;
	}
	
	/*
	 * Overrides the toString method
	 */
	@Override
	public String toString() {
		String s = "";
		s += "(" + this.getV1() + " ~ " + this.getWeight() + 
				" ~ " + this.getV2() + ")";
		return s;
	}
	
	
	/*
	 * Returns the first Vertex of the Edge
	 */
	public Vertex getV1() {
		return this.v1;
	}
	
	/*
	 * Returns the second Vertex of the Edge
	 */
	public Vertex getV2() {
		return this.v2;
	}
	
	/*
	 * Returns the weight of the Edge
	 * I.E., the distance between v1 and v2
	 */
	public double getWeight() {
		return this.weight;
	}
	
	/*
	 * Returns a Dimension with the midpoint of the edge
	 */
	public Dimension getMidpoint() {
		Vertex v1 = this.getV1();
		Vertex v2 = this.getV2();
		int mx = (v1.getX() + v2.getX()) / 2;
		int my = (v1.getY() + v2.getY()) / 2;
		Dimension midpoint = new Dimension(mx, my);
		return midpoint;
	}
	
	/*
	 * Computes distance between two Vertices
	 */
	public static double distance(Vertex v, Vertex w) {
		return distance(v.getX(), v.getY(), w.getX(), w.getY());
	}
	
	/*
	 * Computes distance between two points
	 */
	private static double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt( ((x1-x2)*(x1-x2)) + ((y1-y2)*(y1-y2)) );
	}
	
	
}
