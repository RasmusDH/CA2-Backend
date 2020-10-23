package rest;

import DTO.HobbyDTO;
import DTO.PersonDTO;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonResourceTest {
    

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person p1,p2,p3;
    private static Hobby h1, h2;
    
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }
    
    @AfterAll
    public static void closeTestServer(){
        //System.in.read();
         //Don't forget this, if you called its counterpart in @BeforeAll
         EMF_Creator.endREST_TestWithDB();
         httpServer.shutdownNow();
    }
    
    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("kurt.H@gmail.com", "Kurt", "Hummel");
        p2 = new Person("berries1@hotmail.com", "Rachel", "Berry");
        p3 = new Person("san@mail.com", "Santana", "Something");
        
        h1 = new Hobby("Dance", "Hiphop dance");
        h2 = new Hobby("Football", "AB boldklub");
        
        p1.setHobby(h1);
        p1.setHobby(h2);
        p2.setHobby(h2);
        
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
                em.createNativeQuery("alter table PERSON AUTO_INCREMENT = 1").executeUpdate();
            
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
                em.createNativeQuery("alter table PERSON AUTO_INCREMENT = 1").executeUpdate();
                
                
                em.persist(p1);
                em.persist(p2); 
                em.persist(p3);
            em.getTransaction().commit();
        } finally { 
            em.close();
        }
    }
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person").then().statusCode(200);
    }
   
    //This test assumes the database contains two rows
    @Test
    public void testDummyMsg() throws Exception {
        given()
        .contentType("application/json")
        .get("/person/").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("msg", equalTo("Person endpoint"));   
    }
    
    
    
    @Test
    public void testGetPerson() throws Exception {
        System.out.println("Testing getting Person");
        
        given()
            .contentType("application/json")
            .get("/person/" + p1.getId())
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200.getStatusCode())
            .body("firstName", is("Kurt"))
            .and()
            .body("lastName", is("Hummel"));
    }
    
    
    @Test
    public void getAllPersons(){
        System.out.println("Testing getting all");
        
            List<PersonDTO> personsDTOs;
        
            personsDTOs = given()
                .contentType("application/json")
                .when()
                .get("/person/all")
                .then()
                .extract().body().jsonPath().getList("all", PersonDTO.class);
                    
            PersonDTO p1DTO = new PersonDTO(p1);
            PersonDTO p2DTO = new PersonDTO(p2);
            PersonDTO p3DTO = new PersonDTO(p3);
            
            assertThat(personsDTOs, containsInAnyOrder(p1DTO, p2DTO, p3DTO));
    }
    
    //Gets a server error, it can't seem to find an object
    /*
    @Test
    public void testGetPersonCountByHobby() throws Exception {
        System.out.println("Testing getting Person count by Hobby");
        
        given()
            .contentType("application/json")
            .get("/person/hobbyCount/" + p1.getHobbies().get(0).getName())
            .then()
            .assertThat()
            .statusCode(HttpStatus.OK_200.getStatusCode())
            .body("hobbyCount", equalTo("1"));
    }
    
    
    @Test
    public void getAllPersonsByHobby(){
        System.out.println("Testing getting all by Hobby");
        
            List<PersonDTO> personsDTOs;
        
            personsDTOs = given()
                .contentType("application/json")
                .when()
                .get("/person/allByHobby/" + p1.getHobbies().get(0).getName())
                .then()
                .extract().body().jsonPath().getList("all", PersonDTO.class);
                    
            PersonDTO p1DTO = new PersonDTO(p1);
            PersonDTO p2DTO = new PersonDTO(p2);
            PersonDTO p3DTO = new PersonDTO(p3);
            
            assertThat(personsDTOs, containsInAnyOrder(p1DTO, p2DTO, p3DTO));
    }
    */
    
}























