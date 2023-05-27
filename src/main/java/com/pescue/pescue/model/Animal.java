package com.pescue.pescue.model;

import com.pescue.pescue.dto.AnimalDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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
    private String animalAge;
    private boolean animalGender;
    private Integer animalWeight;
    private String animalBreed;
    private String animalSpecie;
    private String animalColor;
    private String animalImg;
    private String animalStatus;
    private boolean vaccinated;
    private boolean deWormed;
    private boolean sterilized;
    private boolean friendly;
    private boolean isAdopted = false;
    @DBRef
    private List<User> onlineAdopters;
    private List<String> othersImg;

    public Animal(AnimalDTO animalDTO){
        this.shelterID = animalDTO.getShelterID();
        this.animalName = animalDTO.getAnimalName();
        this.animalAge = animalDTO.getAnimalAge();
        this.animalGender = animalDTO.isAnimalGender();
        this.animalWeight = animalDTO.getAnimalWeight();
        this.animalBreed = animalDTO.getAnimalBreed();
        this.animalSpecie = animalDTO.getAnimalSpecie();
        this.animalColor = animalDTO.getAnimalColor();
        this.animalImg = animalDTO.getAnimalImg();
        this.animalStatus = animalDTO.getAnimalStatus();
        this.vaccinated = animalDTO.isVaccinated();
        this.deWormed = animalDTO.isDeWormed();
        this.sterilized = animalDTO.isSterilized();
        this.friendly = animalDTO.isFriendly();
        this.onlineAdopters = new ArrayList<>();
        this.othersImg = animalDTO.getOthersImg();
        this.isAdopted = false;
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

    public String getAnimalAge() {
        return animalAge;
    }

    public void setAnimalAge(String animalAge) {
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

    public String getAnimalSpecie() {
        return animalSpecie;
    }

    public void setAnimalSpecie(String animalSpecie) {
        this.animalSpecie = animalSpecie;
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

    public String getAnimalStatus() {
        return animalStatus;
    }

    public void setAnimalStatus(String animalStatus) {
        this.animalStatus = animalStatus;
    }

    public boolean isVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }

    public boolean isDeWormed() {
        return deWormed;
    }

    public void setDeWormed(boolean deWormed) {
        this.deWormed = deWormed;
    }

    public boolean isSterilized() {
        return sterilized;
    }

    public void setSterilized(boolean sterilized) {
        this.sterilized = sterilized;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

    public boolean isAdopted() {
        return isAdopted;
    }

    public void setAdopted(boolean adopted) {
        this.isAdopted = adopted;
    }

    public List<User> getOnlineAdopters() {
        return onlineAdopters;
    }

    public void setOnlineAdopters(List<User> onlineAdopters) {
        this.onlineAdopters = onlineAdopters;
    }

    public List<String> getOthersImg() {
        return othersImg;
    }

    public void setOthersImg(List<String> othersImg) {
        this.othersImg = othersImg;
    }
}
