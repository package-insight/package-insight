package io.github.westonal.analysis.graph.circular

import io.github.westonal.analysis.graph.Edge
import io.github.westonal.analysis.graph.Graph
import org.junit.Test

class CircularDetectionTest {

    @Test
    void empty() {
        assert new Graph(edges: []).findCircular(10) == []
    }

    @Test
    void oneEdge() {
        assert new Graph(edges: [
                edge(1, 2)
        ]
        ).findCircular(10) == []
    }

    @Test
    void selfReferencing() {
        assert new Graph(edges: [
                edge(1, 1)
        ]
        ).findCircular(10) == []
    }

    @Test
    void directCircularReference() {
        assert new Graph(edges: [
                edge(1, 2),
                edge(2, 1)
        ]
        ).findCircular(10) == [
                circular(1, 2)
        ]
    }

    @Test
    void indirectCircularReference() {
        assert new Graph(edges: [
                edge(1, 2),
                edge(2, 3),
                edge(3, 1)
        ]
        ).findCircular(10) == [
                circular(1, 2, 3)
        ]
    }

    @Test
    void twoCircularReferencesDisjoint() {
        assert new Graph(edges: [
                edge(1, 2),
                edge(2, 1),
                edge(3, 4),
                edge(4, 3)
        ]
        ).findCircular(10) == [
                circular(1, 2),
                circular(3, 4)
        ]
    }

    @Test
    void twoCircularReferencesContainingSameEdge() {
        assert new Graph(edges: [
                edge(1, 2),
                edge(2, 1),
                edge(1, 3),
                edge(3, 1)
        ]
        ).findCircular(10) == [
                circular(1, 2),
                circular(1, 3)
        ]
    }

    @Test
    void circularReferenceWithAdditionalEntryPoint() {
        assert new Graph(edges: [
                edge(1, 2),
                edge(2, 3),
                edge(3, 4),
                edge(4, 2)
        ]
        ).findCircular(10) == [
                circular(2, 3, 4)
        ]
    }

    @Test
    void twoCircularReferences() {
        assert new Graph(edges: [
                edge(3, 4),
                edge(1, 2),
                edge(2, 3),
                edge(3, 1),
                edge(4, 5),
                edge(5, 2)
        ]
        ).findCircular(10) == [
                circular(2, 3, 4, 5),
                circular(1, 2, 3)
        ]
    }

    @Test
    void twoCircularReferencesLimit1() {
        assert new Graph(edges: [
                edge(3, 4),
                edge(1, 2),
                edge(2, 3),
                edge(3, 1),
                edge(4, 5),
                edge(5, 2)
        ]
        ).findCircular(1) == [
                circular(2, 3, 4, 5)
        ]
    }

    @Test
    void twoCircularReferencesWithShortCut() {
        assert new Graph(edges: [
                edge(1, 2),
                edge(2, 3),
                edge(3, 1),
                edge(1, 3)
        ]
        ).findCircular(10) == [
                circular(1, 2, 3),
                circular(1, 3)
        ]
    }

    @Test
    void twoCircularReferencesWithShortCutFirst() {
        assert new Graph(edges: [
                edge(1, 3),
                edge(1, 2),
                edge(2, 3),
                edge(3, 1)
        ]
        ).findCircular(10) == [
                circular(1, 3),
                circular(1, 2, 3)
        ]
    }

    @Test
    void twoCircularReferencesWithShortCut2() {
        assert new Graph(edges: [
                edge(1, 2),
                edge(2, 3),
                edge(3, 4),
                edge(4, 1),
                edge(2, 4),
        ]
        ).findCircular(10) == [
                circular(1, 2, 3, 4),
                circular(1, 2, 4)
        ]
    }

    @Test
    void circularReferencesInSquareWithCenterConnector() {
        assert new Graph(edges: [
                edge(1, 2),
                edge(2, 3),
                edge(3, 4),
                edge(4, 1),
                edge(5, 1),
                edge(2, 5),
                edge(3, 5),
                edge(4, 5),
        ]
        ).findCircular(10) == [
                circular(1, 2, 3, 4),
                circular(1, 2, 3, 4, 5),
                circular(1, 2, 3, 5),
                circular(1, 2, 5),
        ]
    }


    static Edge edge(int from, int to) {
        new Edge(from, to)
    }

    static def circular(Integer... nodes) {
        def edges = []
        for (int i = 0; i < nodes.length; i++) {
            edges.add(edge(nodes[i], nodes[(i + 1) % nodes.length]))
        }
        new CircularReference(edges)
    }
}
