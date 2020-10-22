package facades;

import DTO.AddressDTO;
import DTO.CityInfoDTO;
import DTO.PersonDTO;
import entities.Address;
import entities.Person;
import exceptions.AlreadyExistsException;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class AddressFacade {

    private static AddressFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private AddressFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static AddressFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AddressFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    
    public List<CityInfoDTO> getAllZipCodes() {
        EntityManager em = emf.createEntityManager();
        try {
            List<CityInfoDTO> list = em.createQuery("SELECT c FROM CityInfo c").getResultList();
            return list;
        } finally {
            em.close();
        }
    }
    
    public AddressDTO createAddress(AddressDTO address) throws AlreadyExistsException {
        EntityManager em = emf.createEntityManager();
        Address entaddress = new Address();
        entaddress.setStreet(address.getStreet());
        entaddress.setAdditionalInfo(address.getAdditionalInfo());
        try {
            em.getTransaction().begin();
            em.persist(entaddress);
            em.getTransaction().commit();
            return address;
        } catch (Exception ex) {
            throw new AlreadyExistsException("This address already exists in the database.");
        } finally {
            em.close();
        }
    }
    
    public AddressDTO findAddress(String street) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        Address entaddress = null;
        try {
            em.getTransaction().begin();
            TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE"
                    + " a.street = :address", Address.class)
                    .setParameter("address", street);
            em.getTransaction().commit();
            entaddress = query.getSingleResult();
            return new AddressDTO(entaddress);
        } catch (Exception e) {
            throw new NotFoundException("No address found");
        } finally {
            em.close();
        }
    }
    
    public List<PersonDTO> getAllPersonsWithZip(String zipCode) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            List<Person> persons = em.createQuery("SELECT p FROM Person p "
                    + "JOIN p.address pa JOIN pa.cityInfo pac WHERE pac.zipCode = :zipCode", Person.class)
                    .setParameter("zipCode", zipCode)
                    .getResultList();

            List<PersonDTO> persondtos = new ArrayList();
            for (Person person : persons) {
                persondtos.add(new PersonDTO(person));
            }

            return persondtos;
        } catch (NoResultException ex) {
            throw new NotFoundException("Persons with given Zip Code could not be found");
        } finally {
            em.close();
        }
    }

}
