package io.github.westonal.analysis

import org.junit.Test

import static org.junit.Assert.*

class ImportLineTest {

    @Test
    void assertEqualityContract_Equal() {
        assertEquals(
                new ImportLine(packageName: new PackageName(name: 'A')),
                new ImportLine(packageName: new PackageName(name: 'A'))
        )
    }

    @Test
    void assertEqualityContract_NotEqual() {
        assertNotEquals(
                new ImportLine(packageName: new PackageName(name: 'A')),
                new ImportLine(packageName: new PackageName(name: 'B'))
        )
    }

    @Test
    void fromJavaLine() {
        assertEquals(
                new ImportLine(
                        packageName: new PackageName(name: 'com.example'),
                        lineNo: 10,
                        originalImport: 'import com.example.Class;'
                ),
                ImportLine.fromLine(10, 'import com.example.Class;')
        )
    }

    @Test
    void fromStaticJavaLine() {
        assertEquals(
                new ImportLine(
                        packageName: new PackageName(name: 'com.example.package'),
                        lineNo: 11,
                        originalImport: 'import static com.example.package.Class.method;'
                ),
                ImportLine.fromLine(11, 'import static com.example.package.Class.method;')
        )
    }

    @Test
    void fromKotlinLine() {
        assertEquals(
                new ImportLine(
                        packageName: new PackageName(name: 'com.example'),
                        lineNo: 12,
                        originalImport: 'import com.example.Class'
                ),
                ImportLine.fromLine(12, 'import com.example.Class')
        )
    }

    @Test
    void fromKotlinStaticLine() {
        assertEquals(
                new ImportLine(
                        packageName: new PackageName(name: 'org.junit'),
                        lineNo: 13,
                        originalImport: 'import org.junit.Assert.assertEquals'
                ),
                ImportLine.fromLine(13, 'import org.junit.Assert.assertEquals')
        )
    }

    @Test
    void notAnImportLine() {
        assertNull(
                ImportLine.fromLine(14, '// import static com.example.package.Class.method;')
        )
    }

    @Test
    void leadingSpaceDoesNotMatter() {
        assertEquals(
                new ImportLine(
                        packageName: new PackageName(name: 'com.example'),
                        lineNo: 15,
                        originalImport: ' import com.example.Class;'
                ),
                ImportLine.fromLine(15, ' import com.example.Class;')
        )
    }

    @Test
    void internalSpacesDoNotMatter() {
        assertEquals(
                new ImportLine(
                        packageName: new PackageName(name: 'com.example'),
                        lineNo: 15,
                        originalImport: 'import  static  com.example.Class  ;  '
                ),
                ImportLine.fromLine(15, 'import  static  com.example.Class  ;  ')
        )
    }
}
