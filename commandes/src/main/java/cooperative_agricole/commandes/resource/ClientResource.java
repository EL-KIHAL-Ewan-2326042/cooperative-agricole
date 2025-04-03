package cooperative_agricole.commandes.resource;

import cooperative_agricole.commandes.model.Client;
import cooperative_agricole.commandes.service.ClientService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

@Path("/clients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClientResource {

    @Inject
    private ClientService clientService;

    @GET
    public Response getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return Response.ok(clients).build();
    }

    @GET
    @Path("/{id}")
    public Response getClientById(@PathParam("id") Integer id) {
        Client client = clientService.getClientById(id);
        if (client == null) {
            return Response.status(Status.NOT_FOUND)
                    .entity("{\"error\": \"Client non trouvé\"}").build();
        }
        return Response.ok(client).build();
    }

    @POST
    public Response createClient(Client client) {
        try {
            Client nouveauClient = clientService.createClient(client);
            return Response.status(Status.CREATED)
                    .entity(nouveauClient).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateClient(@PathParam("id") Integer id, Client client) {
        if (clientService.getClientById(id) == null) {
            return Response.status(Status.NOT_FOUND)
                    .entity("{\"error\": \"Client non trouvé\"}").build();
        }

        try {
            client.setId(id);
            Client clientMisAJour = clientService.updateClient(client);
            return Response.ok(clientMisAJour).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/email/{email}")
    public Response getClientByEmail(@PathParam("email") String email) {
        Client client = clientService.getClientByEmail(email);
        if (client == null) {
            return Response.status(Status.NOT_FOUND)
                    .entity("{\"error\": \"Client non trouvé\"}").build();
        }
        return Response.ok(client).build();
    }
}