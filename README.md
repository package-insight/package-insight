[![Build Status](https://travis-ci.org/package-insight/package-insight.svg?branch=master)](https://travis-ci.org/package-insight/package-insight)

```
buildscript {
    repositories {
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.github.package-insight:package-insight:plugin-test-SNAPSHOT@jar'
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
    circularDependency = 'Warning' // or 'Disabled' or 'Error'
    listPackages = true
}
```

### circularDependency

Detect and display _some_ of the circular dependencies in the module.
It will also give an indication of some of the hot paths, dependencies most frequently seen in circular dependencies.
N.B. listing all the circular references is not currently supported, so hot paths and counts are not entirely accurate.
However, it is accurate in detecting zero dependencies. Turning error at the start of work on a module and linking to check:
```
check.dependsOn insight
```

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
