package com.pescue.pescue.model;

import com.pescue.pescue.dto.ShelterDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Document("Shelter")
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Shelter {
    @Id
    private String shelterID;
    private String userID;
    private String shelterName;
    private String representativeFacebookLink;
    private String representativeEmailAddress;
    private String unitNoAndStreet;
    private String ward;
    private String district;
    private String city;
    private String shelterPhoneNo;
    private String shelterLogo;
    private List<String> relatedDocuments;
    private Date date = new Date();
    private Boolean isApproved = false;
    private BigDecimal totalFundReceived = BigDecimal.ZERO;

    public Shelter(String userID, String shelterName,  String representativeFacebookLink, String representativeEmailAddress, String unitNoAndStreet, String ward, String district, String city, String shelterPhoneNo, String shelterLogo, List<String> relatedDocuments) {
        this.userID = userID;
        this.shelterName = shelterName;
        this.representativeFacebookLink = representativeFacebookLink;
        this.representativeEmailAddress = representativeEmailAddress;
        this.unitNoAndStreet = unitNoAndStreet;
        this.ward = ward;
        this.district = district;
        this.city = city;
        this.shelterPhoneNo = shelterPhoneNo;
        this.shelterLogo = shelterLogo;
        this.relatedDocuments = relatedDocuments;
    }

    public Shelter(ShelterDTO DTO) {
        this.userID = DTO.getUserID();
        this.shelterName = DTO.getShelterName();
        this.representativeFacebookLink = DTO.getRepresentativeFacebookLink();
        this.representativeEmailAddress = DTO.getRepresentativeEmailAddress();
        this.unitNoAndStreet = DTO.getUnitNoAndStreet();
        this.ward = DTO.getWard();
        this.district = DTO.getDistrict();
        this.city = DTO.getCity();
        this.shelterPhoneNo = DTO.getShelterPhoneNo();
        this.shelterLogo = DTO.getShelterLogo();
        this.relatedDocuments = DTO.getRelatedDocuments();
    }
}
