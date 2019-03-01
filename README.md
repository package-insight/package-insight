[![Build Status](https://travis-ci.org/package-insight/package-insight.svg?branch=master)](https://travis-ci.org/package-insight/package-insight)

```
buildscript {
    repositories {
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.github.package-insight:package-insight:master-SNAPSHOT@jar'
    }
}

apply plugin: 'package-insight'
```
or:
```
allprojects {
   apply plugin: 'package-insight'
}
```

## Options

```
packageInsight {
    listPackages true
    stronglyConnectedComponentLimit 1
    printPackagesNotInScc true
}
```

### listPackages

Lists all packages in the source set and the number of dependencies they have. This will highlight packages with zero
dependencies last as potential candidates for moving to other modules.

### stronglyConnectedComponentLimit

This identifies Circular Package References ([strongly connected components](https://en.wikipedia.org/wiki/Strongly_connected_component) on the graph of package dependencies), i.e. groups of packages that are interdependent. It will tell you about all of them and the task fails if exceeds the specified size.

### printPackagesNotInScc

Lists packages that were not identified in Circular Package References (Strongly Connected Components). 

## Running
```
./gradlew insight<SourceSet>
```
e.g.
```
./gradlew insightMain
```
Or simply:
```
./gradlew insight
```
