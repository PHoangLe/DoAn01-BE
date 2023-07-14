package com.pescue.pescue.controller;

import com.pescue.pescue.dto.AnimalDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.exception.ExistedException;
import com.pescue.pescue.model.Animal;
import com.pescue.pescue.model.User;
import com.pescue.pescue.service.AnimalService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/animal")
@Api
public class AnimalController {
    private final AnimalService animalService;

    @GetMapping("/getAllAnimals")
    public ResponseEntity<Object> getAllAnimals(){
        return ResponseEntity.ok(animalService.getAllAnimals());

    }
    @GetMapping("/getAnimalByAnimalID/{animalID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllAnimalByAnimalID(@PathVariable String animalID){
        if(animalService.getAnimalByAnimalID(animalID) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StringResponseDTO.builder()
                    .message("Không có bé bạn cần tìm")
                    .build());
        return ResponseEntity.ok(animalService.getAnimalByAnimalID(animalID));
    }
    @GetMapping("/getOnlineAdoptersByAnimalID/{animalID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getOnlineAdoptersByAnimalID(@PathVariable String animalID){
        List<User> onlineAdopters = animalService.getAnimalOnlineAdopters(animalID);
        return ResponseEntity.ok(onlineAdopters);
    }

    @GetMapping("/getAnimalsByShelterID/{shelterID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAnimalsByShelterID(@PathVariable String shelterID){
        return ResponseEntity.ok(animalService.getAnimalsByShelterID(shelterID));
    }
    @PostMapping("/addAnimal")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> addAnimal(@RequestBody AnimalDTO animal) throws ExistedException {
        animalService.addAnimal(animal);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Thông tin của bé đã được thêm thành công")
                .build());
    }

    @PutMapping("/updateAnimal")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> updateAnimal(@RequestBody Animal animal) throws ExistedException {
        animalService.updateAnimal(animal);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Thông tin của bé đã được chỉnh sửa thành công")
                .build());
    }

    @DeleteMapping("/deleteAnimal/{animalID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> deleteAnimal(@PathVariable String animalID){
        animalService.deleteAnimal(animalID);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Thông tin của bé đã được xóa thành công")
                .build());
    }

    @PutMapping("/restoreAnimal/{animalID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> restoreAnimal(@PathVariable String animalID){
        animalService.restoreAnimal(animalID);
        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Thông tin của bé đã được hồi phục thành công")
                .build());
    }
}
