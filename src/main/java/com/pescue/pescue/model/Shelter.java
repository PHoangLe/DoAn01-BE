package com.pescue.pescue.model;

import com.pescue.pescue.dto.ShelterDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Shelter")
@Builder
@AllArgsConstructor
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
    private Boolean isApproved = false;

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
        this.city = DTO.getDistrict();
        this.shelterPhoneNo = DTO.getShelterPhoneNo();
        this.shelterLogo = DTO.getShelterLogo();
        this.relatedDocuments = DTO.getRelatedDocuments();
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

    public String getRepresentativeFacebookLink() {
        return representativeFacebookLink;
    }

    public void setRepresentativeFacebookLink(String representativeFacebookLink) {
        this.representativeFacebookLink = representativeFacebookLink;
    }

    public String getRepresentativeEmailAddress() {
        return representativeEmailAddress;
    }

    public void setRepresentativeEmailAddress(String representativeEmailAddress) {
        this.representativeEmailAddress = representativeEmailAddress;
    }

    public String getUnitNoAndStreet() {
        return unitNoAndStreet;
    }

    public void setUnitNoAndStreet(String unitNoAndStreet) {
        this.unitNoAndStreet = unitNoAndStreet;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getShelterPhoneNo() {
        return shelterPhoneNo;
    }

    public void setShelterPhoneNo(String shelterPhoneNo) {
        this.shelterPhoneNo = shelterPhoneNo;
    }

    public List<String> getRelatedDocuments() {
        return relatedDocuments;
    }

    public void setRelatedDocuments(List<String> relatedDocuments) {
        this.relatedDocuments = relatedDocuments;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }
}
