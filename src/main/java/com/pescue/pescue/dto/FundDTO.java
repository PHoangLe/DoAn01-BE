package com.pescue.pescue.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FundDTO {
    private String fundName;
    private String fundCover;
    private String fundDescription;
}
