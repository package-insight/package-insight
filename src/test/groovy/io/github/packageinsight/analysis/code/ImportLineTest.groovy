package io.github.packageinsight.analysis.code

import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail

class ImportLineTest {

    @Test
    void assertEqualityContract_Equal() {
        assert new ImportLine(packageName: new PackageName(name: 'A')) ==
                new ImportLine(packageName: new PackageName(name: 'A'))
    }

    @Test
    void assertEqualityContract_NotEqual() {
        assert new ImportLine(packageName: new PackageName(name: 'A')) !=
                new ImportLine(packageName: new PackageName(name: 'B'))
    }

    @Test
    void immutable() {
        shouldFail {
            //noinspection GrFinalVariableAccess, GroovyAccessibility
            new ImportLine(packageName: new PackageName(name: 'B')).lineNo = 10
        }
    }

    @Test
    void fromJavaLine() {
        assert new ImportLine(
                packageName: new PackageName(name: 'com.example'),
                lineNo: 10,
                originalImport: 'import com.example.Class;'
        ) == ImportLine.fromLine(10, 'import com.example.Class;')
    }

    @Test
    void fromStaticJavaLine() {
        assert new ImportLine(
                packageName: new PackageName(name: 'com.example.package'),
                lineNo: 11,
                originalImport: 'import static com.example.package.Class.method;'
        ) == ImportLine.fromLine(11, 'import static com.example.package.Class.method;')
    }

    @Test
    void fromKotlinLine() {
        assert new ImportLine(
                packageName: new PackageName(name: 'com.example'),
                lineNo: 12,
                originalImport: 'import com.example.Class'
        ) == ImportLine.fromLine(12, 'import com.example.Class')
    }

    @Test
    void fromKotlinStaticLine() {
        assert new ImportLine(
                packageName: new PackageName(name: 'org.junit'),
                lineNo: 13,
                originalImport: 'import org.junit.Assert.assertEquals'
        ) == ImportLine.fromLine(13, 'import org.junit.Assert.assertEquals')
    }

    @Test
    void notAnImportLine() {
        assert ImportLine.fromLine(14, '// import static com.example.package.Class.method;') == null
    }

    @Test
    void notAnImportLine_2() {
        assert ImportLine.fromLine(14, 'importService.something();') == null
    }

    @Test
    void leadingSpaceDoesNotMatter() {
        assert new ImportLine(
                packageName: new PackageName(name: 'com.example'),
                lineNo: 15,
                originalImport: '  import com.example.Class;'
        ) == ImportLine.fromLine(15, '  import com.example.Class;')
    }

    @Test
    void internalSpacesDoNotMatter() {
        assert new ImportLine(
                packageName: new PackageName(name: 'com.example'),
                lineNo: 15,
                originalImport: 'import  static  com.example.Class  ;  '
        ) == ImportLine.fromLine(15, 'import  static  com.example.Class  ;  ')
    }

    @Test
    void fromStarImportJavaLine() {
        assert new ImportLine(
                packageName: new PackageName(name: 'com.example'),
                lineNo: 10,
                originalImport: 'import com.example.*;'
        ) == ImportLine.fromLine(10, 'import com.example.*;')
    }

    @Test
    void fromStarImportKotlinLine() {
        assert new ImportLine(
                packageName: new PackageName(name: 'com.example'),
                lineNo: 10,
                originalImport: 'import com.example.*'
        ) == ImportLine.fromLine(10, 'import com.example.*')
    }

    @Test
    void fromKotlinStaticTopLevelFunction() {
        assert new ImportLine(
                packageName: new PackageName(name: 'a.package.with.top.level'),
                lineNo: 13,
                originalImport: 'import a.package.with.top.level.function'
        ) == ImportLine.fromLine(13, 'import a.package.with.top.level.function')
    }

    @Test
    void fromKotlinTypealias() {
        assert new ImportLine(
                packageName: new PackageName(name: 'com.package'),
                lineNo: 14,
                originalImport: 'typealias BInner = com.package.B.Inner'
        ) == ImportLine.fromLine(14, 'typealias BInner = com.package.B.Inner')
    }

    @Test
    void nullFromKotlinTypealiasWithoutPackage() {
        assert ImportLine.fromLine(14, 'typealias Predicate<T> = (T) -> Boolean') == null
        assert ImportLine.fromLine(14, 'typealias Predicate = () -> Boolean') == null
    }

    @Test
    void extractPackageFromLine() {
        assert ImportLine.extractPackageFromLine('package com.example;') == 'com.example'
        assert ImportLine.extractPackageFromLine('package com.example1') == 'com.example1'
        assert ImportLine.extractPackageFromLine('package  com.example2') == 'com.example2'
        assert ImportLine.extractPackageFromLine('  package  com.example3') == 'com.example3'
    }
}
