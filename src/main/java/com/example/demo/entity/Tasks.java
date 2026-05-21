package com.example.demo.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

	@Column(name = "category_id")
	private Integer categoryId;

	private String title;

	//	期限
	@Column(name = "closing_date")
	private LocalDate closingDate;

	//	進捗状況
	private Integer progress;

	private String memo;

	//	予定所要時間
	private Integer time;

	//	タスク開始日
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

	public String getClosingDateFormatted() {
		if (this.closingDate == null) {
			return "";
		}
		// 表示形式を定義
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return this.closingDate.format(formatter);
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

	public Integer getCategoryId() {
		return categoryId;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getDateFormatted() {
		if (this.date == null) {
			return "";
		}
		// 表示形式を定義
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return this.date.format(formatter);
	}

}
