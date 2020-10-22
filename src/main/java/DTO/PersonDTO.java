
package DTO;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Rasmus
 */
public class PersonDTO {
    
    
    private long id;
    private String email;
    private String firstName;
    private String lastName;
    
    private List<Phone> numbers;
    //private String phoneDescription;
    
    private List<Hobby> hobbies;
    //private String name;
    //private String hobbyDescription;
    
    private Address address;
    //private String Street;
    //private String AdditionalInfo;
    //private CityInfo cityInfo;

    public PersonDTO() {}
    
    public PersonDTO(Person p) {
        this.id = p.getId();
        this.email = p.getEmail();
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
        
        this.numbers = p.getNumbers();
        
        this.hobbies = p.getHobbies();
        
        this.address = p.getAddress();
        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Phone> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Phone> numbers) {
        this.numbers = numbers;
    }
    
    public void setNumber(Phone number) {
        if (number != null){
            numbers.add(number);
        }
    }

    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Hobby> hobbies) {
        this.hobbies = hobbies;
    }
    
    public void setHobby(Hobby hobby) {
        if (hobby != null){
            hobbies.add(hobby);
        }
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersonDTO other = (PersonDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
      
    
    
    
}
