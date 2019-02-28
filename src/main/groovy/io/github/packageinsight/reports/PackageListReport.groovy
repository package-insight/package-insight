package io.github.packageinsight.reports

import io.github.westonal.analysis.PackageCollection

class PackageListReport {

    static def listPackages(PackageCollection packageCollection) {
        println 'All packages:'
        def allPackageNames = packageCollection.packages*.packageName
        def dependencies = packageCollection.packages.collectEntries { p ->
            [(p.packageName): p.dependsOn.intersect(allPackageNames)]
        }.sort { -it.value.size() }
        dependencies.findAll { !it.value.isEmpty() }.each { p ->
            println "  ${p.key} depends on ${p.value.size()} internal packages"
        }
        def all = dependencies.findAll { it.value.isEmpty() }
        if (!all.isEmpty()) {
            println ''
            println 'The following packages have no internal dependencies and are potential candidates for modularization'
            all.each { p ->
                println "  ${p.key}"
            }
        }
        println ''
    }
}
