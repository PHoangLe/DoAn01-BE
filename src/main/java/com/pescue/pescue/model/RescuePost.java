package com.pescue.pescue.model;

import com.pescue.pescue.dto.RescuePostDTO;
import com.pescue.pescue.model.constant.RescuePostStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("RescuePost")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RescuePost {
    @Id
    private String rescuePostID;
    private String[] images;
    @DBRef
    private User poster;
    @DBRef
    private Shelter rescuer;
    private String animalDescription;
    private String locationDescription;
    private String street;
    private String ward;
    private String district;
    private String city;
    private Date date = new Date(System.currentTimeMillis());
    private RescuePostStatus status = RescuePostStatus.WAITING;

    public RescuePost(
            String[] images, User poster,
            String animalDescription, String locationDescription,
            String street, String ward, String district, String city){
        this.images = images;
        this.poster = poster;
        this.rescuer = null;
        this.animalDescription = animalDescription;
        this.locationDescription = locationDescription;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.city = city;
    }

    public void setDTO(RescuePostDTO dto) {
        this.images = dto.getImages();
        this.animalDescription = dto.getAnimalDescription();
        this.locationDescription = dto.getLocationDescription();
        this.street = dto.getStreet();
        this.ward = dto.getWard();
        this.district = dto.getDistrict();
        this.city = dto.getCity();
    }
}
