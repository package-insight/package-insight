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
        if (!trimmed.startsWith('import')) return null
        new ImportLine(
                packageName: new PackageName(name: extractFromLine(trimmed)),
                lineNo: lineNo,
                originalImport: line
        )
    }

    static String extractFromLine(String s) {
        def split = s.split(/\s+/)
        if (split[1] == 'static') {
            trimLast(trimLast(split[2]))
        } else {
            trimLast(split[1])
        }
    }

    static String trimLast(String s) {
        s.split(/\./).takeWhile { allLower(it) }.join('.')
    }

    static def allLower(String s) {
        s == s.toLowerCase(Locale.US)
    }
}
