package io.github.packageinsight

import groovy.io.FileType
import io.github.packageinsight.reports.PackageListReport
import io.github.westonal.analysis.PackageCollection
import io.github.westonal.analysis.SourceFile
import org.gradle.api.Plugin
import org.gradle.api.Project

class InsightPluginExtension {
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

                if (project.packageInsight.listPackages) {
                    new PackageListReport().listPackages(packageCollection)
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
