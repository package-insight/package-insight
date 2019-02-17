package io.github.westonal.analysis

import groovy.transform.Immutable

@Immutable
class SourceFile {
    PackageName packageName
    List<ImportLine> imports
    Set<PackageName> importPackages

    static SourceFile fromLines(String[] s) {
        def imports = findImports(s)
        new SourceFile(
                packageName: findPackage(s),
                imports: imports,
                importPackages: toPackageSet(imports)
        )
    }

    private static Set<PackageName> toPackageSet(ImportLine[] importLines) {
        def names = new HashSet<PackageName>()
        names.addAll(importLines*.packageName)
        names
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
