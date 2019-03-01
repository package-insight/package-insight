package io.github.packageinsight.analysis.graph.scc


import org.junit.Test

class KosarajuSccGraphTest {

    @Test
    void integer() {
        KosarajuSccGraph g = new KosarajuSccGraph<Integer>()
        g.addEdge(1, 0)
        g.addEdge(0, 2)
        g.addEdge(2, 1)
        g.addEdge(0, 3)
        g.addEdge(3, 4)

        assert g.getSccs() == [
                [0, 1, 2] as Set,
                [3] as Set,
                [4] as Set
        ]
    }

    @Test
    void string() {
        KosarajuSccGraph g = new KosarajuSccGraph<String>()
        g.addEdge("1", "0")
        g.addEdge("0", "2")
        g.addEdge("2", "1")
        g.addEdge("0", "3")
        g.addEdge("3", "4")

        assert g.getSccs() == [
                ["0", "1", "2"] as Set,
                ["3"] as Set,
                ["4"] as Set
        ]
    }
}
