package com.pescue.pescue.model;

import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("AdoptionApplication")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdoptionApplication {
    @Id
    private String applicationID;
    private String animalID;
    private String shelterID;
    private String userID;
    private Date date;
    private String applicationStatus;

    public AdoptionApplication(String animalID, String shelterID, String userID, Date date, String applicationStatus) {
        this.animalID = animalID;
        this.shelterID = shelterID;
        this.userID = userID;
        this.date = date;
        this.applicationStatus = applicationStatus;
    }

    public AdoptionApplication(AdoptionApplicationRequestDTO dto) {
        this.animalID = dto.getAnimalID();
        this.shelterID = dto.getShelterID();
        this.userID = dto.getUserID();
        this.date = new Date(System.currentTimeMillis());
        this.applicationStatus = "pending";
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getAnimalID() {
        return animalID;
    }

    public void setAnimalID(String animalID) {
        this.animalID = animalID;
    }

    public String getShelterID() {
        return shelterID;
    }

    public void setShelterID(String shelterID) {
        this.shelterID = shelterID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
}
