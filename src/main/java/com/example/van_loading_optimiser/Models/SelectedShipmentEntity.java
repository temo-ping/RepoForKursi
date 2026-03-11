package com.example.van_loading_optimiser.Models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "selected_shipments")
@Getter
@Setter
public class SelectedShipmentEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    @Column(name = "shipment_name", nullable = false)
    private String shipmentName;

    @Column(name = "volume",nullable = false)
    private int volume;
    @Column(name = "revenue", nullable = false)
    private int revenue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "optimization_request_id", nullable = false)
    private OptimizationRequestEntity optimizationRequest;

}
