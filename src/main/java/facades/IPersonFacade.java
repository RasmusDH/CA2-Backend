
package facades;

import DTO.HobbyDTO;
import DTO.PersonDTO;
import DTO.PersonsDTO;
import DTO.PhoneDTO;
import DTO.AddressDTO;
import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import exceptions.NotFoundException;
import java.util.List;


/**
 *
 * @author miade
 */
public interface IPersonFacade {
  public PersonDTO getPerson(long id); 
  public PersonsDTO getAllPersons();  
  public PersonDTO getPersonByNumber(int number);
  public int getPersoncountByHobby(String hobby); 
  public PersonsDTO getAllPersons(String hobby); 
  
  
  public PersonDTO addPerson(PersonDTO pDTO);
  public PersonDTO editPerson(PersonDTO p);  
  public PersonDTO deletePerson(long id);
  public PhoneDTO addPhone(PhoneDTO pDTO, Person p);
  public PhoneDTO editPhone(PhoneDTO p);
  public PhoneDTO deletePhone(int number);
  public HobbyDTO addHobby(HobbyDTO hDTO);
  public HobbyDTO editHobby(HobbyDTO hDTO);
  public HobbyDTO deleteHobby(String hobby);
  
}


