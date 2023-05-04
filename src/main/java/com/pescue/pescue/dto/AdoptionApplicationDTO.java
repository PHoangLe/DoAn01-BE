package com.pescue.pescue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdoptionApplicationDTO {
    private String animalID;
    private String shelterID;
    private String userID;
    private Date date;
}
