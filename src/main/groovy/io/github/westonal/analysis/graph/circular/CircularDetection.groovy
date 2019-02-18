package io.github.westonal.analysis.graph.circular

import io.github.westonal.analysis.graph.Edge
import io.github.westonal.analysis.graph.Graph

class CircularDetection<T> {
    private Graph<T> graph
    private int limit
    private List<Edge<T>> edges
    private Map<T, List<Edge<T>>> edgeMap
    private Comparator<T> comparator

    CircularDetection(Graph<T> graph, int limit, Comparator<T> comparator) {
        this.comparator = comparator
        this.graph = graph
        this.limit = limit
        edges = reduceEdgeSet(graph) as List
        edgeMap = edges.groupBy { it.from }
    }

    Collection<CircularReference<T>> findCircular() {
        def references = new LinkedHashSet<CircularReference<T>>()
        edges*.from.each { startPoint ->
            travelAllFrom(references, startPoint, [startPoint] as Set, [])
        }
        references as List
    }

    def travelAllFrom(Set<CircularReference<T>> references, T from, Set<T> visitedEdges, Collection<Edge<T>> path) {
        if (references.size() >= limit) return
        visitedEdges.add(from)
        edgeMap[from].each { edge ->
            if (visitedEdges.contains(edge.to)) {
                if (path[0]?.from == edge.to) {
                    references.add(new CircularReference<T>(edges: order(path + [edge])))
                }
            } else {
                travelAllFrom(references, edge.to, visitedEdges, path + [edge])
            }
        }
    }

    private List<Edge<T>> order(Collection<Edge<T>> edges) {
        final int size = edges.size()
        int minEdgeIdx = 0
        for (int i = 1; i < size; i++) {
            if (comparator.compare(edges[i].from, edges[minEdgeIdx].from) < 0) {
                minEdgeIdx = i
            }
        }
        if (minEdgeIdx != 0) {
            edges.drop(minEdgeIdx) + edges.take(minEdgeIdx)
        } else {
            edges
        }
    }

    private Set<Edge<T>> reduceEdgeSet(Graph<T> graph) {
        def edges = graph.edges
        while (true) {
            def originalSize = edges.size()
            edges = removeDeadEnds(edges)
            edges = removeUnreachable(edges)
            if (edges.size() == originalSize) break
        }
        edges
    }

    private Set<Edge<T>> removeDeadEnds(Set<Edge<T>> edges) {
        def sources = edges*.from as Set
        edges.findAll { it.to in sources }
    }

    private Set<Edge<T>> removeUnreachable(Set<Edge<T>> edges) {
        def targets = edges*.to as Set
        edges.findAll { it.from in targets }
    }
}
