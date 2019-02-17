package io.github.westonal.analysis

class Package {
    final PackageName packageName
    final List<SourceFile> sourceFiles = new LinkedList<>()

    Package(PackageName packageName) {
        this.packageName = packageName
    }

    def addSourceFile(SourceFile sourceFile) {
        sourceFiles.add(sourceFile)
    }
}
