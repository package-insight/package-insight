package io.github.westonal.analysis

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

    private static Collection<ImportLine> findImports(String[] strings) {
        def importLines = new LinkedList<ImportLine>()
        for (int i = 0; i < strings.length; i++) {
            def line = ImportLine.fromLine(i + 1, strings[i])
            if (line != null) importLines.add(line)
        }
        importLines
    }

    private static PackageName findPackage(String[] s) {
        new PackageName(name: ImportLine.extractFromLine(s.find { it.startsWith('package') }))
    }
}
