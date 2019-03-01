package io.github.packageinsight.analysis.graph

import io.github.packageinsight.analysis.graph.scc.KosarajuSccGraph
import io.github.packageinsight.general.SetComparator

class StronglyConnectedComponentDetection<T> {
    private Graph<T> graph
    private Comparator<T> comparator

    StronglyConnectedComponentDetection(Graph<T> graph, Comparator<T> comparator) {
        this.comparator = comparator
        this.graph = graph
    }

    Collection<Graph<T>> findCircular() {
        def strongGraph = new KosarajuSccGraph<T>()
        graph.edges.each {
            strongGraph.addEdge(it.from, it.to)
        }
        strongGraph.sccs
                .toSorted(new SetComparator(comparator))
                .collect { nodes ->
            new Graph<T>(
                    nodes: nodes,
                    edges: graph.edges.findAll { edge ->
                        nodes.contains(edge.from) && nodes.contains(edge.to)
                    }
            )
        }
    }
}
