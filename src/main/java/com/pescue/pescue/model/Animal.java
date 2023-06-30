package com.pescue.pescue.model;

import com.pescue.pescue.dto.AnimalDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("Animal")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
    private boolean isDeleted = false;
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
        this.othersImg = animalDTO.getOthersImg();
        this.isAdopted = false;
        this.isDeleted = false;
    }
}
