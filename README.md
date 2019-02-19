[![Build Status](https://travis-ci.org/westonal/package-insight.svg?branch=master)](https://travis-ci.org/westonal/package-insight)

```
buildscript {
    repositories {
        maven { url 'https://jitpack.io' }
    }
    dependencies {
        classpath 'com.github.package-insight:package-insight:plugin-test-SNAPSHOT@jar'
    }
}

apply plugin: 'insight'
```
or:
```
allprojects {
   apply plugin: 'insight'

}
```
