package com.targa.labs.quarkushop.order.client;

import com.targa.labs.quarkushop.commons.dto.ProductDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/products")
@RegisterRestClient
public interface ProductRestClient {

    @GET
    @Path("/{id}")
    ProductDto findById(@PathParam Long id);
}
