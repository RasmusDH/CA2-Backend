package facades;

import DTO.AddressDTO;
import DTO.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Person;
import exceptions.AlreadyExistsException;
import exceptions.NotFoundException;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
@Disabled
public class AddressFacadeTest {

    private static EntityManagerFactory emf;
    private static AddressFacade facade;
    private static PersonFacade FACADE;
    private EntityManager em;
//    private static EntityManager sem;
    
    private Person p1, p2, p3, p4;
    private Address a1, a2, a3;
    private CityInfo c1, c2, c3;
//    private static CityInfo sc1, sc2, sc3;

    public AddressFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = AddressFacade.getFacadeExample(emf);
       FACADE = PersonFacade.getFacadeExample(emf);
//       sem = emf.createEntityManager();
//       
//       try {
//           sem.getTransaction().begin();
//           sem.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
//           sc1 = new CityInfo("2100", "KBH Ø");
//           sc2 = new CityInfo("2300", "KBH S");
//           sc3 = new CityInfo("2820", "Gentofte");
//           
//           sem.getTransaction().commit();
//       } finally {
//           sem.close();
//       }
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
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

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testGetAllZipCodes() {
        System.out.println("Tester getAllZipCodes");
        
        assertThat(facade.getAllZipCodes(), hasSize(3));
    }
    
    @Test
    public void testGetAllPersonsWithZip() throws NotFoundException {
        System.out.println("Tester getAllPersonsWithZip");
        
        assertThat(facade.getAllPersonsWithZip("2200"), hasSize(1));
        assertThat(facade.getAllPersonsWithZip("2800"), hasSize(2));
    }
    
    @Test
    public void testFindAddress() throws NotFoundException {
        System.out.println("Tester findAddress");
        
        assertEquals("Testgade", facade.findAddress(a1.getStreet()).getStreet());
    }
    
    @Test
    public void testFailFindAddress() throws NotFoundException {
        System.out.println("Tester failFindAddress");
        
        Address a4 = a1;
        a1.setStreet("thiswillfail");
        try {
            AddressDTO a = facade.findAddress(a4.getStreet());
            fail("No address found");
        } catch (NotFoundException ex) {
            assertThat(ex.getMessage(), is("No address found"));
        }
    }
    
    @Test
    public void testCreateAddress() throws AlreadyExistsException {
        AddressDTO addressdto = new AddressDTO("Testvej2", "testdesc");
        AddressDTO aDTO = facade.createAddress(addressdto);
        assertThat(aDTO, is (addressdto));
    }
    
    @Test
    public void testCreateAddressFail() throws AlreadyExistsException {
        AddressDTO addressdto = new AddressDTO("Testvej2", "testdesc");
        AddressDTO aDTO = facade.createAddress(addressdto);
        try {
        AddressDTO newaddressdto = facade.createAddress(addressdto);
        fail("expected AlreadyExistsException to be thrown");
        }catch(AlreadyExistsException ex) {
            assertThat(ex.getMessage(), is("This address already exists in the database."));
        }
    }
    
    
    
    
}
