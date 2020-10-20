package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;


@Entity
@NamedQuery(name = "CityInfo.deleteAllRows", query = "DELETE from CityInfo")
public class CityInfo implements Serializable {

    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    @Id
    @Column(length = 4)
    private String zipCode;
    @Column(length=35)
    private String city;
    @JoinColumn(name = "cityinfo_id")
    @OneToMany(mappedBy = "cityInfo")
    private List<Address> addresses = new ArrayList();
    
    public CityInfo() {
    }

    public CityInfo(String zipCode, String city) {
        this.zipCode = zipCode;
        this.city = city;
    }
    
        
//    public Long getId() {
//        return id;
//    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }
    
    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
    
    public void addAddress(Address address){
        this.addresses.add(address);
    }
    
}
