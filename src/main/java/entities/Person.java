package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;


@Entity
@NamedQueries({
    @NamedQuery(name = "Person.deleteAllRows", query = "DELETE from Person"),
    @NamedQuery(name = "Person.getAllRows", query = "SELECT p from Person p")
    
})
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    
    @ManyToMany(mappedBy = "person")
    private List<Hobby> hobbies = new ArrayList();
    
    @ManyToOne(cascade = { CascadeType.PERSIST })
    private Address address;
    
    @OneToMany(mappedBy = "person")
    private List<Phone> numbers = new ArrayList();

    public Person(String email, String firstName, String lastName, List<Phone> phones) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.numbers = phones;
    }
    
    public Person() {
    }
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(Hobby hobby) {
        if (hobby != null){
            hobbies.add(hobby);
        }
    }

    public List<Phone> getNumbers() {
        return numbers;
    }

    public void setNumbers(Phone number) {
        if (number != null){
            numbers.add(number);
        }
    }
    
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (address != null){
            this.address = address;
            //address.setPersons(this);
        } else {
            this.address = null;
        }
        
    }
    
    
}
