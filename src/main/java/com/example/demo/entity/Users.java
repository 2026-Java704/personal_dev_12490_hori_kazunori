package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

	private String name;

	private String password;

	public Users() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public Users(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

}
