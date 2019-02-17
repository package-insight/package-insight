package io.github.westonal.analysis.graph

import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail

class GraphTest {

    @Test
    void create() {
        def edge = new Edge<String>("From", "To")
        def graph = new Graph<String>([edge] as Set)
        assert graph.edges == [edge] as Set
    }

    @Test
    void createWith2() {
        def edge1 = new Edge<Integer>(1, 2)
        def edge2 = new Edge<Integer>(1, 2)
        def graph = new Graph<Integer>([edge1, edge2] as Set)
        assert graph.edges == [edge1, edge2] as Set
    }

    @Test
    void immutable() {
        def edge = new Edge<Integer>(1, 2)
        def graph = new Graph<Integer>([] as Set)
        shouldFail {
            graph.edges.add(edge)
        }
    }
}
