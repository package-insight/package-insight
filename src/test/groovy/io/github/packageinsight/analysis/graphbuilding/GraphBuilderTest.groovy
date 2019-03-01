package io.github.packageinsight.analysis.graphbuilding

import io.github.packageinsight.analysis.code.PackageCollection
import io.github.packageinsight.analysis.code.SourceFile
import org.junit.Test

import static io.github.packageinsight.analysis.graphbuilding.Helpers.edge
import static io.github.packageinsight.analysis.graphbuilding.Helpers.p

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

    @Test
    void scc() {
        def scc = new GraphBuilder()
                .addPackageCollection(
                packageCollection([
                        new SourceFile(
                                packageName: p('a'),
                                importPackages: [
                                        p('b')
                                ]
                        ),
                        new SourceFile(
                                packageName: p('b'),
                                importPackages: [
                                        p('a')
                                ]
                        ),
                        new SourceFile(
                                packageName: p('d'),
                                importPackages: [
                                        p('e')
                                ]
                        )]
                )
        )
                .build()
                .findStronglyConnectedComponents(PackageSorting.byName)
        assert scc.edges == [[edge('a->b'),
                              edge('b->a')] as Set,
                             [] as Set,
                             [] as Set]
        assert scc.nodes == [[p('a'),
                              p('b')] as Set,
                             [p('d')] as Set,
                             [p('e')] as Set]
    }

    static def packageCollection(List<SourceFile> sourceFiles) {
        def pc = new PackageCollection()
        sourceFiles.each { pc.addSourceFile(it) }
        pc
    }
}
