
package rest;


import DTO.AddressDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.AlreadyExistsException;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import exceptions.NotFoundException;
import facades.AddressFacade;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import utils.EMF_Creator;


@Path("address")
public class AddressResource {

    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    
    private static final AddressFacade FACADE =  AddressFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
 
   
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Address endpoint\"}";
    }
    
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllZipCodes() {
        return GSON.toJson(FACADE.getAllZipCodes());
    }
    
    @GET
    @Path("/{street}")
    @Produces(MediaType.APPLICATION_JSON)
    public String findAddress(@PathParam("street") String street) {
        try {
            return GSON.toJson(FACADE.findAddress(street));
        } catch (NotFoundException ex) {
            throw new WebApplicationException(ex.getMessage(), 400);
        }
        
    }
    
    @GET
    @Path("persons/{zipCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPersonsWithZip(@PathParam("zipCode") String zipCode) {
        try {
            return GSON.toJson(FACADE.getAllPersonsWithZip(zipCode));
        } catch (NotFoundException ex) {
            throw new WebApplicationException(ex.getMessage(), 400);
        }
        
    }
    
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createAddress(AddressDTO address) {
        try {
            return GSON.toJson(FACADE.createAddress(address));
        } catch (AlreadyExistsException ex) {
            throw new WebApplicationException(ex.getMessage(), 400);
        }
    }
    
    
}

