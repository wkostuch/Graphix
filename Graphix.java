import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import components.*;


public class Graphix
{
	//Hashmap of Hashmaps of vertex -> Doubles
	//Used for storing vertex -> vertex -> edge distance
	private HashMap<Vertex, HashMap<Vertex, Double> > graph =
			new  HashMap<Vertex, HashMap<Vertex, Double> >();
	
	//Hashmap used for checking connectedness of graph
	//Maps a vertex to its representative vertex
	private HashMap<Vertex, Vertex> parentSet = new HashMap<Vertex, Vertex>();
	
	
	/*
	 * Main method
	 */
	public static void main(String[] args) {
		Graphix g = new Graphix();
		g.addVertex(new Vertex(1, 2));
		System.out.println(g.toString());
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
	 */
	public void addVertexNoEdges(Vertex v) {
		addVertex(v, false, false);
	}
	
	
	/*
	 * Adds a Vertex to the graph, top of chain
	 * edges is for signaling if the vertex has edges or not, defaults to true
	 * override is for signaling if the Vertex should be changed if 
	 * it's already in the graph, defaults to false
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
	 * Adds an edge between two vertices
	 * If the vertices aren't in the graph already, it adds them
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
		
	}
	
	
	/*
	 * Returns a string representation of the graph:
	 * v1 : v2:3.2, v3:4, 
	 * v2 : v1:3.2, v5:8.98
	 */
	public String toString() {
		String rv = "";
		Vertex[] keys = graph.keySet().toArray(new Vertex[0]);
		for(Vertex v : keys) {
			rv = rv + v + " : ";
			Vertex[] innerKeys = graph.get(v).keySet().toArray(new Vertex[0]);
			for(Vertex w: innerKeys) {
				rv = rv + w + ":" + graph.get(v).get(w) + ", ";
			}
			rv += "\n";
		}
		return rv;
	}
	
	
	
	
}
