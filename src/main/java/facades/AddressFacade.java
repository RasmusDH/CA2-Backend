package facades;

import DTO.AddressDTO;
import entities.Address;
import exceptions.AlreadyExistsException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
    
    //TODO Remove/Change this before use
    public long getRenameMeCount(){
        EntityManager em = emf.createEntityManager();
        try{
            long renameMeCount = (long)em.createQuery("SELECT COUNT(r) FROM RenameMe r").getSingleResult();
            return renameMeCount;
        }finally{  
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
            throw new AlreadyExistsException("This address exists in the database.");
        } finally {
            em.close();
        }
    }

}
