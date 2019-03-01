package io.github.packageinsight.reports

import io.github.westonal.analysis.PackageCollection
import io.github.westonal.analysis.PackageName
import io.github.westonal.analysis.graph.scc.StronglyConnectedComponent
import io.github.westonal.analysis.graphbuilding.GraphBuilder
import io.github.westonal.analysis.graphbuilding.PackageSorting
import org.gradle.api.GradleException

class StronglyConnectedComponentReport {

    static void stronglyConnectedComponentsReport(PackageCollection packageCollection, int failureLimit, boolean printPackagesNotInScc) {
        if (failureLimit < 1) failureLimit = 1
        def sets = new GraphBuilder().addPackageCollection(packageCollection)
                .excludeExternalTo(packageCollection.packages*.packageName)
                .build()
                .findStronglyConnectedComponents(PackageSorting.byName)
                .sort { -it.size() }
        printLimited(sets, failureLimit)
        printOKScc(sets, failureLimit)
        if (printPackagesNotInScc) printSingles(sets)
        println ""
    }

    private static void printLimited(List<StronglyConnectedComponent> sets, int failureLimit) {
        def exceedingLimit = sets.findAll { it.size() > failureLimit }
        exceedingLimit.each { scc ->
            assert scc.size() > 1
            println "Circular package reference (Strongly connected component)"
            println " These ${scc.size()} packages are all interdependent on each other"
            printScc(scc)
            println ""
        }
        if (!exceedingLimit.isEmpty()) {
            throw new GradleException("Circular package references (Strongly connected components) found exceeding the limit of $failureLimit.")
        }
    }

    private static void printOKScc(List<StronglyConnectedComponent> sets, int failureLimit) {
        sets.findAll { it.size() > 1 && it.size() <= failureLimit }.each { scc ->
            assert scc.size() > 1
            println "Circular package reference (Strongly connected component)"
            println " These ${scc.size()} packages are all interdependent on each other, but under the limit of $failureLimit"
            printScc(scc)
            println ""
        }
    }

    private static void printSingles(List<StronglyConnectedComponent> sets) {
        println "These packages are not members of circular references (Strongly connected components)"
        sets.findAll { it.size() == 1 }*.nodes
                .collectMany { it }
                .toSorted(PackageSorting.byName).each { p -> println "    $p" }
    }

    private static List<PackageName> printScc(StronglyConnectedComponent scc) {
        scc.nodes.toSorted(PackageSorting.byName).each { p ->
            println "    $p"
        }
    }
}
