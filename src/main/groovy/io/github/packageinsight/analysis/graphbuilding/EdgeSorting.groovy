package io.github.packageinsight.analysis.graphbuilding

import io.github.packageinsight.analysis.code.PackageName
import io.github.packageinsight.analysis.graph.Edge

class EdgeSorting {

    private final static Comparator<Edge<PackageName>> byFrom =
            Comparator.comparing({ Edge<PackageName> edge -> edge.from.name })

    private final static Comparator<Edge<PackageName>> byTo =
            Comparator.comparing({ Edge<PackageName> edge -> edge.to.name })

    final static Comparator<Edge<PackageName>> byFromTo =
            byFrom.thenComparing(byTo)
}
