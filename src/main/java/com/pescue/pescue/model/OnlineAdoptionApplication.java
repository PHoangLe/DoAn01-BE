package com.pescue.pescue.model;

import com.pescue.pescue.dto.AdoptionApplicationDTO;
import com.pescue.pescue.dto.AdoptionApplicationRequestDTO;
import com.pescue.pescue.dto.UserDTO;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;

@Document("OnlineAdoptionApplication")
@AllArgsConstructor
@Getter
@Setter
public class OnlineAdoptionApplication extends AdoptionApplication{
    private Date expiry;
    public OnlineAdoptionApplication(AdoptionApplicationRequestDTO dto, Animal animal, Shelter shelter, UserDTO userDTO){
        super(dto, animal, shelter, userDTO);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        cal.add(Calendar.MONTH, 1);
        this.expiry = cal.getTime();
    }
}
