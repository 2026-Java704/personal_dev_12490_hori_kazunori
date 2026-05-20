package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Tasks;
import com.example.demo.model.Account;
import com.example.demo.repository.CategoriesRepository;
import com.example.demo.repository.TasksRepository;
import com.example.demo.repository.UsersRepository;

@Controller
public class TasksController {

	private final HttpSession session;
	private final Account account;

	private final UsersRepository usersRepository;
	private final CategoriesRepository categoriesRepository;
	private final TasksRepository tasksRepository;

	public TasksController(HttpSession session, Account account, UsersRepository usersRepository,
			CategoriesRepository categoriesRepository, TasksRepository tasksRepository) {
		this.session = session;
		this.account = account;
		this.usersRepository = usersRepository;
		this.categoriesRepository = categoriesRepository;
		this.tasksRepository = tasksRepository;
	}

	@GetMapping("/tasks")
	public String index(@RequestParam(defaultValue = "") Integer category,
			Model model) {
		List<Tasks> taskList = null;
		if (category == 3) {
			taskList = tasksRepository.findByUserIdOrderByDateAsc(account.getUserId());
		} else {
			taskList = tasksRepository.findByUserIdAndCategoryId(account.getUserId(), category);
		}

		// 日付順（古い順）に自動ソートされるMapを用意
		Map<LocalDate, List<Tasks>> tasksByDate = new TreeMap<>();

		if (taskList != null && !taskList.isEmpty()) {
			for (Tasks task : taskList) {
				LocalDate startDate = task.getDate();
				LocalDate endDate = task.getClosingDate();

				if (startDate == null || endDate == null) {
					continue;
				}

				// 開始日から期限日まで1日ずつ展開してMapに登録
				LocalDate current = startDate;
				while (!current.isAfter(endDate)) {
					tasksByDate.computeIfAbsent(current, k -> new ArrayList<>()).add(task);
					current = current.plusDays(1);
				}
			}
		}

		// 画面に日付ごとのデータを渡す
		model.addAttribute("tasksByDate", tasksByDate);
		// データがあれば
		model.addAttribute("showContent", tasksByDate != null && !tasksByDate.isEmpty());

		//		List<Categories> categoryList = categoriesRepository.findAll();
		//		model.addAttribute("categories", categoryList);

		return "tasks";
	}

}
