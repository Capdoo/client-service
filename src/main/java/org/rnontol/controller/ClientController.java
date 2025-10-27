package org.rnontol.controller;


import io.quarkus.logging.Log;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.rnontol.entity.Client;

import java.net.URI;
import java.util.List;

@Path("/api/clients")
public class ClientController {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addClient(Client client) {
        Client.persist(client);
        return Response.created(URI.create("/api/clients/"+client.id)).build();//redirecciona
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Client> getClients() {
        return Client.listAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Client getClient(@PathParam("id") long id) {
        return Client.findById(id);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Client updateClient(@PathParam("id") long id, Client client) {
        Client entity = Client.findById(id);
        if (entity == null) {
            throw new NotFoundException();
        }

        int age = client.age>0? client.age : entity.age;

        String firstName = client.firstName.isEmpty()? entity.firstName : client.firstName;

        entity.firstName = firstName;
        entity.age = age;

        Client.persist(entity);

        return entity;
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public void deleteClient(@PathParam("id") long id) {
        Client entity = Client.findById(id);
        if (entity == null) {
            throw new NotFoundException("Client not found with id: " + id);
        }

        Client.deleteById(entity.id);
    }


}