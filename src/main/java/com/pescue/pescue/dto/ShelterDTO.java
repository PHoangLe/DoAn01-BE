package com.pescue.pescue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShelterDTO {
    private String userID;
    private String shelterName;
    private String representativeFacebookLink;
    private String representativeEmailAddress;
    private String unitNoAndStreet;
    private String ward;
    private String district;
    private String city;
    private String shelterPhoneNo;
    private List<String> relatedDocuments;
}
