package com.pescue.pescue.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pescue.pescue.dto.AdoptionApplicationDTO;
import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import com.pescue.pescue.dto.UserDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("AdoptionApplication")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdoptionApplication {
    @Id
    private String applicationID;
    @JsonIgnore
    private String animalID;
    @JsonIgnore
    private String shelterID;
    @JsonIgnore
    private String userID;
    private Animal animal;
    private Shelter shelter;
    private UserDTO user;
    private Date date;
    private ApplicationStatus applicationStatus;

    public AdoptionApplication(AdoptionApplicationRequestDTO dto, Animal animal, Shelter shelter, UserDTO userDTO) {
        this.animalID = dto.getAnimalID();
        this.shelterID = dto.getShelterID();
        this.userID = dto.getUserID();
        this.animal = animal;
        this.shelter = shelter;
        this.user = userDTO;
        this.date = new Date(System.currentTimeMillis());
        this.applicationStatus = ApplicationStatus.PENDING;
    }
}
