package com.targa.labs.quarkushop.customer.client;

import com.targa.labs.quarkushop.commons.dto.OrderDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Optional;

@Path("/orders")
@RegisterRestClient
public interface OrderRestClient {

    @GET
    @Path("/{id}")
    Optional<OrderDto> findById(@PathParam Long id);

    @GET
    @Path("/payment/{id}")
    Optional<OrderDto> findByPaymentId(Long id);

    @POST
    OrderDto save(OrderDto order);
}
