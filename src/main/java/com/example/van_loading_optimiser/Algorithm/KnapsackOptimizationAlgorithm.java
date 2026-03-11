package com.example.van_loading_optimiser.Algorithm;

import com.example.van_loading_optimiser.Dtos.ShipmentDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KnapsackOptimizationAlgorithm {

    public Result optimize(List<ShipmentDto> shipments, int maxVolume) {
        int n = shipments.size();


        if (maxVolume == 0 || n == 0) {
            return new Result(Collections.emptyList(), 0, 0);
        }

        int[][] dpTable = new int[n + 1][maxVolume + 1];

        for (int i = 1; i <= n; i++) {
            ShipmentDto shipment = shipments.get(i - 1);

            for (int w = 0; w <= maxVolume; w++) {
                if (shipment.getVolume() > w) {
                    dpTable[i][w] = dpTable[i - 1][w];
                } else {
                    int skip = dpTable[i - 1][w];
                    int take = shipment.getRevenue() + dpTable[i - 1][w - shipment.getVolume()];
                    dpTable[i][w] = Math.max(skip, take);
                }
            }
        }

        List<ShipmentDto> selected = new ArrayList<>();
        int w = maxVolume;

        for (int i = n; i > 0; i--) {
            if (dpTable[i][w] != dpTable[i - 1][w]) {
                ShipmentDto shipment = shipments.get(i - 1);
                selected.add(shipment);
                w -= shipment.getVolume();
            }
        }

        Collections.reverse(selected);

        int totalVolume = selected.stream().mapToInt(ShipmentDto::getVolume).sum();
        int totalRevenue = selected.stream().mapToInt(ShipmentDto::getRevenue).sum();

        return new Result(selected, totalVolume, totalRevenue);
    }

    public record Result(List<ShipmentDto> selectedShipments, int totalVolume, int totalRevenue) {
    }

}
