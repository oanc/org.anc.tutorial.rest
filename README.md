REST Services Tutorial
==================

This is a short tutorial on creating REST services using JAX-RS and the ANC applications stack.

## Prerequisites

It is assumed you have the following installed and configured.

1. Java 1.7 or later
1. Maven 3.x with the ANC’s setting.xml file
1. Groovy 2.x
1. A suitable IDE. I recommend the community edition of [IntelliJ](http://www.jetbrains.com/idea/download/), but Eclipse and Netbeans will work as well.  However, these instructions assume IntelliJ and users of other IDEs will have to figure out the equivalent commands on their own.
1. A Tomcat server installed (optional)

## Create the project

Select `File -> New Project...`, select a *Maven* project type and click the *Next* button.

Select a *Maven* project and click the *Next* button.

## Add the Groovy framework

Right click the project in the *Project* view and select *Add Framework Support...`. Select *Groovy* and click the
*Ok* button.

## Project layout

Add the following directories to the project:
1. src/main/groovy<br/>
Mark this directory as a *Sources Root*<br/>
Right click on the directory and select `Mark Directory As -> Sources Root`
1. src/test/groovy<br/>
Mark this directory as a *Test Sources Root*
1. src/main/webapp/WEB-INF

## Edit the pom.xml

### Set the parent pom
```xml
<parent>
    <groupId>org.anc.maven</groupId>
    <artifactId>groovy-war-parent-pom</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</parent>
```

### Set the packaging type
```xml
<packaging>war</packaging>
```

### Add the Jetty Plugin

The Jetty plugin provides a small, standalone web server that can be used to run a web service during
testing and development.  The plugin is declared in the `<build>` section of the pom.
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.mortbay.jetty</groupId>
            <artifactId>jetty-maven-plugin</artifactId>
            <version>8.1.11.v20130520</version>
        </plugin>
    <plugins>
</build>
```
	
### Create a Groovy class

1. Create a new package in *src/main/groovy* named `org.anc.tutorial.rest`
1. Create a new Groovy class in the above package named `SimpleService`
1.Add a greet method to the SimpleService class
```java
class SimpleService {
	String greet(String who) {
		return “Hello $who”
	}
}
```

Create a new package in src/test/groovy named org.anc.tutorial.rest
Create a new class in the above package named SimpleServiceTest

import org.junit.*
import static org.junit.Assert.*

class SimpleServiceTest {
	@Test
	void testGreet() {
		assertTrue ‘Hello world’ == new SimpleService().greet(“world”)
	}
}

Run the test and fix any typos until the test passes.

Make SimpleService a web service:

Create the file src/main/webapp/WEB-INF/web.xml

<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd" id="WebApp_ID" version="3.0">
    <display-name>Tutorial Services</display-name>

    <servlet>
        <servlet-name>JerseyServlet</servlet-name>
        <servlet-class>com.sun.jersey.spi.container.servlet.ServletContainer</servlet-class>
        <init-param>
            <param-name>com.sun.jersey.config.property.packages</param-name>
            <param-value>org.anc.tutorial.service</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>JerseyServlet</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>
</web-app>

Annotate the SimpleService class

	- add @Path to the class declaration
	- add @GET to the greet() method
	- add @QueryParam(‘who’) to the greet method’s ‘who’ parameter

@Path(‘/greet’)
class SimpleService {
	@GET
	String greet(@QueryParam(‘who’) String who) {
		return “Hello $who”
	}
}		

Modify
Add POST methods:
	- one that accepts text/html
	- one that accepts text/plain

@POST
