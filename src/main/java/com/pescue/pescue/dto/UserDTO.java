package com.pescue.pescue.dto;

import com.pescue.pescue.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userID;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String phoneNo;
    private String userAvatar;

    public UserDTO(User user){
        this.userID = user.getUserID();
        this.userEmail = user.getUserEmail();
        this.userFirstName = user.getUserFirstName();
        this.userLastName = user.getUserLastName();
        this.phoneNo = user.getPhoneNo();
        this.userAvatar = user.getUserAvatar();
    }
}
