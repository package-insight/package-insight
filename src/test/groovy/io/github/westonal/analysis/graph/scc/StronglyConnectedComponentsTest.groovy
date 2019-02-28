package io.github.westonal.analysis.graph.scc

import io.github.westonal.analysis.graph.Edge
import io.github.westonal.analysis.graph.Graph
import org.junit.Test

class StronglyConnectedComponentsTest {

    @Test
    void empty() {
        assert new Graph(edges: []).findStronglyConnectedComponents().isEmpty()
    }

    @Test
    void oneEdge_nodes() {
        assert new Graph(edges: [
                edge(1, 2)
        ]
        ).findStronglyConnectedComponents()*.nodes == [[1] as Set, [2] as Set]
    }

    @Test
    void oneEdge_edges() {
        assert new Graph(edges: [
                edge(1, 2)
        ]
        ).findStronglyConnectedComponents()*.edges == [[] as Set, [] as Set]
    }

    @Test
    void selfReferencing() {
        def scc = new Graph(edges: [
                edge(1, 1)
        ]
        ).findStronglyConnectedComponents()
        assert scc*.nodes == [[1] as Set]
        assert scc*.edges == [[edge(1, 1)] as Set]
    }

    @Test
    void directCircularReference() {
        def scc = new Graph(edges: [
                edge(1, 2),
                edge(2, 1)
        ]
        ).findStronglyConnectedComponents()
        assert scc*.nodes == [[1, 2] as Set]
        assert scc*.edges == [[edge(1, 2), edge(2, 1)] as Set]
    }

    @Test
    void indirectCircularReference() {
        def scc = new Graph(edges: [
                edge(1, 2),
                edge(2, 3),
                edge(3, 1)
        ]
        ).findStronglyConnectedComponents()
        assert scc*.nodes == [[1, 2, 3] as Set]
        assert scc*.edges == [[edge(1, 2), edge(2, 3), edge(3, 1)] as Set]
    }

    @Test
    void twoCircularReferencesDisjoint() {
        def scc = new Graph(edges: [
                edge(1, 2),
                edge(2, 1),
                edge(3, 4),
                edge(4, 3)
        ]
        ).findStronglyConnectedComponents()
        assert scc*.nodes == [[1, 2] as Set,
                              [3, 4] as Set]
        assert scc*.edges == [[edge(1, 2), edge(2, 1)] as Set,
                              [edge(3, 4), edge(4, 3)] as Set]
    }

    @Test
    void twoCircularReferencesContainingSameEdge() {
        def scc = new Graph(edges: [
                edge(1, 2),
                edge(2, 1),
                edge(1, 3),
                edge(3, 1)
        ]
        ).findStronglyConnectedComponents()
        assert scc*.nodes == [[1, 2, 3] as Set]
        assert scc*.edges == [[edge(1, 2),
                               edge(2, 1),
                               edge(1, 3),
                               edge(3, 1)] as Set]
    }

    @Test
    void circularReferenceWithAdditionalEntryPoint() {
        def scc = new Graph(edges: [
                edge(1, 2),
                edge(2, 3),
                edge(3, 4),
                edge(4, 2)
        ]
        ).findStronglyConnectedComponents()
        assert scc*.nodes == [[1] as Set,
                              [2, 3, 4] as Set]
        assert scc*.edges == [[] as Set,
                              [edge(2, 3),
                               edge(3, 4),
                               edge(4, 2)] as Set]
    }

    @Test
    void twoCircularReferences() {
        def scc = new Graph(edges: [
                edge(3, 4),
                edge(1, 2),
                edge(2, 3),
                edge(3, 1),
                edge(4, 5),
                edge(5, 2)
        ]
        ).findStronglyConnectedComponents()
        assert scc*.nodes == [[1, 2, 3, 4, 5] as Set]
        assert scc*.edges == [[edge(3, 4),
                               edge(1, 2),
                               edge(2, 3),
                               edge(3, 1),
                               edge(4, 5),
                               edge(5, 2)] as Set]
    }

    @Test
    void twoCircularReferencesWithShortCut() {
        def scc = new Graph(edges: [
                edge(1, 2),
                edge(2, 3),
                edge(3, 1),
                edge(1, 3)
        ]
        ).findStronglyConnectedComponents()
        assert scc*.nodes == [[1, 2, 3] as Set]
        assert scc*.edges == [[edge(1, 2),
                               edge(2, 3),
                               edge(3, 1),
                               edge(1, 3)] as Set]
    }

    @Test
    void twoCircularReferencesWithShortCutFirst() {
        def scc = new Graph(edges: [
                edge(1, 3),
                edge(1, 2),
                edge(2, 3),
                edge(3, 1)
        ]
        ).findStronglyConnectedComponents()
        assert scc*.nodes == [[1, 2, 3] as Set]
        assert scc*.edges == [[edge(1, 2),
                               edge(2, 3),
                               edge(3, 1),
                               edge(1, 3)] as Set]
    }

    @Test
    void twoCircularReferencesWithShortCut2() {
        def scc = new Graph(edges: [
                edge(1, 2),
                edge(2, 3),
                edge(3, 4),
                edge(4, 1),
                edge(2, 4),
        ]
        ).findStronglyConnectedComponents()
        assert scc*.nodes == [[1, 2, 3, 4] as Set]
        assert scc*.edges == [[edge(1, 2),
                               edge(2, 3),
                               edge(3, 4),
                               edge(4, 1),
                               edge(2, 4)] as Set]
    }

    @Test
    void circularReferencesInSquareWithCenterConnector() {
        def scc = new Graph(edges: [
                edge(1, 2),
                edge(2, 3),
                edge(3, 4),
                edge(4, 1),
                edge(5, 1),
                edge(2, 5),
                edge(3, 5),
                edge(4, 5),
        ]
        ).findStronglyConnectedComponents()
        assert scc*.nodes == [[1, 2, 3, 4, 5] as Set]
        assert scc*.edges == [[edge(1, 2),
                               edge(2, 3),
                               edge(3, 4),
                               edge(4, 1),
                               edge(5, 1),
                               edge(2, 5),
                               edge(3, 5),
                               edge(4, 5)] as Set]
    }


    static Edge edge(int from, int to) {
        new Edge(from, to)
    }
}
