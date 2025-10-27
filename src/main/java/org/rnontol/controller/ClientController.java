package org.rnontol.controller;


import io.quarkus.logging.Log;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.rnontol.entity.Client;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;

import java.net.URI;
import java.util.List;

@Path("/api/clients")
public class ClientController {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Counted(value = "clients.create.count", description = "Numero de veces que se llama al endpoint POST /clientes/")
    @Timed(value = "clients.create.time", description = "Tiempo que toma ejecutar el endpoint POST /clientes/")
    public Response addClient(Client client) {
        Client.persist(client);
        return Response.created(URI.create("/api/clients/"+client.id)).build();//redirecciona
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(value = "clients.get_all.count", description = "Número de llamadas al endpoint GET /clients")
    @Timed(value = "clients.get_all.time", description = "Tiempo de ejecución del endpoint GET /clients")
    public List<Client> getClients() {
        return Client.listAll();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(value = "clients.get_by_id.count", description = "Número de llamadas al endpoint GET /clients/{id}")
    @Timed(value = "clients.get_by_id.time", description = "Tiempo de ejecución del endpoint GET /clients/{id}")
    public Client getClient(@PathParam("id") long id) {
        return Client.findById(id);
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Counted(value = "clients.update.count", description = "Número de llamadas al endpoint PUT /clients/{id}")
    @Timed(value = "clients.update.time", description = "Tiempo de ejecución del endpoint PUT /clients/{id}")
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
    @Counted(value = "clients.delete.count", description = "Número de llamadas al endpoint DELETE /clients/{id}")
    @Timed(value = "clients.delete.time", description = "Tiempo de ejecución del endpoint DELETE /clients/{id}")
    public void deleteClient(@PathParam("id") long id) {
        Client entity = Client.findById(id);
        if (entity == null) {
            throw new NotFoundException("Client not found with id: " + id);
        }

        Client.deleteById(entity.id);
    }


}