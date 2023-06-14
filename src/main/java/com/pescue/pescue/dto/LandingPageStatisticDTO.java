package com.pescue.pescue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LandingPageStatisticDTO {
    private long shelterCount;
    private long adoptedAnimalCount;
    private long notAdoptedAnimalCount;
}
