
package facades;

import DTO.PersonDTO;
import DTO.PersonsDTO;
import entities.Hobby;
import entities.Phone;
import java.util.List;


/**
 *
 * @author miade
 */
public interface IPersonFacade {
  public PersonDTO getPerson(long id); 
  public PersonsDTO getAllPersons();  
  
  // Skal den være i addresse facaden?
  //public PersonsDTO getPersonZip(int zip); 
  
  
  public PersonsDTO getAllPersons(Hobby hobby); 
  
  public int getPersoncountByHobby(Hobby hobby); 
  
  public PersonDTO addPerson(PersonDTO p);
  public PersonDTO editPerson(PersonDTO p);  
  
}


