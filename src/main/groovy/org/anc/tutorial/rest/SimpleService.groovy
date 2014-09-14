package org.anc.tutorial.rest

import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import java.lang.annotation.RetentionPolicy

/**
 * @author Keith Suderman
 */
@Path('/greet')
class SimpleService {
    @GET
    Response greet(@QueryParam('who') String who) {
        respond("Hello $who")
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    Response textPosted(String text) {
        respond("Text was posted.")
    }

    @POST
    @Consumes(MediaType.TEXT_HTML)
    Response htmlPosted(String html) {
        respond("HTML was posted.")
    }

    Response respond(String content) {
        return Response.ok(content).build()
    }

    Response error(String message) {
        return Response.serverError().entity(message).build()
    }
}
