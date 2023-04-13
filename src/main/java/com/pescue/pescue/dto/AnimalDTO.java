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
public class AnimalDTO {
    private String shelterID;
    private String animalName;
    private String animalAge;
    private boolean animalGender;
    private Integer animalWeight;
    private String animalBreed;
    private String animalSpecie;
    private String animalColor;
    private String animalImg;
    private boolean vaccinated;
    private boolean deWormed;
    private boolean sterilized;
    private boolean friendly;
    private List<String> othersImg;
}
