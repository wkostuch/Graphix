package backend;

import java.util.Arrays;
import java.util.HashMap;

import frontend.GraphixTextOutput;
import frontend.GraphixVisuals;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.BufferedWriter;

import java.awt.Dimension;



public class Graphix
{
	//Hashmap of Hashmaps of vertex -> Doubles
	//Used for storing vertex -> vertex -> edge distance
	public HashMap<Vertex, HashMap<Vertex, Double> > graph =
			new  HashMap<Vertex, HashMap<Vertex, Double> >();
	
	//Hashmap used for checking connectedness of graph
	//Maps a vertex to its representative vertex
	private HashMap<Vertex, Vertex> parentSet;
	
	//Text output box used in several methods,
	//handed to constructor by frontend stuff
	GraphixTextOutput textOutput;
	
	private static Dimension windowSize;
	
	
	/*
	 * Main method
	 */
	public static void main(String[] args) {
		/*
		GraphixTextOutput gto = new GraphixTextOutput();
		Graphix g2 = new Graphix(gto);
		g2.readGraph("Graphix/src/graphs/diamond.2dg");
		System.out.println(g2.numberOfVertices());
		System.out.println(g2);
		g2.changeVertex(g2.getVertex(50, 150), 600, 600);

		System.out.println(g2);

		g2.removeEdge(g2.getVertex(600, 600), g2.getVertex(150, 150));
		g2.removeVertex(g2.getVertex(600, 600));
		System.out.println("Here: " + "\n" +g2);
		Edge[] ea = g2.orderedEdgeArray();
		for(int i = 0; i < ea.length; i++) {
			System.out.println(ea[i]);
			g2.removeEdge(ea[i]);
		}
		
		
		System.out.println(g2);
		g2.isTree();
		g2.MWST().isTree();
		
		*/
		/*System.out.println(g2);
		Graphix g2MWSP = g2.MWST();
		System.out.println("MWSP:");
		System.out.println(g2MWSP);
		Vertex h = new Vertex ("h", 23, 23);
		g2.addVertex(h);
		g2.addEdge(g2.getVertex(100, 100), h, 20);
		//System.out.println(g2.graph.get(g2.getVertex(100, 100)));
		g2.changeVertex(g2.getVertex(100, 100), 200, 200);
		System.out.println(g2);
		System.out.println(g2.numberOfEdges());
		Edge[] e = g2.orderedEdgeArray();
		*/

		
		//Edge e2 = new Edge(h, new Vertex(25, 57), "200");
		//System.out.println(e2);
		/*g2.changeVertex(h, 500, 500);
		System.out.println(g2);
		
		Graphix MWSP = g2.MWST();
		System.out.println(MWSP);
		*/
		//Graphix g = new Graphix();
		//g.testGraph();
		//System.out.println(g);

		GraphixVisuals.start(args);
	}
	
	/*
	 * Constructor that takes a graphixTextOutput object as an argument
	 */
	public Graphix(GraphixTextOutput gto) {
		this.textOutput = gto;
	}
	
	
	/*
	 * Creates a test graph with a 400x400 block of Vertices
	 */
	public void testGraph() {
		this.readGraph("Graphix/src/graphs/diamond.2dg");
		for(int i = 0; i <= 400; i++) {
			for(int j = 0; j <= 400; j++) {
				Vertex v = new Vertex("",i, j);
				this.addVertex(v);
			}
		}
	}
	
    /**
     * Return a graph containing a minimum-weight spanning tree of this graph.
     */
    public Graphix MWST(){
		Graphix MWSP = new Graphix(this.textOutput); //Empty graph to be build into MWSP
		int V = this.numberOfVertices(); //Number of vertices of the graph
		parentSet = this.disjointSet(); //Update field
		//Hashmap for points and their parents (null at this point)
		
		//Array for edges, sorted in increasing order
		Edge[] sortedEdges = this.orderedEdgeArray();
		int edges = 0; //edges in MWSP, used for breaking loop
		//Loop through, adding edges while checking if it's a tree yet
		for(int i = 0; i < sortedEdges.length; i++) {
			Edge e = sortedEdges[i];
			//can't add edge because it'd create a cycle
			if(cycle(e.getV1(), e.getV2())) continue;
			double weight = e.getWeight();
			//Add vertices and edge with weight
			MWSP.addEdge(e.getV1(), e.getV2(), weight);
			//Combines the sets as they're now connected by an edge
			combineSets(e.getV1(), e.getV2()); 
			edges++;
			if(edges == V - 1) break;
		}
		
		return MWSP;
    }
    
    

    /*
     * Prints a String to the GraphixTextOutput 
     * which says if the Graphix object is a tree or not
     */
    public void isTree() {
    	this.textOutput.setVisible(true);
    	int numEdges = this.numberOfEdges();
    	int numVertices = this.numberOfVertices();
    	//If e doesn't equal v - 1, not a tree
    	if(numEdges != numVertices - 1) {
    		textOutput.output("This is not a tree!\n");
    		return;
    	}
    	//If there's a cycle, not a tree;
    	//if(this.hasCycle() == true) return false;
    	//If it's not connected, not a true
    	if(this.isConnected() == false) {
    		textOutput.output("This is not a tree!\n");
    		return;
    	}
    	//If e = v-1 and no cycles, then it's a tree!
    	else {
    		textOutput.output("This is a tree!\n");
    		return;
    	}
    }
    
