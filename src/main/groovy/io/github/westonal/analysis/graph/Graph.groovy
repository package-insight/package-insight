package io.github.westonal.analysis.graph


import groovy.transform.Immutable
import io.github.westonal.analysis.graph.circular.CircularDetection
import io.github.westonal.analysis.graph.circular.CircularReference

@Immutable
class Graph<T> {
    Set<Edge<T>> edges

    Collection<CircularReference<T>> findCircular(int limit, Comparator<T> comparator) {
        new CircularDetection<>(this, limit, comparator).findCircular()
    }

    Collection<CircularReference<T>> findCircular(int limit) {
        findCircular(limit, { o1, o2 -> o1 <=> o2 })
    }
}
