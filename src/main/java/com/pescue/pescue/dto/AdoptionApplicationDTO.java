package com.pescue.pescue.dto;

import com.pescue.pescue.model.AdoptionApplication;
import com.pescue.pescue.model.Animal;
import com.pescue.pescue.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdoptionApplicationDTO {
    private AdoptionApplication application;
    private User user;
    private Animal animal;
}
