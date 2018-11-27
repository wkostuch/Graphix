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
	
	public Vertex getV1() {
		return this.v1;
	}
	
	public Vertex getV2() {
		return this.v2;
	}
	
	public static double distance(Vertex v, Vertex w) {
		return distance(v.getX(), v.getY(), w.getX(), w.getY());
	}
	
	private static double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt( ((x1-x2)*(x1-x2)) + ((y1-y2)*(y1-y2)) );
	}
	
	
}
