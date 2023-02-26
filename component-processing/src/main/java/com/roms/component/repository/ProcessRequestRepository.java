package com.roms.component.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.roms.component.entity.ProcessRequest;

public interface ProcessRequestRepository extends JpaRepository<ProcessRequest, Integer> {

}
