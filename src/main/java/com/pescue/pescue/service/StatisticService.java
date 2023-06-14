package com.pescue.pescue.service;

import com.pescue.pescue.dto.LandingPageStatisticDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticService {
    private final AnimalService animalService;
    private final ShelterService shelterService;

    public LandingPageStatisticDTO getLandingPageStatistc(){
        long[] animalCount = animalService.countAdoptedAndNotAdopted();

        long adoptedAnimalCount = animalCount[0];
        long notAdoptedAnimalCount = animalCount[1];
        long shelterCount = shelterService.countExistingShelter();

        return new LandingPageStatisticDTO(shelterCount, adoptedAnimalCount, notAdoptedAnimalCount);
    }
}
