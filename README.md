# Brinvex Java

_Brinvex Java_ is a micro-library containing various helper utilities for Java.

### Maven and JPMS Setup
````
<properties>
    <brinvex-java.version>1.0.3</brinvex-java.version>
</properties>    

<repository>
    <id>brinvex-repo</id>
    <name>Brinvex Repository</name>
    <url>https://github.com/brinvex/brinvex-repo/raw/main/</url>
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
Java 17 or above

### License

The _Brinvex Java_ is released under version 2.0 of the Apache License.
