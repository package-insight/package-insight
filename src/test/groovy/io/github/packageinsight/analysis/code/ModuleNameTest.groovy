package io.github.packageinsight.analysis.code

import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail

class ModuleNameTest {

    @Test
    void assertEqualityContract_Equal() {
        assert new ModuleName(name: 'A') == new ModuleName(name: 'A')
    }

    @Test
    void assertEqualityContract_NotEqual() {
        assert new ModuleName(name: 'A') != new ModuleName(name: 'B')
    }

    @Test
    void immutable() {
        def name = new ModuleName(name: 'A')
        shouldFail {
            //noinspection GrFinalVariableAccess, GroovyAccessibility
            name.name = 'B'
        }
    }
}
