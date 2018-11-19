import java.util.Arrays;
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.awt.image.BufferedImage;


import components.*;


public class Graphix
{
	//Hashmap of Hashmaps of vertex -> Doubles
	//Used for storing vertex -> vertex -> edge distance
	private HashMap<Vertex, HashMap<Vertex, Double> > graph =
			new  HashMap<Vertex, HashMap<Vertex, Double> >();
	
	//Hashmap used for checking connectedness of graph
	//Maps a vertex to its representative vertex
	private HashMap<Vertex, Vertex> parentSet;
	
	
	/*
	 * Main method
	 */
	public static void main(String[] args) {
		
		Graphix g2 = new Graphix();
		g2.readGraph("Graphix/2D Graphs/TestGraph.2dg");
		//System.out.println(g2);
		Vertex h = new Vertex (23, 23);
		g2.addVertex(h);
		g2.addEdge(new Vertex(100, 100), h);
		//System.out.println(g2);
		//g2.changeVertex(new Vertex(100, 100), 200, 200);
		System.out.println(g2);
		
	}
	
	
	/*
	 * Adds a new Vertex to the graph
	 */
	public void addVertex(Vertex v) {
		addVertex(v, true, false);
	}
	
	
	/*
	 * Adds a Vertex mapped to null to the graph
	 * Represents a vertex with no edges
	 * Use for dropping a vertex in and/or while building initial graph
	 */
	public void addVertexNoEdges(Vertex v) {
		addVertex(v, false, false);
	}
	
	
	/*
	 * Adds a Vertex to the graph
	 * edges is for signaling if the vertex has edges or not, defaults to true
	 * override is for signaling if the Vertex should be changed if 
	 * it's already in the graph, defaults to false
	 * Use as top of chain for adding a Vertex
	 */
	public void addVertex(Vertex v, boolean edges, boolean override) {
		//v already in, don't override
		if(override == false && graph.containsKey(v)) return; 
		//v has no edges, not in graph already
		else if(edges == false && !graph.containsKey(v)) {
			graph.put(v, null);
		}
		//v has edges, not in graph already
		else if(edges == true && !graph.containsKey(v)) {
			graph.put(v, new HashMap<Vertex, Double>() );
		}
		//v has no edges, set key
		else if(edges == false && override == true) {
			graph.put(v, null);
		}
		//v has edges, set key
		else if(edges == true && override == true) {
			graph.put(v, new HashMap<Vertex, Double>() );
		}
	}
	
	
	/*
	 * Changes the Vertex's coordinates
	 * Update's the edges in graph associated with that vertex
	 * Use for dragging Vertices around
	 */
	public void changeVertex(Vertex v, int x, int y) {
		//Keep a copy of the old one around
		Vertex temp = new Vertex(v.getX(), v.getY());
		//Change them coordinates
		v.changeCoords(x, y);
		//Keep those nullPointerExceptions at bay
		if(graph.containsKey(temp)) {
			//A lonely vertex...but also override any that might be where the new coords are
			if(graph.get(temp).equals(null)) {
				this.addVertex(v, false, true);
			}
			else {
				//Add vertex v, override what's there
				this.addVertex(v, true, true);
				//Loop through vertices incident to temp, add them to v
				//and remove temp from them
				for(Vertex w : graph.get(temp).keySet().toArray(new Vertex[0])) {
					this.changeVertexMapping(v, w);
					graph.get(w).remove(temp);
				}
			}
		}
		//Get rid of the mapping to the unchanged coordinates
		graph.remove(temp);
	}
	
	
	/*
	 * Given 2 vertices:
	 * Maps v to w
	 * Maps w to v
	 */
	public void changeVertexMapping(Vertex v, Vertex w) {
		double edge = edgeLength(v, w);
		graph.get(v).put(w, edge);
		graph.get(w).put(v, edge);
	}
	
	/*
	 * Adds an edge between two vertices
	 * If the vertices aren't in the graph already, it adds them
	 * Use for initial building of graph while reading a file in
	 */
	public void addEdge(Vertex v, Vertex w) {
		this.addVertex(v);
		this.addVertex(w);
		double dist = edgeLength(v, w);
		graph.get(v).put(w, dist);
		graph.get(w).put(v, dist);
	}
	
	
	/*
	 * Returns double of the length of the edge
	 * (distance between two vertices)
	 */
	public double edgeLength(Vertex v, Vertex w) {
		return distance(v.getX(), v.getY(), w.getX(), w.getY());
	}
	
	
	/*
	 * Returns double of the distance between two points
	 */
	public double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt( ((x1-x2)*(x1-x2)) + ((y1-y2)*(y1-y2)) );
	}
	
	
	/*
	 * Reads the graph from the file system
	 */
	public void readGraph(String file) {
		System.out.println("Reading graph: " + file);
		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader bfr = new BufferedReader(fr);
			bfr.lines().forEach(line -> parse(line.trim()));
			bfr.close();
		} catch (IOException e) {
		    System.err.format("IOException: %s\n", e);
		}
		System.out.println("Done reading file.");
	}
	
	
	/*
	 * Parses a String line of a graph file into the graph
	 */
	public void parse(String l) {
		String[] line = l.split(" ");
		int size = line.length;
		//If size < 2, then there's only one vertex on that line
		if(size < 2) {
			Vertex v = Vertex.stringToVertex(line[0].trim());
			this.addVertexNoEdges(v);
		} 
		//Two vertices, so it's time to add an edge
		else {
			String v1 = line[0].trim();
			String v2 = line[1].trim();
			Vertex v = Vertex.stringToVertex(v1);
			Vertex w = Vertex.stringToVertex(v2);
			this.addEdge(v, w);
		}
		
	}
	
	
	/*
	 * Returns a string representation of the graph:
	 * v1 : v2:3.2, v3:4, 
	 * v2 : v1:3.2, v5:8.98
	 */
	@Override
	public String toString() {
		String rv = "";
		//graph.keySet().toArray(new Vertex[0]);
		Vertex[] keys = orderedKeyArray();
		for(Vertex v : keys) {
			rv = rv + v + " : ";
			if(graph.get(v) != null) {
				Vertex[] innerKeys = graph.get(v).keySet().toArray(new Vertex[0]);
				for(Vertex w: innerKeys) {
					rv = rv + w + ":" + graph.get(v).get(w) + ", ";
				}
			}
			rv += "\n";
		}
		return rv;
	}
	
	
	/*
	 * Returns a Vertex array of keys to graph
	 * Ordered in this way: if x1 < x2, -> v1 then v2
	 * If x1 = x2, if y1 < y2 -> v1 then v2
	 */
	public Vertex[] orderedKeyArray() {
		Vertex[] keys = graph.keySet().toArray(new Vertex[0]);
		Arrays.sort(keys, (k, h) -> Vertex.compareVertices(k, h));
		return keys;
	}
	
	
}
