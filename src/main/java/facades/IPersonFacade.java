
package facades;

import DTO.HobbyDTO;
import DTO.PersonDTO;
import DTO.PersonsDTO;
import DTO.PhoneDTO;
import DTO.AddressDTO;
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
  public int getPersoncountByHobby(Hobby hobby); 
  public PersonDTO getPersonByNumber(int number);
  public PersonsDTO getAllPersons(Hobby hobby); 
  
  
  public PersonDTO addPerson(PersonDTO pDTO, String street, String additionalInfo);
  public PersonDTO editPerson(PersonDTO p) throws NotFoundException;  
  public PersonDTO deletePerson(long id) throws NotFoundException;
  public PhoneDTO addPhone(PhoneDTO pDTO, Person p);
  public PhoneDTO editPhone(PhoneDTO p) throws NotFoundException;
  public PhoneDTO deletePhone(int number) throws NotFoundException;
  public HobbyDTO addHobby(HobbyDTO hDTO);
  public HobbyDTO editHobby(HobbyDTO hDTO) throws NotFoundException;
  public HobbyDTO deleteHobby(String hobby) throws NotFoundException;
  
}


