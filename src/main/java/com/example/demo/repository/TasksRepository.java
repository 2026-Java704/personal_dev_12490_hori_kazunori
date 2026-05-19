package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Tasks;

public interface TasksRepository extends JpaRepository<Tasks, Integer> {
	List<Tasks> findByUserIdOrderByClosingDateDesc(Integer userId);
}
