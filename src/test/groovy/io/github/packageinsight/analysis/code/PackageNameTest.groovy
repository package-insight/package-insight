package io.github.packageinsight.analysis.code

import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail

class PackageNameTest {

    @Test
    void assertEqualityContract_Equal() {
        assert new PackageName(name: 'A') == new PackageName(name: 'A')
    }

    @Test
    void assertEqualityContract_NotEqual() {
        assert new PackageName(name: 'A') != new PackageName(name: 'B')
    }

    @Test
    void immutable() {
        def name = new PackageName(name: 'A')
        shouldFail {
            //noinspection GrFinalVariableAccess, GroovyAccessibility
            name.name = 'B'
        }
    }

    @Test
    void toStringFormat() {
        assert new PackageName(name: 'a').toString() == 'a'
    }
}
