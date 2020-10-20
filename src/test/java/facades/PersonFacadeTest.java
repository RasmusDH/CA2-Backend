package facades;

import DTO.PersonDTO;
import DTO.PersonsDTO;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person p1, p2;
    private static Phone ph1, ph2, ph3;
    private static Hobby h1, h2;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactory();
       facade = PersonFacade.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        
        
        p1 = new Person("kurt.H@gmail.com", "Kurt", "Hummel");
        p2 = new Person("berries1@hotmail.com", "Rachel", "Berry");
        
        ph1 = new Phone(12345678, "Kurts mobil", p1);
        ph2 = new Phone(22223333, "Kurts arbejdsmobil", p1);
        ph3 = new Phone(12121212, "Rachels mobil", p2);
            p1.setNumber(ph1);
            p1.setNumber(ph2);
            p2.setNumber(ph3);
            
        h1 = new Hobby("Dance", "Hiphop dance");
        h2 = new Hobby("Football", "AB boldklub");
            p1.setHobby(h1);
            p1.setHobby(h2);
            p2.setHobby(h2);
            
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
                em.persist(p1);
                em.persist(p2);
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
    public void testGetPerson(){
        System.out.println("Tester getPerson");
        
        Long tempID = p1.getId();
        int id = tempID.intValue();
        
        EntityManagerFactory _emf = null;
        PersonFacade pFac = PersonFacade.getFacadeExample(_emf);
        
        PersonDTO expResult = new PersonDTO(p1);
        PersonDTO result = pFac.getPerson(id);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetAll() {
        System.out.println("Tester getAll");
        
        EntityManagerFactory _emf = null;
        PersonFacade pFac = PersonFacade.getFacadeExample(_emf);
        
        int expResult = 2;
        PersonsDTO result = pFac.getAllPersons();
        
        assertEquals(expResult, result.getAll().size());
    }
    
    
    @Test
    public void testGetPersoncountHobby() {
        System.out.println("Tester getPersonCount Hobby");
        
        EntityManagerFactory _emf = null;
        PersonFacade pFac = PersonFacade.getFacadeExample(_emf);
        
        Long expResult = 2L;
        Long result = pFac.getPersoncountByHobby(hobby);
        assertEquals(expResult, result);
    }
}
