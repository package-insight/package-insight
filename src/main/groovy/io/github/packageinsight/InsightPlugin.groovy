package io.github.packageinsight

import groovy.io.FileType
import io.github.westonal.analysis.ImportLine
import io.github.westonal.analysis.PackageCollection
import io.github.westonal.analysis.PackageName
import io.github.westonal.analysis.SourceFile
import io.github.westonal.analysis.graph.circular.CircularReference
import io.github.westonal.analysis.graphbuilding.GraphBuilder
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

enum Level {
    Disabled,
    Warning,
    Error
}

class InsightPluginExtension {
    Level circularDependency
    boolean listPackages
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

                Level level = project.packageInsight.circularDependency
                if (level != null && level != Level.Disabled) {
                    circularDependencyReport(packageCollection, level)
                }

                if (project.packageInsight.listPackages) {
                    listPackages(packageCollection)
                }

                println project.packageInsight.circularDependency
            }
        }
    }

    private static void listPackages(PackageCollection packageCollection) {
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

    private static void circularDependencyReport(PackageCollection packageCollection, Level level) {
        def start = System.currentTimeMillis()
        def limit = 1000
        def circular = new GraphBuilder().addPackageCollection(packageCollection)
                .build()
                .findCircular(limit + 1, { p1, p2 -> p1.name <=> p2.name })
        def end = System.currentTimeMillis()
        def limited = circular.take(limit)
        limited.each {
            circularRef ->
                printRef(circularRef)
        }
        def reportedDependencyCount = limited.size()
        println "${circular.size() > limit ? 'More than ' : ''}" +
                "${reportedDependencyCount} circular package dependencies found (took ${end - start}ms)"

        if (reportedDependencyCount == 0) {
            println 'No circular package dependencies found.'
            println ''
            return
        }

        println ''
        def hotEdges = limited.collectMany { it.edges }
                .countBy { it }
                .entrySet()
                .sort { -it.value }
                .take(10)
        println "Top ${hotEdges.size()} Hot edges"
        hotEdges.each { hotEdge ->
            println ''
            println "  ${hotEdge.key} appears in ${hotEdge.value}/${reportedDependencyCount} circular dependencies"

            def hotPackage = hotEdge.key.from
            def hotPackageTarget = hotEdge.key.to

            Map<SourceFile, List<ImportLine>> imports = packageCollection.packages.find { it.packageName == hotPackage }
                    .sourceFiles.collectEntries { [(it): it.imports.findAll { it.packageName == hotPackageTarget }] }
                    .findAll { it.value.size() > 0 }
            println "    Occurs in ${imports.collect { it.value.size() }.sum()} imports "
            imports.sort { it.key.fileName }
                    .each {
                println "      File ${it.key.fileName}"
                it.value.sort { it.lineNo }.each {
                    println "        Line ${it.lineNo} : ${it.originalImport}"
                }
            }
        }
        println ''
        if (level == Level.Error) {
            throw new GradleException('Circular package dependencies found.')
        }
    }

    private static def printRef(CircularReference<PackageName> circularReference) {
        println 'Circular package dependency'
        println " -> ${circularReference.edges[0].from}"
        circularReference.edges.each {
            println " -> $it.to"
        }
        println " "
    }

    private static void importDir(dir, packageCollection) {
        if (!dir.exists()) return
        dir.eachFileRecurse(FileType.FILES) { file ->
            String[] lines = file.readLines()
            packageCollection.addSourceFile(SourceFile.fromLines(file.toString(), lines))
        }
    }
}
