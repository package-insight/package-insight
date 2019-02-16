package io.github.westonal.analysis

import org.junit.Test

import static org.junit.Assert.*

class PackageNameTest {

    @Test
    void assertEqualityContract_Equal() {
        assertEquals(new PackageName(name: "A"), new PackageName(name: "A"))
    }

    @Test
    void assertEqualityContract_NotEqual() {
        assertNotEquals(new PackageName(name: "A"), new PackageName(name: "B"))
    }
}