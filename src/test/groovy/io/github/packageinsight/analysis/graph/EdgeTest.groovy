package io.github.packageinsight.analysis.graph

import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail

class EdgeTest {

    @Test
    void create() {
        def edge = new Edge<String>("From", "To")
        assert edge.from == "From"
        assert edge.to == "To"
    }

    @Test
    void assertEqualityContract_Equal() {
        assert new Edge<String>("From", "To") == new Edge<String>("From", "To")
    }

    @Test
    void assertEqualityContract_NotEqual() {
        assert new Edge<String>("From", "To") != new Edge<String>("To", "From")
    }

    @Test
    void immutable() {
        def edge = new Edge<Integer>(1, 2)
        shouldFail {
            //noinspection GroovyAccessibility,GrFinalVariableAccess
            edge.from = 3
        }
        shouldFail {
            //noinspection GroovyAccessibility,GrFinalVariableAccess
            edge.to = 3
        }
        assert edge.from == 1
        assert edge.to == 2
    }

    @Test
    void toStringFormat() {
        def edge = new Edge<String>("A", "B")
        assert edge.toString() == "A -> B"
    }
}
