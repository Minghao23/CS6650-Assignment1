package com.hmh.test;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/hello")
public class Hello {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello() {
        return "Hello World!";
    }

    @POST
    public Response postText(@FormParam("content") String content) {
        return Response.status(200).entity(content).build();
    }
}