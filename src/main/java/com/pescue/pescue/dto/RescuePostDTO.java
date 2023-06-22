package com.pescue.pescue.dto;

import com.pescue.pescue.model.Shelter;
import com.pescue.pescue.model.User;
import com.pescue.pescue.model.constant.RescuePostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RescuePostDTO {
    private String[] images;
    private String userID;
    private String animalDescription;
    private String locationDescription;
    private String street;
    private String ward;
    private String district;
    private String city;
}
