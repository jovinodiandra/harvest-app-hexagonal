package org.harvest.springhttpadapter.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.harvest.application.dto.result.LoginResult;

import java.util.UUID;


public record LoginResponse(String accessToken,String tokenType,User user, Organization organization) {
 record User(UUID userId, String name, String email, String role ){

 }
 record Organization(UUID organizationId, String organizationName){}
 public static LoginResponse from(LoginResult result){
     User userResponse = new User(result.useerId(), result.name(), result.email(), result.role().getDescription());
     Organization organizationResponse = new Organization(result.organizationId(), result.organizationName());
     return new LoginResponse(result.sessionId(),"Bearer", userResponse, organizationResponse);
 }
}

