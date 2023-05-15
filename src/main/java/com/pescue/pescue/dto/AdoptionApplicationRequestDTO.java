package com.pescue.pescue.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdoptionApplicationRequestDTO {
    private String animalID;
    private String shelterID;
    private String userID;
}
