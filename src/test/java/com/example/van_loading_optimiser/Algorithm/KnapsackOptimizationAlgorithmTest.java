package com.example.van_loading_optimiser.Algorithm;

import com.example.van_loading_optimiser.Dtos.ShipmentDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KnapsackOptimizationAlgorithmTest {

    private final KnapsackOptimizationAlgorithm service = new KnapsackOptimizationAlgorithm();

    @Test
    void shouldSelectBestCombination() {
        List<ShipmentDto> shipments = List.of(
                new ShipmentDto("Parcel A", 5, 120),
                new ShipmentDto("Parcel B", 10, 200),
                new ShipmentDto("Parcel C", 3, 80),
                new ShipmentDto("Parcel D", 8, 160)
        );

        KnapsackOptimizationAlgorithm.Result result = service.optimize(shipments, 15);
   Assertions.assertEquals(15, result.totalVolume());
    Assertions.assertEquals(320, result.totalRevenue());
   Assertions.assertEquals(2, result.selectedShipments().size());
   Assertions.assertEquals("Parcel A", result.selectedShipments().get(0).getName());
   Assertions.assertEquals("Parcel B", result.selectedShipments().get(1).getName());
    }

    @Test
    void shouldReturnEmptyWhenNothingFits() {
        List<ShipmentDto> shipments = List.of(
                new ShipmentDto("Parcel A", 20, 120),
                new ShipmentDto("Parcel B", 30, 200)
        );

        KnapsackOptimizationAlgorithm.Result result = service.optimize(shipments, 10);

        Assertions.assertTrue(result.selectedShipments().isEmpty());
        Assertions.assertEquals(0, result.totalVolume());
        Assertions.assertEquals(0, result.totalRevenue());
    }

    @Test
    void shouldReturnEmptyWhenCapacityIsZero() {
        List<ShipmentDto> shipments = List.of(
                new ShipmentDto("Parcel A", 5, 120)
        );

        KnapsackOptimizationAlgorithm.Result result = service.optimize(shipments, 0);

        Assertions.assertTrue(result.selectedShipments().isEmpty());
        Assertions.assertEquals(0, result.totalVolume());
        Assertions.assertEquals(0, result.totalRevenue());
    }

}