    /*
     * Returns true if the graph is connected, false if not
     */
    public boolean isConnected() {
    	//Connected until proven guilty of not being connected
    	boolean flag = true;
		//Hashmap for points and their parents (null at this point)
		parentSet = this.disjointSet(); //Update field
		//Array for edges, sorted in increasing order
		Edge[] sortedEdges = this.orderedEdgeArray();
		//Loop through edges checking if they're connected or not
		for(int i = 0; i < sortedEdges.length; i++) {
			Edge e = sortedEdges[i];
			//can't add edge because it'd create a cycle
			if(cycle(e.getV1(), e.getV2())) {
				flag = false;
				break;
			}
			//Combines the sets as they're now connected by an edge
			combineSets(e.getV1(), e.getV2()); 
		}
		return flag;
    }
    
    
    
    /*
     * Combines sets by updating their representatives
     */
    public void combineSets(Vertex v1, Vertex v2){
    	Vertex Rv1 = findRep(v1);
    	Vertex Rv2 = findRep(v2);
    	parentSet.put(Rv1, Rv2);
    }
    
    
    /*
     * Returns vertex which is representative of the vertex given
     */
    public Vertex findRep(Vertex v) {
    	if(parentSet.get(v) == v) return v;
    	else return findRep(parentSet.get(v));
    }
    
