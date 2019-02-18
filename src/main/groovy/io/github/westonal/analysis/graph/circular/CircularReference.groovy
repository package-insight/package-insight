package io.github.westonal.analysis.graph.circular

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable
import io.github.westonal.analysis.graph.Edge

@Immutable
@EqualsAndHashCode
class CircularReference<T> {
    List<Edge<T>> edges

    @Override
    String toString() {
        edges.toString()
    }
}
