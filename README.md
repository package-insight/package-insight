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
}
```

### listPackages

Lists all packages in the source set and the number of dependencies they have. This will highlight packages with zero
dependencies last as potential candidates for moving to other modules.

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
