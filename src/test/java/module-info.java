module test.com.brinvex.java {
    requires com.brinvex.java;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.engine;
    requires org.mockito;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;
    requires java.net.http;
    opens test.com.brinvex.java to org.junit.platform.commons,org.mockito;
}