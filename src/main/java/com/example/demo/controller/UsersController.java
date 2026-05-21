package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Users;
import com.example.demo.model.Account;
import com.example.demo.repository.CategoriesRepository;
import com.example.demo.repository.TasksRepository;
import com.example.demo.repository.UsersRepository;

@Controller
public class UsersController {

	private final HttpSession session;
	private final Account account;

	private final UsersRepository usersRepository;
	private final CategoriesRepository categoriesRepository;
	private final TasksRepository tasksRepository;

	public UsersController(HttpSession session, Account account, UsersRepository usersRepository,
			CategoriesRepository categoriesRepository, TasksRepository tasksRepository) {
		this.session = session;
		this.account = account;
		this.usersRepository = usersRepository;
		this.categoriesRepository = categoriesRepository;
		this.tasksRepository = tasksRepository;
	}

	//	始まりの画面(ログイン)表示
	@GetMapping({ "/", "/login" })
	public String index() {
		// セッション情報を全てクリアする
		session.invalidate();

		return "login";
	}

	//	ログイン処理+タスク一覧ページ表示
	@PostMapping("/login")
	public String login(@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String password,
			Model model) {
		// エラー
		List<String> errorList = new ArrayList<>();
		if (name.equals("") || name.length() == 0) {
			errorList.add("名前を入力してください");
		}
		if (password.equals("") || password.length() == 0) {
			errorList.add("パスワードを入力してください");
		}
		if (errorList.size() == 0) {
			if (usersRepository.findByNameAndPassword(name, password).size() == 0) {
				errorList.add("名前とパスワードが一致しませんでした");
			}
		}

		if (errorList.size() > 0) {
			model.addAttribute("errorList", errorList);
			return "login";
		}

		account.setUserId(usersRepository.findByNameAndPassword(name, password).getFirst().getUserId());
		account.setName(name);

		return "redirect:/tasks";
	}

	//	新規ユーザー登録画面表示
	@GetMapping("/users/add")
	public String create() {
		session.invalidate();
		return "accountForm";
	}

	//	新規ユーザー登録処理
	@PostMapping("/users/add")
	public String register(
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "") String password,
			@RequestParam(defaultValue = "") String passwordConfirm,
			Model model) {
		// エラー
		List<String> errorList = new ArrayList<>();
		if (name.equals("") || name.length() == 0) {
			errorList.add("名前を入力してください");
		}
		if (password.equals("") || password.length() == 0) {
			errorList.add("パスワードを入力してください");
		}
		if (passwordConfirm.equals("") || passwordConfirm.length() == 0) {
			errorList.add("確認用パスワードを入力してください");
		}
		if (errorList.size() == 0) {
			if (!password.equals(passwordConfirm)) {
				errorList.add("パスワードと確認パスワードが一致しませんでした");
			}
		}

		if (errorList.size() > 0) {
			model.addAttribute("errorList", errorList);
			model.addAttribute("name", name);
			return "accountForm";
		}

		Users users = new Users(name, password);
		usersRepository.save(users);

		return "redirect:/login";
	}

	@GetMapping("/logout")
	public String logout() {

		return "redirect:/login";
	}

}
