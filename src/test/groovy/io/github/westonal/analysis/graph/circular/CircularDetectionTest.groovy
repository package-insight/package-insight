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
                new Edge(1, 2)
        ]
        ).findCircular(10) == []
    }

    @Test
    void selfReferencing() {
        assert new Graph(edges: [
                new Edge(1, 1)
        ]
        ).findCircular(10) == []
    }

    @Test
    void directCircularReference() {
        assert new Graph(edges: [
                new Edge(1, 2),
                new Edge(2, 1)
        ]
        ).findCircular(10) == [
                circular([new Edge(1, 2),
                          new Edge(2, 1)]
                )
        ]
    }

    @Test
    void indirectCircularReference() {
        assert new Graph(edges: [
                new Edge(1, 2),
                new Edge(2, 3),
                new Edge(3, 1)
        ]
        ).findCircular(10) == [
                circular([new Edge(1, 2),
                          new Edge(2, 3),
                          new Edge(3, 1)]
                )
        ]
    }

    @Test
    void twoCircularReferencesDisjoint() {
        assert new Graph(edges: [
                new Edge(1, 2),
                new Edge(2, 1),
                new Edge(3, 4),
                new Edge(4, 3)
        ]
        ).findCircular(10) == [
                circular([new Edge(1, 2),
                          new Edge(2, 1)]
                ),
                circular([new Edge(3, 4),
                          new Edge(4, 3)]
                )
        ]
    }

    @Test
    void twoCircularReferencesContainingSameEdge() {
        assert new Graph(edges: [
                new Edge(1, 2),
                new Edge(2, 1),
                new Edge(1, 3),
                new Edge(3, 1)
        ]
        ).findCircular(10) == [
                circular([new Edge(1, 2),
                          new Edge(2, 1)]
                ),
                circular([new Edge(1, 3),
                          new Edge(3, 1)]
                )
        ]
    }

    @Test
    void circularReferenceWithAdditionalEntryPoint() {
        assert new Graph(edges: [
                new Edge(1, 2),
                new Edge(2, 3),
                new Edge(3, 4),
                new Edge(4, 2)
        ]
        ).findCircular(10) == [
                circular([new Edge(2, 3),
                          new Edge(3, 4),
                          new Edge(4, 2)]
                )
        ]
    }

    @Test
    void twoCircularReferences() {
        assert new Graph(edges: [
                new Edge(3, 4),
                new Edge(1, 2),
                new Edge(2, 3),
                new Edge(3, 1),
                new Edge(4, 5),
                new Edge(5, 2)
        ]
        ).findCircular(10) == [
                circular([new Edge(2, 3),
                          new Edge(3, 4),
                          new Edge(4, 5),
                          new Edge(5, 2)]
                ),
                circular([new Edge(1, 2),
                          new Edge(2, 3),
                          new Edge(3, 1)]
                )
        ]
    }

    @Test
    void twoCircularReferencesLimit1() {
        assert new Graph(edges: [
                new Edge(3, 4),
                new Edge(1, 2),
                new Edge(2, 3),
                new Edge(3, 1),
                new Edge(4, 5),
                new Edge(5, 2)
        ]
        ).findCircular(1) == [
                circular([new Edge(2, 3),
                          new Edge(3, 4),
                          new Edge(4, 5),
                          new Edge(5, 2)]
                )
        ]
    }

    static def circular(List<Edge> edges) {
        new CircularReference(edges)
    }
}
