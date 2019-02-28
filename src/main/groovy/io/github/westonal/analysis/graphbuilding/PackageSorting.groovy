package io.github.westonal.analysis.graphbuilding

import io.github.westonal.analysis.PackageName

class PackageSorting {

    final static Comparator<PackageName> byName = Comparator.comparing({ PackageName packageName -> packageName.name })
}
