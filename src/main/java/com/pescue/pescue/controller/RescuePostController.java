package com.pescue.pescue.controller;

import com.pescue.pescue.dto.RescuePostDTO;
import com.pescue.pescue.service.RescuePostService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/rescue-posts")
@CrossOrigin
@Api
public class RescuePostController {
    private final RescuePostService rescuePostService;
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> createPost(@RequestBody RescuePostDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(rescuePostService.createPost(dto));
    }
    @PutMapping("/{rescuePostID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> updatePost(@RequestBody RescuePostDTO dto, @PathVariable String rescuePostID){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(rescuePostService.updatePostWithDTO(rescuePostID, dto));
    }
    @DeleteMapping("/{rescuePostID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> updatePost(@PathVariable String rescuePostID){
        rescuePostService.deletePost(rescuePostID);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
    }
    @GetMapping("/{rescuePostID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllPostID(@PathVariable String rescuePostID){
        return ResponseEntity.status(HttpStatus.OK).body(rescuePostService.getByPostID(rescuePostID));
    }
    @GetMapping("/user/{userID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllByUserID(@PathVariable String userID){
        return ResponseEntity.status(HttpStatus.OK).body(rescuePostService.getAllByUserID(userID));
    }
    @GetMapping("/shelter-page/{shelterID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> getAllForShelterRescuePage(@PathVariable String shelterID){
        return ResponseEntity.status(HttpStatus.OK).body(rescuePostService.getAllForShelterRescuePage(shelterID));
    }
    @PutMapping("/process-post/{shelterID}/{rescuePostID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> processPost(@PathVariable String rescuePostID, @PathVariable String shelterID){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(rescuePostService.acceptRescuePost(shelterID, rescuePostID));
    }
    @PutMapping("/complete-post/{rescuePostID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> completePost(@PathVariable String rescuePostID){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(rescuePostService.completeRescuePost(rescuePostID));
    }
    @PutMapping("/abort-post/{rescuePostID}")
    @PreAuthorize("hasAuthority('ROLE_SHELTER_MANAGER')")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Object> abortPost(@PathVariable String rescuePostID){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(rescuePostService.abortRescuePost(rescuePostID));
    }
}
