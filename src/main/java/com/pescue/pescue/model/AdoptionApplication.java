package com.pescue.pescue.model;

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
    private Animal animal;
    private Shelter shelter;
    private UserDTO user;
    private Date date;
    private ApplicationStatus applicationStatus;

    public AdoptionApplication(Animal animal, Shelter shelter, UserDTO userDTO) {
        this.animal = animal;
        this.shelter = shelter;
        this.user = userDTO;
        this.date = new Date(System.currentTimeMillis());
        this.applicationStatus = ApplicationStatus.PENDING;
    }
}
