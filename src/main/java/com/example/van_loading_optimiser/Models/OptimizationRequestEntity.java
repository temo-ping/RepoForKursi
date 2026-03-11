package com.example.van_loading_optimiser.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "optimization_requests")
@Getter
@Setter
public class OptimizationRequestEntity {

    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID id;

    @Column(name = "max_volume", nullable = false)
    private int maxVolume;

    @Column(name = "total_volume", nullable = false)
    private int totalVolume;

    @Column(name = "total_revenue", nullable = false)
    private int totalRevenue;

    @Column(name = "created_at",nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "optimizationRequest", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<SelectedShipmentEntity> selectedShipments = new ArrayList<>();


}
