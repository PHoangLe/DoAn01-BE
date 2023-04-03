package com.pescue.pescue.model;

import com.pescue.pescue.dto.AnimalDTO;
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
    private String animalBreed;
    private String animalColor;
    private String animalImg;
    private boolean isVaccinated;
    private boolean isDeWormed;
    private boolean isSterilized;
    private boolean isFriendly;
    private boolean isDeleted = false;
    private List<String> onlineAdaptors;

    public Animal(String shelterID, String animalName, Integer animalAge, boolean animalGender, Integer animalWeight, String animalBreed, String animalColor, String animalImg, boolean isVaccinated, boolean isDeWormed, boolean isSterilized, boolean isFriendly, List<String> onlineAdapters) {
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
        this.onlineAdaptors = onlineAdapters;
    }

    public Animal(AnimalDTO animalDTO){
        this.shelterID = animalDTO.getShelterID();
        this.animalName = animalDTO.getAnimalName();
        this.animalAge = animalDTO.getAnimalAge();
        this.animalGender = animalDTO.isAnimalGender();
        this.animalWeight = animalDTO.getAnimalWeight();
        this.animalBreed = animalDTO.getAnimalBreed();
        this.animalColor = animalDTO.getAnimalColor();
        this.animalImg = animalDTO.getAnimalImg();
        this.isVaccinated = animalDTO.isVaccinated();
        this.isDeWormed = animalDTO.isDeWormed();
        this.isSterilized = animalDTO.isSterilized();
        this.isFriendly = animalDTO.isFriendly();
        this.onlineAdaptors = animalDTO.getOnlineAdaptors();
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

    public String getAnimalBreed() {
        return animalBreed;
    }

    public void setAnimalBreed(String animalBreed) {
        this.animalBreed = animalBreed;
    }

    public String getAnimalColor() {
        return animalColor;
    }

    public void setAnimalColor(String animalColor) {
        this.animalColor = animalColor;
    }

    public String getAnimalImg() {
        return animalImg;
    }

    public void setAnimalImg(String animalImg) {
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public List<String> getOnlineAdaptors() {
        return onlineAdaptors;
    }

    public void setOnlineAdaptors(List<String> onlineAdaptors) {
        this.onlineAdaptors = onlineAdaptors;
    }
}
