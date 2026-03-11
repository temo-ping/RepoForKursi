package com.example.van_loading_optimiser.Repositories;

import com.example.van_loading_optimiser.Models.OptimizationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OptimizationRequestRepository extends JpaRepository<OptimizationRequestEntity, UUID> {
}
