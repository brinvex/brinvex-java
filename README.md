# Brinvex Java

_Brinvex Java_ is a micro-library containing various helper utilities for Java.

### Maven and JPMS Setup
````
<properties>
    <brinvex-java.version>1.40.11</brinvex-java.version>
</properties>    

<repository>
    <id>github-pubrepo-brinvex</id>
    <name>Github Public Repository - Brinvex</name>
    <url>https://github.com/brinvex/brinvex-pubrepo/raw/main/</url>
    <snapshots>
        <enabled>false</enabled>
    </snapshots>
</repository>

<dependency>
    <groupId>com.brinvex</groupId>
    <artifactId>brinvex-java</artifactId>
    <version>${brinvex-java.version}</version>
</dependency>
````
The library supports _JPMS_ and exports the module named ````com.brinvex.java````.

### Requirements

Java 21 or above

### License

The _Brinvex Java_ is released under version 2.0 of the Apache License.
