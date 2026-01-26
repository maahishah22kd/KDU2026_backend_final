package com.example.smarthomedevicesystemmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeviceRequest {
    @Pattern(
            regexp = "^[0-9A-Fa-f]{6}$",
            message = "kickstonId must be a 6-digit hexadecimal value"
    )
    @NotBlank(message = "kickstonId is required")
    @Size(min = 1, max = 100, message = "kickstonId is invalid")
    private String kickstonId;

    @NotBlank(message = "deviceUsername is required")
    @Size(min = 1, max = 100, message = "deviceUsername is invalid")
    private String deviceUsername;

    @NotBlank(message = "devicePassword is required")
    @Size(min = 1, max = 100, message = "devicePassword is invalid")
    private String devicePassword;
}
