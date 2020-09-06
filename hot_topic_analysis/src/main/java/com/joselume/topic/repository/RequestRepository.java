package com.joselume.topic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joselume.topic.entity.Request;

public interface RequestRepository extends JpaRepository<Request, Long>{

}
