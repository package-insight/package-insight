package io.github.packageinsight.analysis.code

import org.junit.Test

class PackageCollectionTest {

    @Test
    void create() {
        def collection = new PackageCollection()
        assert collection.packages.isEmpty()
    }

    @Test
    void addSourceFile() {
        def collection = new PackageCollection()
        def pName = new PackageName('a')
        def file = new SourceFile(packageName: pName)
        collection.addSourceFile(file)
        assert collection.packages.size() == 1
        assert collection.packages[0].packageName == pName
        assert collection.packages[0].sourceFiles == [file]
    }

    @Test
    void addTwoSourceFilesSamePackage() {
        def collection = new PackageCollection()
        def pName = new PackageName('a')
        def file1 = new SourceFile(packageName: pName)
        def file2 = new SourceFile(packageName: pName)
        collection.addSourceFile(file1)
        collection.addSourceFile(file2)
        assert collection.packages.size() == 1
        assert collection.packages[0].packageName == pName
        assert collection.packages[0].sourceFiles == [file1, file2]
    }

    @Test
    void addTwoSourceFilesDifferentPackages() {
        def collection = new PackageCollection()
        def pName1 = new PackageName('a')
        def pName2 = new PackageName('b')
        def file1 = new SourceFile(packageName: pName1)
        def file2 = new SourceFile(packageName: pName2)
        collection.addSourceFile(file1)
        collection.addSourceFile(file2)
        assert collection.packages.size() == 2
        assert collection.packages[0].packageName == pName1
        assert collection.packages[0].sourceFiles == [file1]
        assert collection.packages[1].packageName == pName2
        assert collection.packages[1].sourceFiles == [file2]
    }
}
