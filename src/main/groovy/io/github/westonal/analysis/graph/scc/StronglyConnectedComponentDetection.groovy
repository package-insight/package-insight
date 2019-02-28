package io.github.westonal.analysis.graph.scc

import io.github.westonal.analysis.graph.Graph
import io.github.westonal.analysis.graph.KosarajuSccGraph

class StronglyConnectedComponentDetection<T> {
    private Graph<T> graph
    private Comparator<T> comparator

    StronglyConnectedComponentDetection(Graph<T> graph, Comparator<T> comparator) {
        this.comparator = comparator
        this.graph = graph
    }

    Collection<StronglyConnectedComponent<T>> findCircular() {
        def strongGraph = new KosarajuSccGraph<T>()
        graph.edges.each {
            strongGraph.addEdge(it.from, it.to)
        }
        strongGraph.sccs
                .toSorted(new SetComparator(comparator))
                .collect { nodes ->
            new StronglyConnectedComponent<T>(
                    nodes: nodes,
                    edges: graph.edges.findAll { edge ->
                        nodes.contains(edge.from) && nodes.contains(edge.to)
                    }
            )
        }
    }
}
