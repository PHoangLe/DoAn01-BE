package com.pescue.pescue.controller;

import com.pescue.pescue.dto.AnimalDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.model.Animal;
import com.pescue.pescue.model.Shelter;
import com.pescue.pescue.service.AnimalService;
import com.pescue.pescue.service.ShelterService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/animal")
public class AnimalController {

    @Autowired
    AnimalService animalService;
    @Autowired
    ShelterService shelterService;

    @GetMapping("/getAllAnimals")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllAnimals(){
        return ResponseEntity.ok(animalService.findAllAnimals());

    }@GetMapping("/getAnimalByAnimalID/{animalID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllAnimalByAnimalID(@PathVariable String animalID){
        if(animalService.findAnimalByAnimalID(animalID) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StringResponseDTO.builder()
                    .message("Không có bé bạn cần tìm")
                    .build());
        return ResponseEntity.ok(animalService.findAnimalByAnimalID(animalID));
    }

    @GetMapping("/getAnimalsByShelterID/{shelterID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAnimalsByShelterID(@PathVariable String shelterID){
        return ResponseEntity.ok(animalService.findAnimalsByShelterID(shelterID));
    }
    @PostMapping("/addAnimal")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> addAnimal(@RequestBody AnimalDTO animal){
        Animal tempAnimal = animalService.findAnimalByAnimalNameAndShelterID(animal.getAnimalName(), animal.getShelterID());

        if(tempAnimal != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Đã tồn tại bé có cùng tên trong trại")
                    .build());
        }

        if (!animalService.addAnimal(animal))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Có lỗi xảy ra khi thêm thông tin cho bé")
                    .build());

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Thông tin của bé đã được thêm thành công")
                .build());
    }

    @PutMapping("/updateAnimal")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> updateAnimal(@RequestBody Animal animal){
        Shelter tempShelter = shelterService.findShelterByShelterID(animal.getShelterID());

        if(tempShelter == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Không tồn tại trại cứu trợ")
                    .build());

        Animal tempAnimal = animalService.findAnimalByAnimalNameAndShelterID(animal.getAnimalName(), animal.getShelterID());

        if (!animalService.updateAnimal(animal))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Có lỗi xảy ra khi cập nhật thông tin thú cưng")
                    .build());

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Thông tin của bé đã được chỉnh sửa thành công")
                .build());
    }

    @DeleteMapping("/deleteAnimal/{animalID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> deleteAnimal(@PathVariable String animalID){
        Animal animal = animalService.findAnimalByAnimalID(animalID);

        if (animal == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Bé không tồn tại")
                    .build());

        if (!animalService.deleteAnimal(animal))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Có lỗi xảy ra khi xóa thông tin của bé")
                    .build());

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Thông tin của bé đã được xóa thành công")
                .build());
    }
}
