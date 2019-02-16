package io.github.westonal.analysis

import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail
import static io.github.westonal.analysis.ResourceLoader.resourceLines
import static junit.framework.Assert.assertEquals
import static org.junit.Assert.assertArrayEquals

class SourceFileTests {

    @Test
    void readAJavaFile() {
        def file = SourceFile.fromLines(resourceLines('sampleFiles/JavaExample_java.txt'))
        assertEquals(file.packageName, new PackageName(name: 'io.github.example'))
        assertArrayEquals(file.imports,
                new ImportLine(packageName: new PackageName('java.io'), originalImport: 'import java.io.BufferedReader;', lineNo: 6),
                new ImportLine(packageName: new PackageName('java.io'), originalImport: 'import java.io.Reader;', lineNo: 7),
                new ImportLine(packageName: new PackageName('java.awt'), originalImport: 'import java.awt.*;', lineNo: 8),
                new ImportLine(packageName: new PackageName('java.beans'), originalImport: 'import static java.beans.Beans.getInstanceOf;', lineNo: 10)
        )
        assertEquals(file.importPackages,
                set([new PackageName('java.io'),
                     new PackageName('java.awt'),
                     new PackageName('java.beans')])
        )
    }

    @Test
    void readAKotlinJavaFile() {
        def file = SourceFile.fromLines(resourceLines('sampleFiles/KotlinExample_kt.txt'))
        assertEquals(file.packageName, new PackageName(name: 'com.example.kotlin'))
        assertArrayEquals(file.imports,
                new ImportLine(packageName: new PackageName('java.util.zip'), originalImport: 'import java.util.zip.ZipFile', lineNo: 3),
                new ImportLine(packageName: new PackageName('java.util'), originalImport: 'import java.util.*', lineNo: 4),
                new ImportLine(packageName: new PackageName('java.util.zip'), originalImport: 'import java.util.zip.ZipEntry', lineNo: 5),
                new ImportLine(packageName: new PackageName('java.io'), originalImport: 'import java.io.FileOutputStream', lineNo: 6),
                new ImportLine(packageName: new PackageName('java.io'), originalImport: 'import java.io.File', lineNo: 7),
                new ImportLine(packageName: new PackageName('java.io'), originalImport: 'import java.io.IOException', lineNo: 8),
                new ImportLine(packageName: new PackageName('java.nio.file'), originalImport: 'import java.nio.file.Files', lineNo: 9),
                new ImportLine(packageName: new PackageName('java.nio.file.attribute'), originalImport: 'import java.nio.file.attribute.FileTime', lineNo: 10),
                new ImportLine(packageName: new PackageName('java.nio.file.attribute'), originalImport: 'import java.nio.file.attribute.BasicFileAttributeView', lineNo: 11),
                new ImportLine(packageName: new PackageName('java.nio.file'), originalImport: 'import java.nio.file.Files.*', lineNo: 12),
        )
        assertEquals(file.importPackages,
                set([new PackageName('java.util.zip'),
                     new PackageName('java.util'),
                     new PackageName('java.io'),
                     new PackageName('java.nio.file'),
                     new PackageName('java.nio.file.attribute')])
        )
    }

    @Test
    void immutable() {
        def sourceFile = SourceFile.fromLines(resourceLines('sampleFiles/JavaExample_java.txt'))
        shouldFail {
            //noinspection GrFinalVariableAccess, GroovyAccessibility
            sourceFile.packageName = null
        }
    }

    static <T> Set<T> set(Collection<T> collection) {
        def set = new HashSet<T>()
        set.addAll(collection)
        set
    }
}
