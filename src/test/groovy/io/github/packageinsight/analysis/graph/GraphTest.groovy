package io.github.packageinsight.analysis.graph

import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail

class GraphTest {

    @Test
    void create() {
        def edge = new Edge<String>("From", "To")
        Graph<String> graph = Graph.fromEdges([edge])
        assert graph.edges == [edge] as Set
        assert graph.nodes == ["From", "To"] as Set
        assert graph.size == 2
    }

    @Test
    void create_with_one() {
        def edge = new Edge<String>("From", "From")
        Graph<String> graph = Graph.fromEdges([edge])
        assert graph.edges == [edge] as Set
        assert graph.nodes == ["From"] as Set
        assert graph.size == 1
    }

    @Test
    void createWith2() {
        def edge1 = new Edge<Integer>(1, 2)
        def edge2 = new Edge<Integer>(1, 2)
        Graph<Integer> graph = Graph.fromEdges([edge1, edge2])
        assert graph.edges == [edge1, edge2] as Set
        assert graph.nodes == [1, 2] as Set
        assert graph.size == 2
    }

    @Test
    void immutable() {
        def edge = new Edge<Integer>(1, 2)
        def graph = Graph.fromEdges([] as Set)
        shouldFail {
            graph.edges.add(edge)
        }
        shouldFail {
            graph.nodes.add(4)
        }
    }
}
