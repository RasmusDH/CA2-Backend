package DTO;

import entities.CityInfo;

/**
 *
 * @author Malthe
 */
public class CityInfoDTO {
    private String zipCode;
    private String city;
    
    //Constructor for making personDTOs with data from the DB
    public CityInfoDTO(CityInfo cityInfo) {
        this.zipCode = cityInfo.getZipCode();
        this.city = cityInfo.getCity();
    }
    
    public CityInfoDTO(String zipCode, String city) {
        this.zipCode = zipCode;
        this.city = city;
    }

    public CityInfoDTO() {
    }
    
    
    public String getZipCode() {
        return zipCode;
    }
    
    public String getCity() {
        return city;
    }
    
    @Override
    public String toString() {
        return "CityInfoDTO{" + "zipCode=" + zipCode + ", city=" + city + '}';
    }
    
}
