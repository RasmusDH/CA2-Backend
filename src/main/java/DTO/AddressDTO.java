package DTO;

import entities.Address;
import java.util.List;

/**
 *
 * @author Malthe
 */
public class AddressDTO {
    private String street;
    private String additionalInfo;
    private List<PersonDTO> persons;
    private CityInfoDTO cityInfo;
    
    //Constructor for making AddressDTOs with data from the DB
    public AddressDTO(Address address) {
        this.street = address.getStreet();
        this.additionalInfo = address.getAdditionalInfo();
    }
    
    //Constructor for making addressDTOs with data from a POST
    public AddressDTO(String street, String additionalInfo) {
        this.street = street;
        this.additionalInfo = additionalInfo;
    }

    public AddressDTO() {
    }

    public CityInfoDTO getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfoDTO cityInfo) {
        this.cityInfo = cityInfo;
    }
    
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return "AddressDTO{" + "street=" + street + ", additionalInfo=" + additionalInfo + ", persons=" + persons + '}';
    }
    
}
