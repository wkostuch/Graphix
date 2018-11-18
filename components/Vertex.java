package components;

import java.util.Random;


public class Vertex
{
	private int x;
	private int y;
	
	
	//Constructor
	public Vertex(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/*
	 * Returns a Vertex with x and y in the range 0 <= x, y <= 1000
	 */
	public static Vertex randomVertex() {
		Vertex v = randomVertex(1000);
		return v;
	}
	
	/*
	 * Returns a Vertex with x and y within the specified range (inclusive
	 */
	public static  Vertex randomVertex(int range) {
		Random r = new Random();
		int randX = r.nextInt(range + 1);
		int randY = r.nextInt(range + 1);
		Vertex v = new Vertex(randX, randY);
		return v;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s = "(" + this.x + ", " + this.y + ")";
		return s;
	}
}
