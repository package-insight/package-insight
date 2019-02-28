package io.github.westonal.analysis.graph.scc

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable
import io.github.westonal.analysis.graph.Edge

@Immutable
@EqualsAndHashCode
class StronglyConnectedComponent<T> {
    Set<T> nodes
    Set<Edge<T>> edges

    @Override
    String toString() {
        edges.toString()
    }
}
