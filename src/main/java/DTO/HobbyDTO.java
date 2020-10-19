
package DTO;

import entities.Hobby;
import java.util.Objects;

/**
 *
 * @author Rasmus
 */
public class HobbyDTO {
    
    private Long id;
    private String name;
    private String description;

    public HobbyDTO() {}

    public HobbyDTO(Hobby h) {
        this.id = h.getId();
        this.name = h.getName();
        this.description = h.getDescription();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final HobbyDTO other = (HobbyDTO) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
