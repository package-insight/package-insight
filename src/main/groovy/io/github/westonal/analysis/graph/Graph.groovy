package io.github.westonal.analysis.graph

import groovy.transform.Immutable
import io.github.westonal.analysis.graph.scc.StronglyConnectedComponentDetection
import io.github.westonal.analysis.graph.scc.StronglyConnectedComponent

@Immutable
class Graph<T> {
    Set<Edge<T>> edges

    Collection<StronglyConnectedComponent<T>> findStronglyConnectedComponents(Comparator<T> comparator) {
        new StronglyConnectedComponentDetection<>(this, comparator).findCircular()
    }

    Set<StronglyConnectedComponent<T>> findStronglyConnectedComponents() {
        findStronglyConnectedComponents({ o1, o2 -> o1 <=> o2 })
    }
}
