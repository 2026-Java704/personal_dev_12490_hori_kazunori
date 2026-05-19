package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class Tasks {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "task_id")
	private Integer taskId;

	@Column(name = "user_id")
	private Integer userId;

	private String title;

	//	期限
	@Column(name = "closing_date")
	private LocalDate closingDate;

	//	進捗状況
	private Integer progress;

	private String memo;

	//	予定所要時間
	private Integer time;

	//	登録実行した日付
	private LocalDate date;

	public Integer getTaskId() {
		return taskId;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getTitle() {
		return title;
	}

	public LocalDate getClosingDate() {
		return closingDate;
	}

	public Integer getProgress() {
		return progress;
	}

	public String getMemo() {
		return memo;
	}

	public Integer getTime() {
		return time;
	}

	public LocalDate getDate() {
		return date;
	}

}
