package com.karol.kindergartenmanagementsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.karol.kindergartenmanagementsystem.validation.ValidationMessages.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    @NotNull(message = EMAIL_REQUIRED)
    @NotBlank(message = EMAIL_REQUIRED)
    @Email(message = EMAIL_INVALID_FORMAT)
    private String email;
    @NotNull(message = EMAIL_INVALID_FORMAT)
    @NotBlank(message = PASSWORD_REQUIRED)
    @Size(min = 5, max = 40, message = PASSWORD_INVALID_LENGTH)
    private String password;
}
