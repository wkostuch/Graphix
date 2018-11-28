package src.graphixBackend;

/*
 * Used for drawing edges, MWST, stuff that requires edges and not just distances
 */
public class Edge
{
	private Vertex v1;
	private Vertex v2;
	private double weight;
	
	public Edge(Vertex v1, Vertex v2) {
		this.v1 = v1;
		this.v2 = v2;
		weight = distance(v1, v2);
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
