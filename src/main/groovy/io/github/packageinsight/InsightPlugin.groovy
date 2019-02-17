package io.github.packageinsight

import groovy.io.FileType
import io.github.westonal.analysis.PackageCollection
import io.github.westonal.analysis.SourceFile
import org.gradle.api.Plugin
import org.gradle.api.Project

class InsightPlugin implements Plugin<Project> {

    @Override
    void apply(Project px) {
        px.afterEvaluate { project ->
            def insight = project.task('insight') {
                group 'Package Insight'
                description 'Package insight supertask'
                doLast {
                    println 'Package insight supertask'
                }
            }

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
                    println 'Java'
                    set.java.srcDirs.each { dir -> importDir(dir, packageCollection) }
                }
                if (set.hasProperty('kotlin')) {
                    println 'Kotlin'
                    set.kotlin.srcDirs.each { dir -> importDir(dir, packageCollection) }
                }
                if (set.hasProperty('groovy')) {
                    println 'Groovy'
                    set.groovy.srcDirs.each { dir -> importDir(dir, packageCollection) }
                }

                println 'All packages:'
                packageCollection.packages*.packageName*.name.sort().each { println "  $it" }
            }
        }
    }

    private static void importDir(dir, packageCollection) {
        if (!dir.exists()) return
        dir.eachFileRecurse(FileType.FILES) { file ->
            String[] lines = file.readLines()
            packageCollection.addSourceFile(SourceFile.fromLines(lines))
        }
    }
}
