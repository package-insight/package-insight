package io.github.packageinsight.plugin

import groovy.io.FileType
import io.github.packageinsight.analysis.code.PackageCollection
import io.github.packageinsight.analysis.code.SourceFile
import io.github.packageinsight.plugin.reports.BannedImportReport
import io.github.packageinsight.plugin.reports.PackageListReport
import io.github.packageinsight.reports.StronglyConnectedComponentReport
import org.gradle.api.Plugin
import org.gradle.api.Project

class InsightPluginExtension {
    boolean listPackages
    int stronglyConnectedComponentLimit
    boolean printPackagesNotInScc
    List banned
}

class InsightPlugin implements Plugin<Project> {

    @Override
    void apply(Project px) {
        def insight = px.task('insight') {
            group 'Package Insight'
            description 'Package insight all'
        }
        px.extensions.create("packageInsight", InsightPluginExtension)
        px.afterEvaluate { project ->
            if (project.hasProperty('android')) {
                project.android.sourceSets.all { set ->
                    insight.dependsOn setToTask(project, set)
                }
            } else {
                if (project.hasProperty('sourceSets')) {
                    project.sourceSets.all { set ->
                        insight.dependsOn setToTask(project, set)
                    }
                }
            }
        }
    }

    private static def setToTask(project, set) {
        project.task("insight${set.name.capitalize()}") {
            group 'Package Insight'
            description "Package insight for source set $set.name"
            doLast {
                def packageCollection = new PackageCollection()
                println "Package insight for :$project.name Set $set.name"
                if (set.hasProperty('java')) {
                    set.java.srcDirs.each { dir -> importDir(dir, packageCollection) }
                }
                if (set.hasProperty('kotlin')) {
                    set.kotlin.srcDirs.each { dir -> importDir(dir, packageCollection) }
                }
                if (set.hasProperty('groovy')) {
                    set.groovy.srcDirs.each { dir -> importDir(dir, packageCollection) }
                }

                boolean didSomething = false

                if (project.packageInsight.listPackages) {
                    new PackageListReport().listPackages(packageCollection)
                    didSomething = true
                }

                def sccLimit = (int) project.packageInsight.stronglyConnectedComponentLimit
                if (sccLimit > 0) {
                    new StronglyConnectedComponentReport().stronglyConnectedComponentsReport(
                            packageCollection,
                            sccLimit,
                            (boolean) project.packageInsight.printPackagesNotInScc
                    )
                    didSomething = true
                }

                if (BannedImportReport.findBanned(packageCollection, (List) project.packageInsight.banned)) {
                    didSomething = true
                }

                if (!didSomething) {
                    println 'Nothing for package insight to do, set some options:'
                    println ''
                    println 'packageInsight {\n' +
                            '    listPackages true\n' +
                            '    stronglyConnectedComponentLimit 1\n' +
                            '    printPackagesNotInScc true\n' +
                            '    banned = [\n' +
                            '        \'junit.framework\'\n' +
                            '    ]\n' +
                            '}'
                    throw new RuntimeException("Nothing for package insight to do")
                }
            }
        }
    }

    private static void importDir(dir, packageCollection) {
        if (!dir.exists()) return
        dir.eachFileRecurse(FileType.FILES) { file ->
            String[] lines = file.readLines()
            packageCollection.addSourceFile(SourceFile.fromLines(file.toString(), lines))
        }
    }
}
