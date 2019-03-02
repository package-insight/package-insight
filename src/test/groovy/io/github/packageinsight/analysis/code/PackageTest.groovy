package io.github.packageinsight.analysis.code

import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail

class PackageTest {

    @Test
    void create() {
        def p = new Package(new PackageName('a'))
        assert p.packageName == new PackageName('a')
    }

    @Test
    void addSourceFile() {
        def p = new Package(new PackageName('a'))
        def file = new SourceFile(packageName: new PackageName('a'))
        p.addSourceFile(file)
        assert p.sourceFiles == [file] as SourceFile[]
    }

    @Test
    void addSourceFileWithWrongPackage() {
        def p = new Package(new PackageName('a'))
        def file = new SourceFile(packageName: new PackageName('b'))
        shouldFail {
            p.addSourceFile(file)
        }
    }

    @Test
    void cantAddDirect() {
        def p = new Package(new PackageName('a'))
        def file = new SourceFile()
        shouldFail {
            p.sourceFiles.add(file)
        }
    }

    @Test
    void dependsOn() {
        def pName = new PackageName('a')
        def lines = [ImportLine.fromLine(1, 'import b.*')]
        def p = new Package(pName)
        p.addSourceFile(SourceFile.fromImports('file', pName, lines))
        assert p.dependsOn == [new PackageName('b')] as Set
    }

    @Test
    void dependsOn2FromOneFile() {
        def pName = new PackageName('a')
        def lines = [
                ImportLine.fromLine(1, 'import b.*'),
                ImportLine.fromLine(2, 'import c.*')
        ]
        def p = new Package(pName)
        p.addSourceFile(SourceFile.fromImports('file', pName, lines))
        assert p.dependsOn*.name as Set == ['b', 'c'] as Set
    }

    @Test
    void dependsOn2FromTwoFiles() {
        def pName = new PackageName('a')
        def lines1 = [
                ImportLine.fromLine(1, 'import b.*')
        ]
        def lines2 = [
                ImportLine.fromLine(1, 'import c.*')
        ]
        def p = new Package(pName)
        p.addSourceFile(SourceFile.fromImports('file1', pName, lines1))
        p.addSourceFile(SourceFile.fromImports('file2', pName, lines2))
        assert p.dependsOn*.name as Set == ['b', 'c'] as Set
    }

    @Test
    void dependsOn3FromTwoFilesIncludingDuplicates() {
        def pName = new PackageName('a')
        def lines1 = [
                ImportLine.fromLine(1, 'import b.*'),
                ImportLine.fromLine(2, 'import d.*')
        ]
        def lines2 = [
                ImportLine.fromLine(1, 'import c.*'),
                ImportLine.fromLine(2, 'import d.*')
        ]
        def p = new Package(pName)
        p.addSourceFile(SourceFile.fromImports('file1', pName, lines1))
        p.addSourceFile(SourceFile.fromImports('file2', pName, lines2))
        assert p.dependsOn*.name as Set == ['b', 'c', 'd'] as Set
    }
}
