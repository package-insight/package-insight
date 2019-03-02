package io.github.packageinsight.analysis.code

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable

import java.util.regex.Matcher

@Immutable
@EqualsAndHashCode
class ImportLine {
    PackageName packageName
    String originalImport
    int lineNo

    static ImportLine fromLine(int lineNo, String line) {
        def importPackage = extractFromLine(line)
        if (importPackage == null) return null
        new ImportLine(
                packageName: new PackageName(name: importPackage),
                lineNo: lineNo,
                originalImport: line
        )
    }

    private static String extractFromLine(String s) {
        // https://regex101.com/r/Nj45v9/4
        Matcher group = (s =~ /^\s*(import|(typealias\s+[^\s]*\s*=))\s*(static)?\s*([a-z0-9.]+)\./)
        if (group.size() == 1) {
            return group[0][4]
        }
        return null
    }

    static String extractPackageFromLine(String s) {
        // https://regex101.com/r/zd0dsk/2
        Matcher group = (s =~ /^\s*(package)\s*([a-z0-9.]+)/)
        if (group.size() == 1) {
            return group[0][2]
        }
        return null
    }
}
