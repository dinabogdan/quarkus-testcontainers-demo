package org.acme.resteasy.api;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acme.resteasy.domain.model.Fruit;

@Path("/fruits")
public class FruitsResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public List<Fruit> fruits() {
        return Arrays.asList(Fruit.apple(), Fruit.banana(), Fruit.orange());
    }
}