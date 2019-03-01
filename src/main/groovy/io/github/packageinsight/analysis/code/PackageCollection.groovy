package io.github.packageinsight.analysis.code

class PackageCollection {
    final Map<PackageName, Package> packageMap = new HashMap<>()

    def addSourceFile(SourceFile sourceFile) {
        def p = packageMap.computeIfAbsent(sourceFile.packageName, { name -> new Package(name) })
        p.addSourceFile(sourceFile)
        p
    }

    Collection<Package> getPackages() { packageMap.values() }
}
