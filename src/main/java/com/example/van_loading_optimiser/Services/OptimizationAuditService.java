package com.example.van_loading_optimiser.Services;

import com.example.van_loading_optimiser.Dtos.OptimizationResponseDto;
import com.example.van_loading_optimiser.Dtos.ShipmentDto;
import com.example.van_loading_optimiser.ExceptionHandling.Exceptions.ResourceNotFoundException;
import com.example.van_loading_optimiser.Models.OptimizationRequestEntity;
import com.example.van_loading_optimiser.Models.SelectedShipmentEntity;
import com.example.van_loading_optimiser.Repositories.OptimizationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OptimizationAuditService {


    private final OptimizationRequestRepository optimizationRequestRepository;

    public OptimizationResponseDto saveOptimization(int maxVolume, List<ShipmentDto> selectedShipments, int totalVolume, int totalRevenue) {


        OptimizationRequestEntity entity = new OptimizationRequestEntity();
        entity.setMaxVolume(maxVolume);
        entity.setTotalVolume(totalVolume);
        entity.setTotalRevenue(totalRevenue);
        entity.setCreatedAt(Instant.now());

        for(ShipmentDto shipment: selectedShipments)
        {

            SelectedShipmentEntity ssEntity = new SelectedShipmentEntity();
            ssEntity.setShipmentName(shipment.getName());
            ssEntity.setVolume(shipment.getVolume());
            ssEntity.setRevenue(shipment.getRevenue());
            ssEntity.setOptimizationRequest(entity);
            entity.getSelectedShipments().add(ssEntity);
        }

        OptimizationRequestEntity saved = optimizationRequestRepository.save(entity);
        return mapToDto(saved);
    }


    public OptimizationResponseDto getById(UUID requestId) {
        OptimizationRequestEntity entity = optimizationRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Optimization request not found: " + requestId));

        return mapToDto(entity);
    }

    public List<OptimizationResponseDto> getAll() {
        return optimizationRequestRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private OptimizationResponseDto mapToDto(OptimizationRequestEntity entity) {
        List<ShipmentDto> shipments = entity.getSelectedShipments()
                .stream()
                .map(s -> new ShipmentDto(s.getShipmentName(), s.getVolume(), s.getRevenue()))
                .toList();

        return OptimizationResponseDto.builder()
                .requestId(entity.getId())
                .selectedShipments(shipments)
                .totalVolume(entity.getTotalVolume())
                .totalRevenue(entity.getTotalRevenue())
                .createdAt(entity.getCreatedAt())
                .build();
    }


}
