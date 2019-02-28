package io.github.westonal.analysis.graphbuilding

import io.github.westonal.analysis.PackageCollection
import io.github.westonal.analysis.SourceFile
import org.junit.Test

import static io.github.westonal.analysis.graphbuilding.Helpers.edge
import static io.github.westonal.analysis.graphbuilding.Helpers.p

class GraphBuilderTest {

    @Test
    void addASingleSourceFile() {
        def graph = new GraphBuilder(
        ).addSourceFile(
                new SourceFile(
                        packageName: p('a'),
                        importPackages: [
                                p('b.a'),
                                p('b.b')
                        ]
                )
        ).build()
        assert graph.edges == [
                edge('a->b.a'),
                edge('a->b.b')
        ] as Set
    }

    @Test
    void addTwoSourceFiles() {
        def graph = new GraphBuilder(
        ).addSourceFile(
                new SourceFile(
                        packageName: p('a'),
                        importPackages: [
                                p('b.a'),
                                p('b.b')
                        ]
                )
        ).addSourceFile(
                new SourceFile(
                        packageName: p('b'),
                        importPackages: [
                                p('c.a'),
                                p('a')
                        ]
                )
        ).build()
        assert graph.edges == [
                edge('a->b.a'),
                edge('a->b.b'),
                edge('b->a'),
                edge('b->c.a')
        ] as Set
    }

    @Test
    void addTwoSourceFilesByPackageCollection() {
        def graph = new GraphBuilder(
        ).addPackageCollection(
                packageCollection([
                        new SourceFile(
                                packageName: p('a'),
                                importPackages: [
                                        p('b.a'),
                                        p('b.b')
                                ]
                        ),
                        new SourceFile(
                                packageName: p('b'),
                                importPackages: [
                                        p('c.a'),
                                        p('a')
                                ]
                        )]
                )
        ).build()
        assert graph.edges == [
                edge('a->b.a'),
                edge('a->b.b'),
                edge('b->a'),
                edge('b->c.a')
        ] as Set
    }

    @Test
    void addTwoSourceFilesByPackageCollectionWithoutExternal() {
        def collection = packageCollection([
                new SourceFile(
                        packageName: p('a'),
                        importPackages: [
                                p('b.a'),
                                p('b.b')
                        ]
                ),
                new SourceFile(
                        packageName: p('b'),
                        importPackages: [
                                p('c.a'),
                                p('a')
                        ]
                )]
        )
        def graph = new GraphBuilder()
                .addPackageCollection(collection)
                .excludeExternalTo(collection.packages*.packageName)
                .build()
        assert graph.edges == [
                edge('b->a')
        ] as Set
    }

    static def packageCollection(List<SourceFile> sourceFiles) {
        def pc = new PackageCollection()
        sourceFiles.each { pc.addSourceFile(it) }
        pc
    }
}
