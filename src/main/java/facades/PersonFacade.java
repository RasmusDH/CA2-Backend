package facades;

import DTO.PersonDTO;
import DTO.PersonsDTO;
import entities.Hobby;
import entities.Person;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
        try{
            int personCount = (int)em.createQuery(
              "SELECT COUNT(p) FROM Person p "
                + "left join Hobby h"
                + "on p.hId = h.id "
            ).getSingleResult();
            return personCount;
        }finally{  
            em.close();
        }
    }
    
    @Override
    public PersonsDTO getAllPersons(Hobby hobby) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public PersonDTO addPerson(PersonDTO p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    

}
