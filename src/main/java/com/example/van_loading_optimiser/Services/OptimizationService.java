package com.example.van_loading_optimiser.Services;

import com.example.van_loading_optimiser.Algorithm.KnapsackOptimizationAlgorithm;
import com.example.van_loading_optimiser.Dtos.OptimizationRequestDto;
import com.example.van_loading_optimiser.Dtos.OptimizationResponseDto;
import com.example.van_loading_optimiser.Dtos.ShipmentDto;
import com.example.van_loading_optimiser.ExceptionHandling.Exceptions.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OptimizationService {

    private final KnapsackOptimizationAlgorithm knapsackOptimizationService;
    private final OptimizationAuditService optimizationAuditService;

    public OptimizationResponseDto optimize(OptimizationRequestDto request) {
        validateBusinessRules(request);

        List<ShipmentDto> shipments = request.getAvailableShipments();
        KnapsackOptimizationAlgorithm.Result result =
                knapsackOptimizationService.optimize(shipments, request.getMaxVolume());

        return optimizationAuditService.saveOptimization(
                request.getMaxVolume(),
                result.selectedShipments(),
                result.totalVolume(),
                result.totalRevenue()
        );
    }

    public OptimizationResponseDto getById(UUID requestId) {
        return optimizationAuditService.getById(requestId);
    }

    public List<OptimizationResponseDto> getAll() {
        return optimizationAuditService.getAll();
    }

    private void validateBusinessRules(OptimizationRequestDto request) {
        if (request.getAvailableShipments() == null) {

            throw new InvalidRequestException("availableShipments must not be null");
        }


        boolean duplicateNames = request.getAvailableShipments()
                .stream()
                .map(ShipmentDto::getName)
                .map(String::trim)
                .map(String::toLowerCase)
                .distinct()
                .count() != request.getAvailableShipments().size();

        if (duplicateNames) {

            throw new InvalidRequestException("Shipment names must be unique in one request");
        }
    }

}
