package com.joselume.topic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joselume.topic.entity.News;

public interface NewsRepository extends JpaRepository<News, Long> {

}
