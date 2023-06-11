package com.pescue.pescue.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonationDTO {
    private String userID;
    private String fundID;
    private Integer numsOfPackage;
}
