/* CPCS324 Project Part 1

 Group members: 
    1- Razan Alamri
    2- Khloud Alsofyani
    3- Leen Ba Galaql
    4- Shatha Binmahfouz
 */
package GraphFramework;

import java.io.*;
import java.util.*;

// Graph is a class defines the structure of a graph
public abstract class Graph {

    // Data filed

    // Number of vertices
    int verticesNo;
    // Number of edges
    int edgeNo;
    // Check if graph is directed or not
    boolean isDigraph;
    // Map to can store and found vertex by label (from association relationship)
    public Map<String, Vertex> vertices;

    // ----------------------------------------------------------check
    // Map to can found Edge by source
    // Map<String, Edge> edgeMap;

    // Contructors
    public Graph() {
        this.verticesNo = 0;
        this.edgeNo = 0;
        this.isDigraph = false;
        this.vertices = new HashMap<>();
    }

    public Graph(int verticesNo, int edgeNo, boolean isDigraph) {
        this.verticesNo = verticesNo;
        this.edgeNo = edgeNo;
        this.isDigraph = isDigraph;
        this.vertices = new HashMap<>(verticesNo);
    }

    // Abstract method to create object of Vertex
    public abstract Vertex creatVertex(String lable);

    // Abstract method to create object of Edge
    public abstract Edge creatEdge(Vertex v, Vertex u, int w);

    /*
     * Methos responsible for creating a graph object with the specified parameters
     * and randomly initializes the vertices’ labels,
     * creating edges that connects the created vertices randomly and
     * assigning them random weights.
     */
    public Graph makeGraph(int n, int m) {
        // Create n vertices with random labels
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            int labelNum = rand.nextInt(n);
            String label = "O" + labelNum;
            Vertex v = creatVertex(label);
            while (vertices.containsKey(label)) {
                labelNum = rand.nextInt(n);
                label = "O" + labelNum;
                v = creatVertex(label);
            }
            vertices.put(label, v);
        }

        // Create m edges randomly between vertices
        HashSet<String> edgeSet = new HashSet<>();
        for (int i = 0; i < m; i++) {
            // Choose two random vertices
            List<String> labels = new ArrayList<>(vertices.keySet());
            String label1 = labels.get(rand.nextInt(labels.size()));
            String label2 = labels.get(rand.nextInt(labels.size()));
            while (label1.equals(label2) || edgeSet.contains(label1 + ":" + label2)) {
                label1 = labels.get(rand.nextInt(labels.size()));
                label2 = labels.get(rand.nextInt(labels.size()));
            }

            // Create edge with random weight
            Vertex v = vertices.get(label1);
            Vertex u = vertices.get(label2);
            int weight = rand.nextInt(100) + 1; // Random weight between 1 and 100
            addEdge(v, u, weight);
            edgeSet.add(label1 + ":" + label2); // Add edge to set to prevent duplicates
        }

        // Return the current Graph object
        return this;
    }

    /*
     * Method to reads the edges and vertices from the text file
     * whose name is inputFile
     */
    public Graph readGraphFromFile(File fileName) throws FileNotFoundException {

        // Read graph from file
        Scanner inpScanner = new Scanner(fileName);

        // Read header information
        String graphname = inpScanner.next();
        int is_Digraph = inpScanner.nextInt();
        /*
         * Read the grapg type if input 0 means it is an undirected graph
         * if input 1 means it is an directed graph
         */
        if (is_Digraph == 1) {
            this.isDigraph = true;
        } else if (is_Digraph == 0) {
            this.isDigraph = false;
        }
        this.verticesNo = inpScanner.nextInt();
        this.edgeNo = inpScanner.nextInt();

        // Create vertices and edges
        while (inpScanner.hasNext()) {
            // Read source, destination, and weight
            String sLabel = inpScanner.next();
            String dLabel = inpScanner.next();
            int weight = inpScanner.nextInt();

            // Get source vertex
            Vertex s = creatVertex(sLabel);
            vertices.put(sLabel, s);
            // Get destination vertex
            Vertex d = creatVertex(dLabel);
            vertices.put(dLabel, d);
            addEdge(s, d, weight);
        }

        // Close scanner
        inpScanner.close();
        // Return graph
        return this;
    }

    /*
     * Method that creates an edge object and passes source v vertex, target u
     * vertex
     * and weight as parameters, assigns the target vertex to the adjacent list
     * of the source vertex
     */
    public void addEdge(Vertex v, Vertex u, int w) {
        // Create edge
        Edge edge = creatEdge(v, u, w);

        // Add edge to the source adjacent list
        v.adjList.addFirst(edge);

        // Check if it is an undirected graph
        if (!isDigraph) {
            // Create reverse edge and add it to the target adjacent list
            Edge revEdge = creatEdge(u, v, w);
            u.adjList.addFirst(revEdge);
            ;
        }
        /*
         * Increment the edge count by 1 If it is a undirected graph
         * and by 2 if directed graph
         */
        edgeNo += isDigraph ? 1 : 2;
    }
}
