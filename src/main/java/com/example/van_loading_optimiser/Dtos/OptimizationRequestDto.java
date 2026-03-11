package com.example.van_loading_optimiser.Dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptimizationRequestDto {
    @Min(value = 0, message = "maxVolume must be greater than or equal to 0")
    private int maxVolume;

    @Valid
    @NotEmpty(message = "availableShipments must not be empty")
    private List<ShipmentDto> availableShipments;
}
