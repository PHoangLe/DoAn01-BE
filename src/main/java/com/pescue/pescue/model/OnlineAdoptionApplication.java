package com.pescue.pescue.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;

@Document("OnlineAdoptionApplication")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OnlineAdoptionApplication extends AdoptionApplication{
    private Date expiry;
    public OnlineAdoptionApplication(Animal animal, Shelter shelter, User user){
        super(animal, shelter, user);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(System.currentTimeMillis()));
        cal.add(Calendar.MONTH, 1);
        this.expiry = cal.getTime();
    }
}
