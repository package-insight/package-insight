package io.github.packageinsight.analysis.code

class Package {
    final PackageName packageName
    private final List<SourceFile> sourceFiles = new LinkedList<>()

    Package(PackageName packageName) {
        this.packageName = packageName
    }

    def addSourceFile(SourceFile sourceFile) {
        assert sourceFile.packageName == packageName
        sourceFiles.add(sourceFile)
    }

    Set<PackageName> getDependsOn() {
        sourceFiles.collectMany { file -> file.imports*.packageName }
    }

    SourceFile[] getSourceFiles() { sourceFiles }
}
