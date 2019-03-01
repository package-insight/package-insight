package io.github.packageinsight.analysis.graph.scc

/**
 * Based on https://www.geeksforgeeks.org/strongly-connected-components/
 *
 * Plan to replace with Tarjan.
 *
 * @param < T >  Node type
 */
class KosarajuSccGraph<T> {

    private final Map<T, Set<T>> adj = new HashMap<>() //Adjacency List

    //Function to add an edge into the graph
    void addEdge(T v, T w) {
        def ts = adj[v]
        if (ts == null) {
            ts = new HashSet<T>()
            adj.put(v, ts)
        }
        ts.add(w)
    }

    // A recursive function to print DFS starting from v
    void dfsUtil(T v, Set<T> visited) {
        // Mark the current node as visited
        visited.add(v)
        System.out.print(v + " ")

        // Recur for all the vertices adjacent to this vertex
        adj.get(v)?.each { n ->
            if (!visited.contains(n))
                dfsUtil(n, visited)
        }
    }

    // Function that returns reverse (or transpose) of this graph
    KosarajuSccGraph transpose() {
        KosarajuSccGraph g = new KosarajuSccGraph()
        adj.each {
            outer ->
                outer.value.each { inner ->
                    g.addEdge(inner, outer.key)
                }
        }
        return g
    }

    // A recursive DFS function to collect nodes starting at v
    void dfs(T v, Set<T> visited, List<T> collection) {
        // Mark the current node as visited and print it
        visited.add(v)

        // Recur for all the vertices adjacent to this vertex
        adj.get(v)?.each { n ->
            if (!visited.contains(n))
                dfs(n, visited, collection)
        }

        // All vertices reachable from v are processed by now,
        // push v to Stack
        collection.add(v)
    }

    // The main function that finds and prints all strongly
    // connected components
    List<Set<T>> getSccs() {
        def stack = new Stack<T>()

        // Mark all the vertices as not visited (For first DFS)
        def visited = new HashSet<T>()

        // Fill vertices in stack according to their finishing
        // times
        adj.keySet().each {
            if (!visited.contains(it))
                dfs(it, visited, stack)
        }

        // Create a reversed graph
        KosarajuSccGraph gr = transpose()

        // Mark all the vertices as not visited (For second DFS)
        visited = new HashSet<T>()

        def result = new LinkedList<Set<T>>()

        // Now process all vertices in order defined by Stack
        while (!stack.isEmpty()) {
            // Pop a vertex from stack
            T v = stack.pop()

            // Print Strongly connected component of the popped vertex
            if (!visited.contains(v)) {
                def output = new LinkedList<T>()
                gr.dfs(v, visited, output)
                result.add(output.toSet())
            }
        }

        result
    }
}
