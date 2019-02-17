package io.github.westonal.analysis

import org.junit.Test

class PackageTest {

    @Test
    void create() {
        def p = new Package(new PackageName('a'))
        assert p.packageName == new PackageName('a')
    }

    @Test
    void addSourceFile() {
        def p = new Package(new PackageName('a'))
        def file = new SourceFile()
        p.addSourceFile(file)
        assert p.sourceFiles == [file]
    }
}
