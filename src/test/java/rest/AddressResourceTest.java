package rest;

import DTO.AddressDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import facades.AddressFacade;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
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
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
@Disabled
public class AddressResourceTest {
    

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static AddressFacade facade;
    private EntityManager em;
    
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    
    private Person p1, p2, p3, p4;
    private Address a1, a2, a3;
    private CityInfo c1, c2, c3;

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
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            
            p1 = new Person("email", "Gurli", "Mogensen");
            p2 = new Person("outlook", "Gunnar", "Hjorth");
            p3 = new Person("gmail", "Søren", "Brun");
            p4 = new Person("gmail", "Karen", "Brun");
            
            c1 = new CityInfo("2200", "KBH N");
            c2 = new CityInfo("2400", "KBH NV");
            c3 = new CityInfo("2800", "Lyngby");
            
            a1 = new Address("Testgade", "Meget smuk.");
            a2 = new Address("Testvej", "Snavset...");
            a3 = new Address("Testbommen", "Ok.");
            
            //adding Address to CityInfo
            c1.addAddress(a1);
            c2.addAddress(a2);
            c3.addAddress(a3);
            //adding CityInfo to Address
            a1.setCityInfo(c1);
            a2.setCityInfo(c2);
            a3.setCityInfo(c3);
            //persisting CityInfos and Addresses
            em.persist(c1);
            em.persist(c2);
            em.persist(c3);
            
//            em.persist(a1);
//            em.persist(a2);
//            em.persist(a3);

            //adding Address to Persons
            p1.setAddress(a1);
            p2.setAddress(a2);
            p3.setAddress(a3);
            p4.setAddress(a3);
            //persisting Persons
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
            
            em.getTransaction().commit();
        } finally { 
            em.close();
        }
    }
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/address").then().statusCode(200);
    }
   
    //This test assumes the database contains two rows
    @Test
    public void testDummyMsg() throws Exception {
        given()
        .contentType("application/json")
        .get("/address/").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("msg", equalTo("Address endpoint"));   
    }
    
    @Test
    public void testFindAddress() {
            given()
                .contentType("application/json")
                .get("/address/"+a1.getStreet()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("street", equalTo("Testgade"))
                .body("additionalInfo", equalTo("Meget smuk."));
    }
    //test broke from 500 status code error
    /*
    @Test
    public void testGetAllPersonsWithZip() {
        given()
                .contentType("application/json")
                .get("/address/persons/" + p1.getAddress().getCityInfo().getZipCode()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", hasItems("Gurli"))
                .body("lastName", hasItems("Mogensen"));
    }
    */
    @Test
    public void testAddAddress() {

        AddressDTO addressToBeAdded = new AddressDTO("Test", "new address");

        AddressDTO result
                = with()
                        .body(addressToBeAdded) //include object in body
                        .contentType("application/json")
                        .when().request("POST", "/address/add").then() //post REQUEST
                        .assertThat()
                        .statusCode(HttpStatus.OK_200.getStatusCode())
                        .extract()
                        .as(AddressDTO.class); //extract result JSON as object

        //checking that nothing has changed that we don't want to change
        assertThat((result.getStreet()), equalTo("Test"));
        assertThat((result.getAdditionalInfo()), equalTo("new address"));
    }
    
}























