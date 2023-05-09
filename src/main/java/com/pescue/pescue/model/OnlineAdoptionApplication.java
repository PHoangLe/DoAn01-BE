package com.pescue.pescue.model;

import com.pescue.pescue.dto.AdoptionApplicationDTO;
import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("OnlineAdoptionApplication")
@AllArgsConstructor
@Getter
@Setter
public class OnlineAdoptionApplication extends AdoptionApplication{
    public OnlineAdoptionApplication(AdoptionApplicationRequestDTO dto){
        super(dto);
    }

    public OnlineAdoptionApplication(String animalID, String shelterID, String userID, Date date, ApplicationStatus applicationStatus) {
        super(animalID, shelterID, userID, date, applicationStatus);
    }
}
