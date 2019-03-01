package io.github.packageinsight.analysis.graph

import groovy.transform.Immutable

@Immutable
class Graph<T> {
    Set<Edge<T>> edges
    Set<T> nodes

    static <T> Graph<T> fromEdges(Collection<Edge<T>> edges) {
        def setEdges = edges as Set
        new Graph<T>(setEdges, (setEdges*.from + setEdges*.to) as Set<T>)
    }

    Collection<Graph<T>> findStronglyConnectedComponents(Comparator<T> comparator) {
        new StronglyConnectedComponentDetection<>(this, comparator).findCircular()
    }

    Set<Graph<T>> findStronglyConnectedComponents() {
        findStronglyConnectedComponents({ o1, o2 -> o1 <=> o2 })
    }

    int getSize() {
        nodes.size()
    }
}
