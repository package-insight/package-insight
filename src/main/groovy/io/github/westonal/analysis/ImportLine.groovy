package io.github.westonal.analysis

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable

@Immutable
@EqualsAndHashCode
class ImportLine {
    PackageName packageName
    String originalImport
    int lineNo

    static ImportLine fromLine(int lineNo, String line) {
        def trimmed = line.trim()
        if (!trimmed.startsWith('import ')) return null
        new ImportLine(
                packageName: new PackageName(name: extractFromLine(trimmed)),
                lineNo: lineNo,
                originalImport: line
        )
    }

    static String extractFromLine(String s) {
        try {
            if (s.endsWith(';')) s = s.substring(0, s.length() - 1)
            def split = s.split(/\s+/)
            if (split[1] == 'static') {
                trimLast(trimLast(split[2]))
            } else {
                trimLast(split[1])
            }
        }
        catch (Exception e) {
            println "Error with line: " + s
            throw e
        }
    }

    static String trimLast(String s) {
        s.split(/\./).takeWhile { allLowerNonStar(it) }.join('.')
    }

    static def allLowerNonStar(String s) {
        s != '*' && s == s.toLowerCase(Locale.US)
    }
}
