package facades;

import DTO.AddressDTO;
import DTO.HobbyDTO;
import DTO.PersonDTO;
import DTO.PersonsDTO;
import DTO.PhoneDTO;
import entities.Hobby;
import entities.Person;
import entities.Address;
import entities.Phone;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade implements IPersonFacade{

    private static PersonFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private PersonFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    
    
    // GET metoder:
    @Override
    public PersonDTO getPerson(long id){
       EntityManager em = getEntityManager();
       try {
            Person person = em.find(Person.class, id);
            return new PersonDTO(person);
       } finally {
           em.close();
       }
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();
        try {
            return new PersonsDTO(em.createNamedQuery("Person.getAllRows").getResultList());
        } finally{  
            em.close();
        }   
    }
    
/*
    @Override
    public PersonsDTO getPersonZip(int zip) {
        
    }
*/
    
    @Override
    public int getPersoncountByHobby(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        
        long hobbyID = hobby.getId();
        try{
            Query q = em.createQuery(
                "SELECT COUNT(p) FROM Person p" +
                "left join Hobby h" +
                "on p.hId = h.id where h.id= :id");
                q.setParameter("id", hobbyID);
                
                return (int)q.getSingleResult();
        }finally{  
            em.close();
        }
    }
    
    @Override
    public PersonDTO getPersonByNumber(int number) {
        EntityManager em = emf.createEntityManager();
        
        try{
            Query q1 = em.createQuery("SELECT id FROM Phone p WHERE number= :number");
                q1.setParameter("number", number);
            long phoneID = (long)q1.getSingleResult();
            
            Query q2 = em.createQuery(
                "SELECT p FROM Person p"
                + "left join Phone ph"
                + "on p.phId = ph.id where ph.id= :id");
                q2.setParameter("id", phoneID);
                
            return (PersonDTO)q2.getSingleResult();
        }finally{  
            em.close();
        }
    }
    
    @Override
    public PersonsDTO getAllPersons(Hobby hobby) {
        EntityManager em = getEntityManager();
        
        long hobbyID = hobby.getId();
        try{
            Query q = em.createQuery(
                "SELECT p FROM Person p" +
                "left join Hobby h" +
                "on p.hId = h.id where h.id= :id");
                q.setParameter("id", hobbyID);
                
            return new PersonsDTO(q.getResultList());
        } finally{  
            em.close();
        }
    }

    @Override
    public PersonsDTO getPersonsByAddress(AddressDTO addressdto) {
        EntityManager em = getEntityManager();
        
        long addressID = addressdto.getID();
        try{
            Query q = em.createQuery(
                "SELECT p FROM Person p" +
                "left join Address a" +
                "on p.aId = a.id where a.id= :id");
                q.setParameter("id", addressID);
                
            return new PersonsDTO(q.getResultList());
        } finally{  
            em.close();
        }
    }
    
    
    

    @Override
    public PersonDTO addPerson(PersonDTO pDTO, List<Phone> phones, String street, String additionalInfo) {
        EntityManager em = getEntityManager();
        
        Person person = new Person(pDTO.getEmail(), pDTO.getFirstName(), pDTO.getLastName(), phones);
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("SELECT a FROM Address a WHERE a.street= :street AND a.additionalInfo= :additionalInfo");
                q.setParameter("street", street);
                q.setParameter("additionalInfo", additionalInfo);
                
                // Laver en liste med alle adresser:
                List<Address> addresses = q.getResultList();
                // Hvis addressen allerede findes i db:
                if (addresses.size() > 0){
                    person.setAddress(addresses.get(0));
                } else {
                    person.setAddress(new Address(street, additionalInfo));
                }
                em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(person);
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //implement exception
    @Override
    public PersonDTO deletePerson(long id) {
        EntityManager em = getEntityManager();
        
        
        Person person = em.find(Person.class, id);
        Address address = person.getAddress();
        if (person == null) {
            return null;
            //throw new PersonNotFoundException(String.format("Person with id: (%d) not found", id));
        } else {
        try {
            em.getTransaction().begin();
                em.remove(person);
                address.getPersons().remove(person);
                    if (address.getPersons().size() < 1){
                        em.remove(address);
                    }
            em.getTransaction().commit();
            } finally {
                em.close();
            }
            return new PersonDTO(person);
        }
    }

    @Override
    public HobbyDTO deleteHobby(String hobby) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PhoneDTO deletePhone(int number) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PhoneDTO addPhone(PhoneDTO pDTO, PersonDTO p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PhoneDTO editPhone(PhoneDTO p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HobbyDTO addHobby(HobbyDTO hDTO, List<PersonDTO> p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HobbyDTO editHobby(HobbyDTO hDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    

}
