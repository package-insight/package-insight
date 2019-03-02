package io.github.packageinsight.plugin.reports

import io.github.packageinsight.analysis.code.PackageCollection

class BannedImportReport {

    static boolean findBanned(PackageCollection packageCollection, List banned) {
        if (banned == null || banned.isEmpty()) return false

        def bannedSeen = false

        Set<String> bannedSet = banned.findAll { b -> b instanceof String }.toSet()

        println 'Looking for banned import packages'
        bannedSet.each { println "  $it" }

        packageCollection.packages.each { p ->
            p.sourceFiles.each { s ->
                def all = s.imports.findAll { i -> bannedSet.contains(i.packageName.name) }
                if (!all.isEmpty()) {
                    println ''
                    println "Banned import package was detected in source file ${s.fileName}"
                    all.each { i ->
                        println "  L${i.lineNo}: ${i.originalImport}"
                    }
                    bannedSeen = true
                }
            }
        }

        if (bannedSeen) throw new RuntimeException('Banned import packages seen')

        println 'No banned import packages seen'
        println ''

        return true
    }
}
