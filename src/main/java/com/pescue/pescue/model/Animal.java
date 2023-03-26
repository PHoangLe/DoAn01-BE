package com.pescue.pescue.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Animal")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Animal {
    @Id
    private String animalID;
    private String shelterID;
    private String animalName;
    private Integer animalAge;
    private boolean animalGender;
    private Integer animalWeight;
    private Integer animalBreed;
    private Integer animalColor;
    private Integer animalImg;
    private boolean isVaccinated;
    private boolean isDeWormed;
    private boolean isSterilized;
    private boolean isFriendly;
    private List<String> onlineAdapters;

    public Animal(String shelterID, String animalName, Integer animalAge, boolean animalGender, Integer animalWeight, Integer animalBreed, Integer animalColor, Integer animalImg, boolean isVaccinated, boolean isDeWormed, boolean isSterilized, boolean isFriendly, List<String> onlineAdapters) {
        this.shelterID = shelterID;
        this.animalName = animalName;
        this.animalAge = animalAge;
        this.animalGender = animalGender;
        this.animalWeight = animalWeight;
        this.animalBreed = animalBreed;
        this.animalColor = animalColor;
        this.animalImg = animalImg;
        this.isVaccinated = isVaccinated;
        this.isDeWormed = isDeWormed;
        this.isSterilized = isSterilized;
        this.isFriendly = isFriendly;
        this.onlineAdapters = onlineAdapters;
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

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public Integer getAnimalAge() {
        return animalAge;
    }

    public void setAnimalAge(Integer animalAge) {
        this.animalAge = animalAge;
    }

    public boolean isAnimalGender() {
        return animalGender;
    }

    public void setAnimalGender(boolean animalGender) {
        this.animalGender = animalGender;
    }

    public Integer getAnimalWeight() {
        return animalWeight;
    }

    public void setAnimalWeight(Integer animalWeight) {
        this.animalWeight = animalWeight;
    }

    public Integer getAnimalBreed() {
        return animalBreed;
    }

    public void setAnimalBreed(Integer animalBreed) {
        this.animalBreed = animalBreed;
    }

    public Integer getAnimalColor() {
        return animalColor;
    }

    public void setAnimalColor(Integer animalColor) {
        this.animalColor = animalColor;
    }

    public Integer getAnimalImg() {
        return animalImg;
    }

    public void setAnimalImg(Integer animalImg) {
        this.animalImg = animalImg;
    }

    public boolean isVaccinated() {
        return isVaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        isVaccinated = vaccinated;
    }

    public boolean isDeWormed() {
        return isDeWormed;
    }

    public void setDeWormed(boolean deWormed) {
        isDeWormed = deWormed;
    }

    public boolean isSterilized() {
        return isSterilized;
    }

    public void setSterilized(boolean sterilized) {
        isSterilized = sterilized;
    }

    public boolean isFriendly() {
        return isFriendly;
    }

    public void setFriendly(boolean friendly) {
        isFriendly = friendly;
    }

    public List<String> getOnlineAdapters() {
        return onlineAdapters;
    }

    public void setOnlineAdapters(List<String> onlineAdapters) {
        this.onlineAdapters = onlineAdapters;
    }
}
