
package facades;

import DTO.HobbyDTO;
import DTO.PersonDTO;
import DTO.PersonsDTO;
import DTO.PhoneDTO;
import DTO.AddressDTO;
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
  public int getPersoncountByHobby(Hobby hobby); 
  public PersonDTO getPersonByNumber(int number);
  public PersonsDTO getAllPersons(Hobby hobby); 
  public PersonsDTO getPersonsByAddress(AddressDTO addressdto);
  
  // Skal den være i addresse facaden?
  //public PersonsDTO getPersonsZip(int zip); 
  
  
  public PersonDTO addPerson(PersonDTO pDTO, List<Phone> phone, String street, String additionalInfo);
  public PersonDTO editPerson(PersonDTO p);  
  public PersonDTO deletePerson(long id);
  public PhoneDTO addPhone(PhoneDTO pDTO, PersonDTO p);
  public PhoneDTO editPhone(PhoneDTO p);
  public PhoneDTO deletePhone(int number);
  public HobbyDTO addHobby(HobbyDTO hDTO, List<PersonDTO> p);
  public HobbyDTO editHobby(HobbyDTO hDTO);
  public HobbyDTO deleteHobby(String hobby);
  
}


