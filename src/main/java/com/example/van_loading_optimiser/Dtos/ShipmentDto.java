package com.example.van_loading_optimiser.Dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDto {

    @NotBlank(message = "Shipment name must not be blank")
    private String name;

    @Min(value = 1, message = "Shipment volume must be greater than 0")
    private int volume;

    @Min(value = 0, message = "Shipment revenue must be greater than or equal to 0")
    private int revenue;

}
