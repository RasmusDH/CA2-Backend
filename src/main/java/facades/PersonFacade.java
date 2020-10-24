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
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

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
    
    @Override
    public PersonsDTO getAllPersons(String hobbyName) {
        EntityManager em = getEntityManager();
        
        try{
            Query q = em.createQuery(
                        "SELECT h FROM Hobby h where h.name= :name");
                q.setParameter("name", hobbyName);
                Hobby hobby = (Hobby)q.getSingleResult();
            List<Long> personIDList = listOfPersonIDs(hobby, em);
            List<Person> personList = new ArrayList(); 
            personListByIDList(personIDList, em, personList);
            
            return new PersonsDTO(personList);
        } finally{  
            em.close();
        }
    }

        private void personListByIDList(List<Long> personIDList, EntityManager em, List<Person> personList) {
            for(Long id : personIDList){
                Query q2 = em.createQuery(
                        "SELECT p FROM Person p where p.id= :id");
                q2.setParameter("id", id);
                Person tempP = (Person)q2.getSingleResult();
                personList.add(tempP);
            }
        }

        private List<Long> listOfPersonIDs(Hobby hobby, EntityManager em) {
            long hobbyID = hobby.getId();
            Query q = em.createQuery(
                    "SELECT p.id FROM Person p " +
                            "JOIN Hobby h " +
                            "on p.hobbies.id = h.id where h.id= :id");
            q.setParameter("id", hobbyID);
            List<Long> personIDList = q.getResultList();
            return personIDList;
        }
    
    @Override
    public int getPersoncountByHobby(String hobbyName) {
        EntityManager em = emf.createEntityManager();
        try{
            return getAllPersons(hobbyName).getAll().size();
        }finally{  
            em.close();
        }
    }
    
    @Override
    public PersonDTO getPersonByNumber(int number) {
        EntityManager em = emf.createEntityManager();
        
        try{
            Query q1 = em.createQuery("SELECT ph.person.id FROM Phone ph WHERE ph.number= :number");
                q1.setParameter("number", number);
            long personID = (long)q1.getSingleResult();
            
            Query q2 = em.createQuery("SELECT p FROM Person p where p.id= :id");
                q2.setParameter("id", personID);
                
                Person p = (Person)q2.getSingleResult();
                System.out.println("Person from facade: " + p.getFirstName());
            return new PersonDTO(p);
        }finally{  
            em.close();
        }
    }
    

    
    
    
    
    
    
    @Override
    public PersonDTO addPerson(PersonDTO pDTO) {
        EntityManager em = getEntityManager();
        
        Person person = new Person(pDTO.getEmail(), pDTO.getFirstName(), pDTO.getLastName());
        try {
            em.getTransaction().begin();
                em.persist(person);
            em.getTransaction().commit();
            return pDTO;
        } finally {
            em.close();
        }
        
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) {
        EntityManager em = getEntityManager();
        Person person = em.find(Person.class, p.getId());
        
            
                person.setFirstName(p.getFirstName());
                person.setLastName(p.getLastName());
                person.setEmail(p.getEmail());
                person.setNumbers(p.getNumbers());
                person.setAddress(p.getAddress());
                person.setHobbies(p.getHobbies());
        try {
            em.getTransaction().begin();
                    em.merge(person);

            em.getTransaction().commit();
            return new PersonDTO(person);
        
        } finally {  
          em.close();
        }
    
    
    
    }
    //implement exception
    @Override
    public PersonDTO deletePerson(long id) {
        EntityManager em = getEntityManager();
        
        
        Person person = em.find(Person.class, id);
        Address address = person.getAddress();
        
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

    @Override
    public HobbyDTO deleteHobby(String hobbyName) {
        EntityManager em = getEntityManager();
        
        Query q = em.createQuery("SELECT h FROM Hobby h where h.name :name");
                q.setParameter("name", hobbyName);
                
                Hobby tempH = (Hobby)q.getSingleResult();
        try {
            em.getTransaction().begin();
            em.remove(tempH);
            
            em.getTransaction().commit();
            
        } finally {
            em.close();
        }
        return new HobbyDTO(tempH);
    }

    @Override
    public PhoneDTO deletePhone(int number) {
        EntityManager em = getEntityManager();
        
        Phone phone = em.find(Phone.class, number);
        
        try {
            em.getTransaction().begin();
            em.remove(phone);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PhoneDTO(phone);
    }

    @Override
    public PhoneDTO addPhone(PhoneDTO phonedto, Person p) {
        EntityManager em = getEntityManager();
        
        
        Phone phone = new Phone(phonedto.getNumber(), phonedto.getDescription(), p);
        try {
            em.getTransaction().begin();
            Query q = em.createQuery("SELECT p FROM Person p WHERE p.id= :id");
                q.setParameter("id", p.getId());
                
                
                Person person = (Person)q.getSingleResult();
                if (person.getNumbers().contains(phone)){
                    //throw exception that the number already exists for this person
                } else {
                    p.setNumber(phone);
                }
                em.persist(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PhoneDTO(phone);
    }

    @Override
    public PhoneDTO editPhone(PhoneDTO p) {
        EntityManager em = getEntityManager();
        Phone phone = em.find(Phone.class, p.getId());
        
        
                phone.setDescription(p.getDescription());
                phone.setNumber(p.getNumber());
                
        try {
            em.getTransaction().begin();
                    em.merge(phone);

            em.getTransaction().commit();
            return new PhoneDTO(phone);
        
        } finally {  
          em.close();
        }
    
    
    
    }
    

    @Override
    public HobbyDTO addHobby(HobbyDTO hDTO) {
        EntityManager em = getEntityManager();
        Hobby hobby = new Hobby(hDTO.getName(), hDTO.getDescription());
        try {
            em.getTransaction().begin();
            em.persist(hobby);
            
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new HobbyDTO(hobby);
    }

    @Override
    public HobbyDTO editHobby(HobbyDTO hDTO) {
        EntityManager em = getEntityManager();
        Hobby hobby = em.find(Hobby.class, hDTO.getId());
        
                hobby.setName(hDTO.getName());
                hobby.setDescription(hDTO.getDescription());
                
        try {
            em.getTransaction().begin();
                    em.merge(hobby);

            em.getTransaction().commit();
            return new HobbyDTO(hobby);
        
        } finally {  
          em.close();
        }
    
       
    }

    

    

    

}
