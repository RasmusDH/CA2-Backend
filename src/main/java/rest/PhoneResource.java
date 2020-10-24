
package rest;

import DTO.PersonDTO;
import DTO.PersonsDTO;
import DTO.PhoneDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.PersonFacade;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import entities.Person;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import utils.EMF_Creator;

//Todo Remove or change relevant parts before ACTUAL use
@Path("phone")
public class PhoneResource {

    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    
    private static final PersonFacade FACADE =  PersonFacade.getFacadeExample(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
 
   
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Phone endpoint\"}";
    }
    
    @GET
    @Path("/{number}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonByNumber(@PathParam("number") int number){
        PersonDTO personDTO = FACADE.getPersonByNumber(number);
        
        return GSON.toJson(personDTO);
    }
    /*
    @Path("/add")
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String addPhone(PhoneDTO phone, Person p) {
        PhoneDTO pDTO = FACADE.addPhone(phone, p);
        return GSON.toJson(pDTO);
        
    }*/
    
    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String editPhone(@PathParam("id") String phone) {
        PhoneDTO phDTO = GSON.fromJson(phone, PhoneDTO.class);
        PhoneDTO phNew = FACADE.editPhone(phDTO);
        return GSON.toJson(phNew);
    }
    
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String deletePhone(@PathParam("id") int number) {
        PhoneDTO phDeleted = FACADE.deletePhone(number);
        return GSON.toJson(phDeleted);
    }

}

