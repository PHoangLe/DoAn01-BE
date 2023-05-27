package com.pescue.pescue.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
    @DBRef
    private Animal animal;
    @DBRef
    private Shelter shelter;
    @DBRef
    private User user;
    private Date date;
    private ApplicationStatus applicationStatus;

    public AdoptionApplication(Animal animal, Shelter shelter, User user) {
        this.animal = animal;
        this.shelter = shelter;
        this.user = user;
        this.date = new Date(System.currentTimeMillis());
        this.applicationStatus = ApplicationStatus.PENDING;
    }
}
