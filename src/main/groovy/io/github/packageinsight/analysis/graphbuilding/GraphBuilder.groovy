package io.github.packageinsight.analysis.graphbuilding

import io.github.packageinsight.analysis.code.PackageCollection
import io.github.packageinsight.analysis.code.PackageName
import io.github.packageinsight.analysis.code.SourceFile
import io.github.packageinsight.analysis.graph.Edge
import io.github.packageinsight.analysis.graph.Graph

class GraphBuilder {
    private final Set<Edge<PackageName>> edges = new HashSet<>()

    GraphBuilder addSourceFile(SourceFile sourceFile) {
        def from = sourceFile.packageName
        sourceFile.importPackages.each { to ->
            edges.add(new Edge<>(from, to))
        }
        this
    }

    GraphBuilder addPackageCollection(PackageCollection packageCollection) {
        packageCollection.packages.each { p ->
            p.sourceFiles.each {
                source -> addSourceFile(source)
            }
        }
        this
    }

    GraphBuilder excludeExternalTo(Collection<PackageName> packageNames) {
        Set<PackageName> packageNameSet = packageNames.toSet()
        edges.removeIf { !(it.to in packageNameSet) }
        this
    }

    Graph<PackageName> build() {
        Graph.fromEdges(edges)
    }
}
