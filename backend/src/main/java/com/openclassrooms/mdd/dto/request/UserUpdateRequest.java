package com.openclassrooms.mdd.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    
    @Size(min = 3, max = 20)
    private String username;
    
    @Email
    private String email;
    
    @Size(min = 8, max = 120)
    private String password;
}
