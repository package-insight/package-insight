package io.github.packageinsight

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
                //println 'Android'
                project.android.sourceSets.all { set ->
                    //println set
                    // it.properties.each{println it}
                    //set.javaDirectories.each { println "  $it" }
                    def newTask = project.task("insight${set.name.capitalize()}") {
                        group 'Package Insight'
                        description "Package insight for source set $set.name"
                        doLast {
                            println "Package insight for :$project.name Set $set.name"
                            //set.javaDirectories.each { println "  $it" }
                            if (set.hasProperty('java')) {
                                println 'Java'
                                set.java.srcDirs.each { println "  $it" }
                            }
                            if (set.hasProperty('kotlin')) {
                                println 'Kotlin'
                                set.kotlin.srcDirs.each { println "  $it" }
                            }
                            if (set.hasProperty('groovy')) {
                                println 'Groovy'
                                set.groovy.srcDirs.each { println "  $it" }
                            }
                        }
                    }
                    insight.dependsOn newTask
                }
            } else {
                //println 'nonAndroid'
                if (project.hasProperty('sourceSets')) {
                    project.sourceSets.all { set ->
                        //println set
                        //set.allJava.files.each { println "  $it" }
                        //set.java.srcDirs.each { println "  $set" }
                        def newTask = project.task("insight${set.name.capitalize()}") {
                            group 'Package Insight'
                            description "Package insight for source set $set.name"
                            doLast {
                                println "Package insight for :$project.name Set $set.name"
                                //set.allJava.files.each { println "  $it" }
                                if (set.hasProperty('java')) {
                                    println 'Java'
                                    set.java.srcDirs.each { println "  $it" }
                                }
                                if (set.hasProperty('kotlin')) {
                                    println 'Kotlin'
                                    set.kotlin.srcDirs.each { println "  $it" }
                                }
                                if (set.hasProperty('groovy')) {
                                    println 'Groovy'
                                    set.groovy.srcDirs.each { println "  $it" }
                                }
                            }
                        }
                        insight.dependsOn newTask
                    }
                }
            }
        }
    }
}
