package backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import frontend.GraphixTextOutput;
import frontend.GraphixVisuals;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;

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
	
	// Used in adding vertices, particularly deciding whether to chain together user clicks when adding
	// vertices.  That way a user can quickly add a series of connected vertices with a quick series
	// of clicks.  A double-click ends the chain.
	private Boolean activeChain;
	
	// This field is used to save a selected Vertex between clicks to allow Vertex editing
	private Vertex savedVertex;
	
	
	/*
	 * Main method
	 */
	public static void main(String[] args) {
		/*
		GraphixTextOutput gto = new GraphixTextOutput();
		Graphix g2 = new Graphix(gto);
		g2.readGraph("Graphix/src/graphs/diamond.2dg");
		System.out.println(g2.toString());
		g2.connect();
		System.out.println(g2.toString());
		g2.writeGraph("ConnectedDiamond");
		*/
		

		GraphixVisuals.start(args);
	}
	
	/*
	 * Constructor that takes a graphixTextOutput object as an argument
	 */
	public Graphix(GraphixTextOutput gto) {
		this.textOutput = gto;
		activeChain = false;
		savedVertex = null;
	}
	
	
	/*
	 * Creates a test graph with a 400x400 block of Vertices
	 * Deprecated
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
     * Connects every possible edge in the graph
     */
    public void complete() {
    	Vertex[] keys = graph.keySet().toArray(new Vertex[0]);
    	int count = 0;
    	int length = keys.length;
    	
    	while(count < length) {
    		//Prevents us from adding every edge twice
    		for(int i = count + 1; i < length; i++) {
    			//Prevents us from overriding an already existing edge
    			if(this.hasEdge(keys[count], keys[i])) continue;
    			this.addEdge(keys[count], keys[i]);
    		}
    		count++;
    	}
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
	 * Makes the given Vertex the apex Vertex of the graph
	 * (AKA: Connects that Vertex to every other one)
	 */
	public void makeApex(Vertex v) {
		Vertex[] keys = this.orderedKeyArray();
		for(Vertex k : keys) {
			if(!v.equals(k)) this.addEdge(v, k);
		}
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
	 * Returns the Vertex with the name if it exists
	 */
	public Vertex getVertex(String name) {
		Vertex[] va = this.orderedKeyArray();
		for(Vertex v : va) {
			if(v.getName().equals(name)) return v;
		}
		return null;
	}
	
	
	/*
	 * Generates a sequential name for the next Vertex.
	 * Vertex names take the form "v1, v2,..., vn" where n is the number of vertices in the graph.
	 */
	private String getNextName() {
		// Get the number of vertices already in the graph
		int numVertices = orderedKeyArray().length;
		
		// Make a string representation of the name
		// The count will start at 1 for ease of use
		return "v" + (Integer.toString(numVertices + 1));
	}
	
	
	/*
	 * Adds new Vertex with automated name generation.
	 * Accepts coordinates, creates a Vertex, generates name sequentially.
	 * Returns newly created Vertex in case it is needed in edge creation
	 */
	public Vertex addVertex(int x, int y) {
		Vertex newVertex = new Vertex(getNextName(), x, y);
		addVertex(newVertex);
		return newVertex;
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
	 * Returns the most recently added vertex
	 * Used for chaining user creation of vertices
	 */
	public Vertex getLastVertex() {
		// Make a string that will match the name of the last added vertex
		String lastVertexName = "v" + Integer.toString(orderedKeyArray().length - 1);
		
		// Return the Vertex
		return getVertex(lastVertexName);
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
	 * Saves a Vertex between clicks so it can be moved
	 */
	public void saveVertex(Vertex v) {
		savedVertex = v;
	}
	
	
	/*
	 * Returns the saved vertex, and sets the savedVertex field back to null
	 */
	public Vertex getSavedVertex() {
		Vertex returnVertex = savedVertex;
		savedVertex = null;
		return returnVertex;
	}
	
	
	/*
	 * Returns true if there is a saved Vertex
	 */
	public boolean isSavedVertex() {
		if (savedVertex == null)
			return false;
		
		return true;
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
	 * Checks to see if there's already an edge between two Vertex objects
	 * Returns true if there is, false if not
	 */
	public boolean hasEdge(Vertex v, Vertex w) {
		return graph.get(v).containsKey(w);
	}
	
	/*
	 * Changes the weight of the edge
	 */
	public void changeEdgeWeight(Edge e, String w) {
		Vertex v1 = e.getV1();
		Vertex v2 = e.getV2();
		double weight = e.convertWeight(w);
		graph.get(v1).put(v2, weight);
		graph.get(v2).put(v1, weight);
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
	 * Writes the graph to the file system
	 * Takes a string and names the file that under the graphs folder in
	 * Graphix/src
	 */
	public void writeGraph(String name) {
		//Creates a file with the name in the graphs folder
		String path = System.getProperty("user.dir");
		//Bad workaround for file issues
		if(!path.contains("Graphix")) {
			path += "\\Graphix";
		}
		File file = new File(path + "\\src\\graphs\\" + name + ".2dg");
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		Vertex[] keys = this.orderedKeyArray();
		Edge[] edges = this.orderedEdgeArray();
		//Make an ArrayList to keep track of which Vertices we see from edges
		ArrayList<Vertex> vFromE = new ArrayList<Vertex>();
		
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			
			//Write checking the edges
			for(Edge e : edges) {
				String s = "";
				Vertex v1 = e.getV1();
				Vertex v2 = e.getV2();
				double w = e.getWeight();
				//Add Vertices to keep track of them if it's the first time seeing 'em
				if(!vFromE.contains(v1)) vFromE.add(v1);
				if(!vFromE.contains(v2)) vFromE.add(v2);
				//Change the string and write it
				s += v1.getName() + "-" + v1.getX() + "," + v1.getY()
					+ " : " 
					+ v2.getName() + "-" + v2.getX() + "," + v2.getY()
					+ " :: "
					+ w;
				bw.write(s);
				bw.newLine();
			}
			
			//Now check for Vertices not in edges
			for(Vertex v : keys) {
				String s = "";
				//Vertex we haven't seen from an edge
				if(!vFromE.contains(v)) {
					//Change string and write it
					s += v.getName() + "-" + v.getX() + "," + v.getY();
					bw.write(s);
					bw.newLine();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//Close stuff up
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		//Make the text box visible and say when the file is done writing
		textOutput.setVisible(true);
		textOutput.output("Done writing file: " + name + "\n");
	}
	
	
	/*
	 * Reads the graph from the file system
	 */
	public void readGraph(String file) {
		this.textOutput.setVisible(true);
		//Runs if the "cancel" button is hit: no graph file selected to read
		if(file == null) {
			this.textOutput.output("No file selected.\n");
			return;
		}
		//Ensures that the file is a .2dg type
		if(!file.endsWith(".2dg")) {
			this.textOutput.output("Please select a valid file ending with \".2dg\" \n ");
			return;
		}
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
			this.addVertex(v);
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
    
    
    /*
     * Returns the value of activeChain field
     */
    public Boolean getActiveChain() {
    	return activeChain;
    }
    
    
    /*
     * Sets the value of activeChain field
     */
    public void setActiveChain(boolean val) {
    	activeChain = val;
    }
	
	
}
