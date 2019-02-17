package io.github.westonal.analysis.graph

import groovy.transform.Immutable

@Immutable
class Graph<T> {
    Set<Edge<T>> edges
}
