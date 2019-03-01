package io.github.packageinsight.analysis.graphbuilding

import org.junit.Test

import static io.github.packageinsight.analysis.graphbuilding.Helpers.edge
import static io.github.packageinsight.analysis.graphbuilding.Helpers.p

class EdgeSortingTest {

    @Test
    void fromSorting() {
        def edges = [
                edge(p('b'), p('c')),
                edge(p('a'), p('c'))
        ]
        edges.sort(EdgeSorting.byFromTo)
        assert edges == [
                edge(p('a'), p('c')),
                edge(p('b'), p('c'))
        ]
    }

    @Test
    void toSorting() {
        def edges = [
                edge(p('a'), p('c')),
                edge(p('a'), p('b'))
        ]
        edges.sort(EdgeSorting.byFromTo)
        assert edges == [
                edge(p('a'), p('b')),
                edge(p('a'), p('c'))
        ]
    }

    @Test
    void fromThenToSorting() {
        def edges = [
                edge(p('b'), p('c')),
                edge(p('a'), p('d'))
        ]
        edges.sort(EdgeSorting.byFromTo)
        assert edges == [
                edge(p('a'), p('d')),
                edge(p('b'), p('c'))
        ]
    }

    @Test
    void fromThenToSortingBothUsed() {
        def edges = [
                edge(p('b'), p('f')),
                edge(p('b'), p('e')),
                edge(p('a'), p('d')),
                edge(p('a'), p('c'))
        ]
        edges.sort(EdgeSorting.byFromTo)
        assert edges == [
                edge(p('a'), p('c')),
                edge(p('a'), p('d')),
                edge(p('b'), p('e')),
                edge(p('b'), p('f'))
        ]
    }
}
