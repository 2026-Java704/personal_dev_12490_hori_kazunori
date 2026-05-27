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

	public UsersController(HttpSession session, Account account, UsersRepository usersRepository,
			CategoriesRepository categoriesRepository, TasksRepository tasksRepository) {
		this.session = session;
		this.account = account;
		this.usersRepository = usersRepository;
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
			@RequestParam(defaultValue = "") String email,
			Model model) {
		// エラー
		List<String> errorList = new ArrayList<>();
		if (name.equals("") || name.length() == 0) {
			errorList.add("名前を入力してください");
		} else if (name.length() > 20) {
			errorList.add("名前は20文字以下で入力してください");
		}
		if (password.equals("") || password.length() == 0) {
			errorList.add("パスワードを入力してください");
		}
		if (email.equals("")) {
			errorList.add("メールアドレスを入力してください");
		}

		if (errorList.size() == 0) {
			if (usersRepository.findByNameAndPasswordAndEmail(name, password, email).size() == 0) {
				errorList.add("もう一度入力してください");
			}
		}

		if (errorList.size() > 0) {
			model.addAttribute("errorList", errorList);
			model.addAttribute("name", name);
			model.addAttribute("email", email);
			return "login";
		}

		account.setUserId(usersRepository.findByNameAndPasswordAndEmail(name, password, email).getFirst().getUserId());
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
			@RequestParam(defaultValue = "") String email,
			Model model) {
		// エラー
		List<String> errorList = new ArrayList<>();
		List<Users> usersList = usersRepository.findAll();
		if (name.equals("") || name.length() == 0) {
			errorList.add("名前を入力してください");
		} else if (name.length() > 20) {
			errorList.add("名前は20文字以下で入力してください");
		}
		if (email.equals("")) {
			errorList.add("メールアドレスを入力してください");
		} else {
			for (int i = 0; i < usersList.size(); i++) {
				if (email.equals(usersList.get(i).getEmail())) {
					errorList.add("入力されたメールアドレスはすでに登録されています");
				}
			}
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
			model.addAttribute("email", email);
			return "accountForm";
		}

		Users users = new Users(name, password, email);
		usersRepository.save(users);

		return "redirect:/login";
	}

	@GetMapping("/logout")
	public String logout() {

		return "redirect:/login";
	}

}
