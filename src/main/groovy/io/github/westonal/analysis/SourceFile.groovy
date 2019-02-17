package io.github.westonal.analysis

import groovy.transform.Immutable

@Immutable
class SourceFile {
    PackageName packageName
    List<ImportLine> imports
    Set<PackageName> importPackages

    static SourceFile fromLines(String[] s) {
        fromImports(findPackage(s), findImports(s))
    }

    static SourceFile fromImports(PackageName packageName, ImportLine[] imports) {
        new SourceFile(
                packageName: packageName,
                imports: imports,
                importPackages: imports*.packageName as Set
        )
    }

    private static ImportLine[] findImports(String[] strings) {
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
