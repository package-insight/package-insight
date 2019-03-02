package io.github.packageinsight.analysis.code

import groovy.transform.Immutable

@Immutable
class SourceFile {
    String fileName
    PackageName packageName
    List<ImportLine> imports
    Set<PackageName> importPackages

    static SourceFile fromLines(String fileName, String[] s) {
        fromImports(fileName, findPackage(s), findImports(s))
    }

    static SourceFile fromImports(String fileName, PackageName packageName, Collection<ImportLine> imports) {
        new SourceFile(
                fileName: fileName,
                packageName: packageName,
                imports: imports,
                importPackages: imports*.packageName as Set
        )
    }

    private static Collection<ImportLine> findImports(String[] allLines) {
        def importLines = new LinkedList<ImportLine>()
        for (int i = 0; i < allLines.length; i++) {
            def line = ImportLine.fromLine(i + 1, allLines[i])
            if (line != null) importLines.add(line)
        }
        importLines
    }

    private static PackageName findPackage(String[] allLines) {
        for (line in allLines) {
            def packageName = ImportLine.extractPackageFromLine(line)
            if (packageName != null) return new PackageName(name: packageName)
        }
        return null
    }
}
