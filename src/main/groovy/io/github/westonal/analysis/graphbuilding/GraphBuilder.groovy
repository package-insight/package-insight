package io.github.westonal.analysis.graphbuilding

import io.github.westonal.analysis.PackageCollection
import io.github.westonal.analysis.PackageName
import io.github.westonal.analysis.SourceFile
import io.github.westonal.analysis.graph.Edge
import io.github.westonal.analysis.graph.Graph

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

    Graph build() {
        new Graph(edges)
    }
}
