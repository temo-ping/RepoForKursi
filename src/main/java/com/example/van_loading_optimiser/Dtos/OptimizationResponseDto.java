package com.example.van_loading_optimiser.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptimizationResponseDto {

    private UUID requestId;
    private List<ShipmentDto> selectedShipments;
    private int totalVolume;
    private int totalRevenue;
    private Instant createdAt;
}
