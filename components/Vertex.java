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
	 * Returns a Vertex from a String of the form x,y
	 * where x and y are some integers
	 */
	public static Vertex stringToVertex(String s) {
		String[] sa = s.split(",");
		int x = Integer.valueOf(sa[0]);
		int y = Integer.valueOf(sa[1]);
		Vertex v = new Vertex(x, y);
		return v;
	}
	
	
	/*
	 * Returns true if vertices have same coordinates
	 *    ''   false if they have different coordinates
	
	public boolean sameVertex(Vertex v) {

	}
	 */
	
	/*
	 * Override of equals method
	 */
	@Override
	public boolean equals(Object o) {
		if(o == null || o.getClass() != Vertex.class) return false;
		Vertex v = (Vertex) o;
		if(this.getX() == v.getX() && this.getY() == v.getY()) return true;
		else return false;
	}
	
	/*
	 * Override of hashCode method
	 */
	@Override
	public int hashCode() {
		int prime = 67619;
		char[] sx = String.valueOf(this.getX()).toCharArray();
		int h = 0;
		for(char c : sx) {
			h = (h + (int)c) % prime;
		}
		char[] sy = String.valueOf(this.getY()).toCharArray();
		for(char c : sy) {
			h = (h + (int)c) % prime;
		}
		return h;
	}
		
	
	/*
	 * Returns the x value of the vertex
	 */
	public int getX() {
		return this.x;
	}
	
	
	/*
	 * Returns the y value of the vertex
	 */
	public int getY() {
		return this.y;
	}
	
	
	/*
	 * Changes x value of the vertex
	 */
	public void changeX(int n) {
		this.x = n;
	}
	
	
	/*
	 * Changes y value of the vertex
	 */
	public void changeY(int n) {
		this.y = n;
	}
	
	
	/*
	 * Changes x and y values of the vertex
	 */
	public void changeCoords(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "(" + this.x + ", " + this.y + ")";
		return s;
	}
}
