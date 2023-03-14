package com.pescue.pescue.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Document("GoogleUser")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleUser implements UserDetails, OAuth2User {
    @Id
    private String userID;
    @Indexed(unique = true)
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userAvatar;
    private List<Role> userRoles;

    public GoogleUser(String userEmail, String userFirstName, String userLastName, String userAvatar) {
        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userAvatar = userAvatar;
    }

    public GoogleUser(String userEmail, String userFirstName, String userLastName, String userAvatar, List<Role> roles) {
        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userAvatar = userAvatar;
        this.userRoles = roles;
    }

    public GoogleUser(OAuth2User oAuth2User){
        this.userEmail = oAuth2User.getAttribute("email");
        this.userFirstName = oAuth2User.getAttribute("given_name");
        this.userLastName = oAuth2User.getAttribute("family_name");
        this.userAvatar = oAuth2User.getAttribute("picture");
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public List<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<Role> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return userFirstName + " " + userLastName;
    }
}
