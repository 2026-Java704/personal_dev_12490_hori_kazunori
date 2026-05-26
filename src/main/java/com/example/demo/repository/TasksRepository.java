package com.example.demo.repository;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Tasks;

public interface TasksRepository extends JpaRepository<Tasks, Integer> {
	List<Tasks> findByUserIdOrderByClosingDateAsc(Integer userId);

	List<Tasks> findByUserIdOrderByDateAsc(Integer userId);

	List<Tasks> findByUserIdOrderByDateDesc(Integer userId);

	List<Tasks> findByUserIdAndCategoryIdOrderByDateAsc(Integer userId, Integer categoryId);

	List<Tasks> findByUserIdAndCategoryIdOrderByDateDesc(Integer userId, Integer categoryId);

	Tasks findByUserIdAndTaskId(Integer userId, Integer taskId);

	@Transactional
	void deleteByUserIdAndTaskId(Integer userId, Integer taskId);

	@Transactional
	void deleteByUserIdAndProgress(Integer userId, Integer progress);
}
