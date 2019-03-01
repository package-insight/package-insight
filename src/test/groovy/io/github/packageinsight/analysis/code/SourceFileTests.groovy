package io.github.packageinsight.analysis.code

import org.junit.Test

import static groovy.test.GroovyAssert.shouldFail
import static io.github.packageinsight.analysis.ResourceLoader.resourceLines

class SourceFileTests {

    @Test
    void readAJavaFile() {
        def file = SourceFile.fromLines("JavaExample_java.txt", resourceLines('sampleFiles/JavaExample_java.txt'))
        assert new PackageName(name: 'io.github.example') == file.packageName
        assert file.imports == [
                new ImportLine(packageName: new PackageName('java.io'), originalImport: 'import java.io.BufferedReader;', lineNo: 6),
                new ImportLine(packageName: new PackageName('java.io'), originalImport: 'import java.io.Reader;', lineNo: 7),
                new ImportLine(packageName: new PackageName('java.awt'), originalImport: 'import java.awt.*;', lineNo: 8),
                new ImportLine(packageName: new PackageName('java.beans'), originalImport: 'import static java.beans.Beans.getInstanceOf;', lineNo: 10)
        ]
        assert file.importPackages ==
                [new PackageName('java.io'),
                 new PackageName('java.awt'),
                 new PackageName('java.beans')] as Set
        assert file.fileName == "JavaExample_java.txt"
    }

    @Test
    void readAKotlinJavaFile() {
        def file = SourceFile.fromLines("KotlinExample_kt.txt", resourceLines('sampleFiles/KotlinExample_kt.txt'))
        assert new PackageName(name: 'com.example.kotlin') == file.packageName
        assert file.imports == [
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
        ]
        assert file.importPackages ==
                [new PackageName('java.util.zip'),
                 new PackageName('java.util'),
                 new PackageName('java.io'),
                 new PackageName('java.nio.file'),
                 new PackageName('java.nio.file.attribute')] as Set
        assert file.fileName == "KotlinExample_kt.txt"
    }

    @Test
    void immutable() {
        def sourceFile = SourceFile.fromLines("JavaExample_java", resourceLines('sampleFiles/JavaExample_java.txt'))
        shouldFail {
            //noinspection GrFinalVariableAccess, GroovyAccessibility
            sourceFile.packageName = null
        }
        shouldFail {
            sourceFile.importPackages.add(new PackageName('a'))
        }
        shouldFail {
            sourceFile.imports.add(new ImportLine())
        }
    }
}
