package io.github.westonal.analysis

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable

@Immutable
@EqualsAndHashCode
class PackageName {
    String name

    @Override
    String toString() {
        name
    }
}
