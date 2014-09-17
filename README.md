REST Services Tutorial
==================

This is a short tutorial on creating REST services using JAX-RS and the ANC application stack.

## Prerequisites

It is assumed you have the following installed and configured.

1. Java 1.7 or later
1. Maven 3.x with the ANC’s setting.xml file
1. Groovy 2.x
1. A suitable IDE. I recommend the community edition of [IntelliJ](http://www.jetbrains.com/idea/download/), but Eclipse and Netbeans will work as well.  These instructions assume IntelliJ is being used.
1. A Tomcat server installed (optional)

## Create the project

1. Select `File -> New Project...`
2. Select a *Maven* project type
3. Click the *Next* button.

## Add the Groovy framework

1. Right click on the project in the *Project* view 
2. Select *Add Framework Support...*
3. Select *Groovy* 
4. Click the *Ok* button.

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
	
## Create a Groovy class

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

## Create a Unit Test (optional)

1. Create a new package in *src/test/groovy* named `org.anc.tutorial.rest`
1. Create a new class in the above package named `SimpleServiceTest`

```java
import org.junit.*
import static org.junit.Assert.*

class SimpleServiceTest {
	@Test
	void testGreet() {
		assertTrue ‘Hello world’ == new SimpleService().greet(“world”)
	}
}
```
Run the test and fix any typos until the test passes.

## Make SimpleService a web service:

1. Create the file *src/main/webapp/WEB-INF/web.xml*
1. Paste the following into the above file.<br/>
```xml
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
```
### Annotate the SimpleService class
1. add `@Path` to the class declaration
1. add `@GET` to the `greet()` method
1. add `@QueryParam(‘who’)` to the greet method’s `who` parameter
	
```java
@Path(‘/greet’)
class SimpleService {
	@GET
	String greet(@QueryParam(‘who’) String who) {
		return “Hello $who”
	}
	
}		
```

## Test the service

Open a terminal window and run the command

    > mvn jetty:run
  
After a time you should see the message
    [INFO] Started Jetty Server
    
When you see the above message open a web browser and open http://localhost:8080/greet?who=world

You should see the message *Hello world* in your browser window.

## HTTP Response

Web service methods (i.e. methods annotated with @GET or @POST) typically need to return more information than a simple
String object. The `Response` class provides a [builder](http://en.wikipedia.org/wiki/Builder_pattern) that can be used to
construct the HTTP response that will be returned to the client.  

For the remainder of this tutorial the following methods will be used to construct `Response` objects
```java
Response respond(String message) {
	return Response.ok(message).build()
}
Response error(String message) {
	return Response.serverError().entity(message).build()
}
```
 
## Respond to POST requests

Add two methods that respond to POST requests; one method that consumes `text/plain` and another method that consumes `text/html`

```java
@POST
@Consumes(MediaType.TEXT_PLAIN)
Response handleText(String text) {
    respond 'Some text was posted\n'
}

@POST
@Consumes(MediaType.TEXT_HTML)
Response handleHtml(String html) {
    respond 'Some html was posted\n' 
}
```
 
### Testing POST Requests

Since web browsers send GET requests we will use the program *curl* from a terminal to 
send POST messages to our service. The command is:

    curl -i -X POST -H "Content-Type:text/plain" http://localhost:8080/greet
    
The above options are:

 - **-i** (optional)<br/>displays the response headers.
 - **-X POST**<br/>tells *curl* to send the request as a POST message. If omitted *curl* will send a GET request.
 - **-H "Content-Type:text/plain"**<br/>sets the content-type header in the request to *text/plain*.  The content-type
header tells the service what type of data is being sent in the request.  The service will then use this information to
determine which method should be used (content-type negotiation) to respond to the message.
    
 

