package io.github.packageinsight.analysis.graph

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class Edge<Node> {
    final Node from
    final Node to

    Edge(Node from, Node to) {
        this.from = from
        this.to = to
    }

    @Override
    String toString() {
        "$from -> $to"
    }
}
