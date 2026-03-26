package com.example.smarthomedevicesystemmanagement.repository;

import com.example.smarthomedevicesystemmanagement.entity.DeviceInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceInventoryRepository extends JpaRepository<DeviceInventory,String> {
}
