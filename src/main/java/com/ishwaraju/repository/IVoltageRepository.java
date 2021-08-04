package com.ishwaraju.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ishwaraju.entity.Voltage;

@Repository
public interface IVoltageRepository extends JpaRepository<Voltage, Integer> {

}