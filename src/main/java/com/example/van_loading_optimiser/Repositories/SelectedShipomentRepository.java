package com.example.van_loading_optimiser.Repositories;

import com.example.van_loading_optimiser.Models.SelectedShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SelectedShipomentRepository extends JpaRepository<SelectedShipmentEntity, Long> {

}
