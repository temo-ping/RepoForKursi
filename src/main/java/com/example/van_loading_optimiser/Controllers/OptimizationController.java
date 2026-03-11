package com.example.van_loading_optimiser.Controllers;

import com.example.van_loading_optimiser.Dtos.OptimizationRequestDto;
import com.example.van_loading_optimiser.Dtos.OptimizationResponseDto;
import com.example.van_loading_optimiser.Services.OptimizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/optimizations")
@RequiredArgsConstructor
public class OptimizationController {

    private final OptimizationService optimizationServicejust;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public OptimizationResponseDto optimize(@Valid @RequestBody OptimizationRequestDto request) {
        return optimizationServicejust.optimize(request);
    }

    @GetMapping("/{requestId}")
    public OptimizationResponseDto getById(@PathVariable UUID requestId) {
        return optimizationServicejust.getById(requestId);
    }

    @GetMapping
    public List<OptimizationResponseDto> getAll() {
        return optimizationServicejust.getAll();
    }


}
