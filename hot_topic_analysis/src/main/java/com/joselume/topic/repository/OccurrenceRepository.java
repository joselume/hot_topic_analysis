package com.joselume.topic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joselume.topic.entity.Occurrence;
import com.joselume.topic.entity.Request;

public interface OccurrenceRepository extends JpaRepository<Occurrence, Long> {
	List<Occurrence> findFirst3ByRequestOrderByCounterDesc (Request request);
}