    /*
     * Returns true if a cycle is found for vertices v and w (same rep)
     * Returns false if there isn't a cycle (different rep)
     */
    public boolean cycle(Vertex v, Vertex w) {
    	return findRep(v) == findRep(w);
    }
    
    
    /*
     * Returns a disjoint set of the graph
     * (DSHashMap where each vertex is its own rep)
     */
    public HashMap<Vertex, Vertex> disjointSet(){
    	HashMap<Vertex, Vertex> parentSet = new HashMap<Vertex, Vertex>();
		Vertex[] keys = this.orderedKeyArray(); //List of keys to the graph
		
		for(int i = 0; i < keys.length; i++) {
			Vertex key = (Vertex) keys[i];
				parentSet.put(key, key);
				//Initialize each point as its own parent
		}
		return parentSet;
    }

	
	/*
	 * Allows GraphixVisuals object to give Graphix a window size
	 */
	public static void setWindowSize(Dimension winSize) {
		windowSize = winSize;
	}
	
	
	/*
	 * Returns the Vertex at x and y if it exists
	 */
	public Vertex getVertex(int x, int y) {
		Vertex[] va = this.orderedKeyArray();
		for(Vertex v : va) {
			if(v.getX() == x && v.getY() == y) return v;
		}
		return null;
	}
	
	
	/*
	 * Adds a new Vertex to the graph
	 */
	public void addVertex(Vertex v) {
		addVertex(v, true, true);
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
	 * Removes the Vertex from the graph and kills any edges associated with it
	 */
	public void removeVertex(Vertex v) {
		//If there's stuff attached to v, get rid of them
		if(graph.get(v) != null) {
			Vertex[] attachedVs = graph.get(v).keySet().toArray(new Vertex[0]);
			for(Vertex av : attachedVs) {
				this.removeEdge(av, v);
			}
		}
		graph.remove(v);
	}
	
	
	/*
	 * Changes the Vertex's coordinates
	 * Update's the edges in graph associated with that vertex
	 * Use for dragging Vertices around
	 * Default weight for the new edges is the distance between 
	 * the two Vertices
	 */
	public void changeVertex(Vertex v, int x, int y) {
		//Change them coordinates
		Vertex newV = new Vertex(v.getName(), x, y);
		//Keep those nullPointerExceptions at bay
		
		if(graph.containsKey(v)) {
			//A lonely vertex...but also override any that might be where the new coords are
			if(graph.get(v).equals(null)) {
				this.addVertex(newV, false, true);
			}
			else {
				//Add vertex v, override what's there
				this.addVertex(newV, true, true);
				//Loop through vertices incident to temp, add them to v
				//and remove temp from them
				for(Vertex w : graph.get(v).keySet().toArray(new Vertex[0])) {
					this.addEdge(newV, w);
					graph.get(w).remove(v);
				}
			}
		}
		//Get rid of the mapping to the unchanged coordinates
		graph.remove(v);
		
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
	 * Default weight is the distance between v and w
	 */
	public void addEdge(Vertex v, Vertex w) {
		double dist = edgeLength(v, w);
		this.addEdge(v, w, dist);
	}
	
	/*
	 * Adds an edge between two vertices
	 * If the vertices aren't in the graph already, it adds them
	 * Able to specify weight of the edge
	 */
	public void addEdge(Vertex v, Vertex w, double weight) {
		this.addVertex(v, true, false);
		this.addVertex(w, true, false);
		graph.get(v).put(w, weight);
		graph.get(w).put(v, weight);
	}
	
	/*
	 * Removes the edge from the graph
	 */
	public void removeEdge(Edge e) {
		Vertex v1 = e.getV1();
		Vertex v2 = e.getV2();
		graph.get(v1).remove(v2);
		graph.get(v2).remove(v1);
	}
	
	/*
	 * Removes edge between v1 and v2 from the graph
	 * Only use when you know there's an edge between v1 and v2!
	 */
	public void removeEdge(Vertex v1, Vertex v2) {
		graph.get(v1).remove(v2);
		graph.get(v2).remove(v1);
	}
	
	/*
	 * Returns double of the length of the edge
	 * (distance between two vertices)
	 */
	public double edgeLength(Vertex v, Vertex w) {
		return Edge.distance(v, w);
	}
	
	
	/*
	 * Reads the graph from the file system
	 */
	public void readGraph(String file) {
		this.textOutput.setVisible(true);
		textOutput.output("Reading graph: " + file + "\n");
		try {
			//Go line by line and parse it
			FileReader fr = new FileReader(file);
			BufferedReader bfr = new BufferedReader(fr);
			bfr.lines().forEach(line -> parse(line.trim()));
			bfr.close();
		} catch (IOException e) {
			textOutput.output("IOException: " + e.toString());
		    System.err.format("IOException: %s\n", e);
		}
		textOutput.output("Done reading file.\n");
	}
	
	
	/*
	 * Parses a String line of a graph file into the graph
	 */
	public void parse(String l) {
		//Split along "::" to see if there's a desired weight 
		String[] linePass1 = l.split("::");
		int initSize = linePass1.length;
		//Split along " : " to get each Vertex
		String[] line = linePass1[0].split(" : ");
		int size = line.length;
		//If size < 2, then there's only one vertex on that line
		if(size < 2) {
			Vertex v = Vertex.stringToVertex(line[0].trim());
			this.addVertexNoEdges(v);
			return;
		} 
		//Two vertices, so it's time to add an edge
		else {
			String v1 = line[0].trim();
			String v2 = line[1].trim();
			Vertex v = Vertex.stringToVertex(v1);
			Vertex w = Vertex.stringToVertex(v2);
			//If there is a desired weight, then note that
			double weight;
			if(initSize > 1) {
				weight = Double.parseDouble(linePass1[1]);
			} else {
				weight = edgeLength(v, w);
			}
			this.addEdge(v, w, weight);
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
					rv = rv + w + ":" + graph.get(v).get(w) + ",    ";
				}
			}
			rv += "\n";
		}
		return rv;
	}
	
	
	/*
	 * Returns a Vertex array of keys to the graph
	 * Ordered in this way: if x1 < x2, -> v1 then v2
	 * If x1 = x2, if y1 < y2 -> v1 then v2
	 */
	public Vertex[] orderedKeyArray() {
		Vertex[] keys = graph.keySet().toArray(new Vertex[0]);
		Arrays.sort(keys, (k, h) -> Vertex.compareVertices(k, h));
		return keys;
	}
	
	
	/*
	 * Returns an Edge array of edges from the graph
	 * Ordered by length of edge
	 */
    public Edge[] orderedEdgeArray() {
    	Edge[] sortedEdges = new Edge[this.numberOfEdges()];
    	
    	int spot = 0; //Where to put the edge
    	
    	//Loop through keys of outer map
    	for(Vertex key1 : this.orderedKeyArray()) {
    		//Check if the key1 Vertex is unconnected, if so: continue
    		if(graph.get(key1) == null) continue;
    		//Loop through inner map of keys
    		Vertex[] innerMapKeys = graph.get(key1).keySet().toArray(new Vertex[0]);
    		for(Vertex key2 : innerMapKeys) {
    			if( Vertex.compareVertices(key1, key2) < 0) { 
    				//key1 alphabetically prior to key2
    				String weight = graph.get(key1).get(key2).toString();
    				Edge e = new Edge(key1, key2, weight);
    				sortedEdges[spot] = e;
    				spot++;
    			}
    		}
    	}
        Arrays.sort(sortedEdges, (a, b) -> (int)(a.getWeight() - b.getWeight()));
    	return sortedEdges;
    }


    /*
     * Returns the number of edges in the graph
     */
	private int numberOfEdges()
	{
		int count = 0;
		//Array of Vertex keys to outer HashMap in graph
		Vertex[] k = graph.keySet().toArray(new Vertex[0]);
		//Loop through keys
		for(Vertex s : k){
			//Keep from getting null pointers if the Vertex isn't connected to anything
			if(graph.get(s) != null) {
				count += graph.get(s).keySet().toArray(new Vertex[0]).length;
			}
		}
		return count/2;
	}
	
	
    /*
     * Returns the number of vertices in the graph
     */
    public int numberOfVertices(){
    	return graph.keySet().size();
    }
	
	
}
