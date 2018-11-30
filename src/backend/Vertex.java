package backend;

import java.util.Objects;
import java.util.Random;


public class Vertex
{
	private String name;
	private int x;
	private int y;
	
	
	//Constructor
	public Vertex(String n, int x, int y) {
		this.name = n;
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
		String s = "V-" + randX + "," + randY;
		Vertex v = new Vertex(s,randX, randY);
		return v;
	}
	
	
	/*
	 * Returns a Vertex from a String of the form x,y
	 * where x and y are some integers
	 */
	public static Vertex stringToVertex(String s) {
		//Split along "-" to find name
		String[] sa1 = s.split("-");
		String name = sa1[0];

		//Split along "," after initial split to find x,y coords
		String[] sa2 = sa1[1].split(",");
		int x = Integer.valueOf(sa2[0]);
		int y = Integer.valueOf(sa2[1]);
		Vertex v = new Vertex(name ,x, y);
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
		return Objects.hash(this.x, this.y);
	}
	
	
	/*
	 * Used in Arrays.sort() for sorting vertices
	 * Returns a negative number if a precedes b
	 * Returns 0 if a.x == b.x && a.y == b.y
	 * Returns a positive number if b precedes a
	 * Precedence is determined by: a.x < b.x, 
	 * then by a.y < b.y if a.x == b.x
	 */
	static int compareVertices(Vertex a, Vertex b) {
		int rv = 0;
		//a comes first!
		if(a.getX() < b.getX()) {
			rv = -1; 
		} 
		//b comes first!
		else if (a.getX() > b.getX()) {
			rv = 1;
		} 
		//The x values are equal, time to check y values!
		else {
			//a comes first!
			if(a.getY() < b.getY()) {
				rv = -1;
			} 
			//b comes first!
			else if (a.getY() > b.getY()) {
				rv = 1;
			}
			//Both x and y are equal for a and b
			else rv = 0;
		}
		
		return rv;
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
	 * Returns the name of the vertex
	 */
	public String getName() {
		return this.name;
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
		this.changeX(x);
		this.changeY(y);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = this.getName() + "(" + this.x + ", " + this.y + ")";
		return s;
	}
}
