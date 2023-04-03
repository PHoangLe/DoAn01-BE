package com.pescue.pescue.controller;

import com.pescue.pescue.dto.AnimalDTO;
import com.pescue.pescue.dto.StringResponseDTO;
import com.pescue.pescue.model.Animal;
import com.pescue.pescue.model.Shelter;
import com.pescue.pescue.service.AnimalService;
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

    @GetMapping("/getAllAnimals")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllAnimals(){
        return ResponseEntity.ok(animalService.findAllAnimals());
    }

    @GetMapping("/getAnimalsByShelterID")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    public ResponseEntity<Object> getAnimalsByShelterID(@RequestBody String shelterID){
        return ResponseEntity.ok(animalService.findAnimalsByShelterID(shelterID));
    }

    @PostMapping("/addAnimal")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    public ResponseEntity<Object> addAnimal(@RequestBody AnimalDTO animal){
        Animal tempAnimal = animalService.findAnimalByAnimalNameAndShelterID(animal.getAnimalName(), animal.getShelterID());

        if(tempAnimal != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Đã tồn tại thú cưng cùng tên trong trại")
                    .build());
        }

        if (!animalService.addAnimal(animal))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Có lỗi xảy ra khi thêm thông tin thú cưng")
                    .build());

        return ResponseEntity.ok("Thông tin của thú nuôi đã được thêm thành công");
    }

    @PostMapping("/updateAnimal")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    public ResponseEntity<Object> updateAnimal(@RequestBody Animal animal){
        Animal tempAnimal = animalService.findAnimalByAnimalNameAndShelterID(animal.getAnimalName(), animal.getShelterID());

        if(tempAnimal != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Đã tồn tại thú cưng cùng tên trong trại")
                    .build());
        }

        if (!animalService.updateAnimal(animal))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Có lỗi xảy ra khi cập nhật thông tin thú cưng")
                    .build());

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Thông tin của thú nuôi đã chỉnh sửa thành công")
                .build());
    }

    @DeleteMapping("/deleteAnimal")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    public ResponseEntity<Object> deleteAnimal(@RequestBody Animal animal){
        if (!animalService.deleteAnimal(animal))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StringResponseDTO.builder()
                    .message("Có lỗi xảy ra khi xóa thông tin thú cưng")
                    .build());

        return ResponseEntity.ok(StringResponseDTO.builder()
                .message("Thông tin của thú nuôi đã được xóa thành công")
                .build());
    }
}
