package io.github.westonal.analysis.graphbuilding

import io.github.westonal.analysis.PackageName
import io.github.westonal.analysis.graph.Edge

class EdgeSorting {

    private final static Comparator<Edge<PackageName>> byFrom =
            Comparator.comparing({ Edge<PackageName> edge -> edge.from.name })

    private final static Comparator<Edge<PackageName>> byTo =
            Comparator.comparing({ Edge<PackageName> edge -> edge.to.name })

    final static Comparator<Edge<PackageName>> byFromTo =
            byFrom.thenComparing(byTo)
}
