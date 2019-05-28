# What Lies Beneath
Code samples and benchmarks for the "What Lies Beneath" talk.

## Build
JDK 11 or later is required to build the code. Make sure that your `JAVA_HOME` points to proper JDK release and the run the following command:
```
./gradlew shadowJar
```
This will produce a single jar file containing benchmarks, i.e. `build/libs/benchmarks.jar`.

## Run
First build the benchmark jars as described above and then execute them using the following command:
```
java -jar build/libs/benchmarks.jar <benchmark params>
```
For example this is how to run benchmarks with inlining disabled:
```
java -jar build/libs/benchmarks.jar -jvmArgs '-XX:-Inline'
```
And this is an example ofn running in the interpreter:
```
java -jar build/libs/benchmarks.jar -jvmArgs '-Xint'
```
