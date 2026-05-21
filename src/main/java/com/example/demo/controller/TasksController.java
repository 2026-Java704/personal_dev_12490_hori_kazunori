package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jakarta.servlet.http.HttpSession;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

	//	一覧表示
	@GetMapping("/tasks")
	public String index(@RequestParam(defaultValue = "3") Integer categoryId,
			Model model) {

		taskListView(categoryId, model);

		return "tasks";

	}

	//	タスク一新規作成画面遷移
	@GetMapping("/tasks/create")
	public String create(Model model) {
		model.addAttribute("task", new Tasks());
		return "taskForm";
	}

	//	タスク一新規作成
	@PostMapping("/tasks/create")
	public String register(@RequestParam Integer categoryId, @RequestParam(defaultValue = "") String title,
			@DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate date,
			@DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate closingDate,
			@RequestParam(defaultValue = "") Integer time,
			@RequestParam(defaultValue = "") String memo,
			@RequestParam(defaultValue = "") Integer progress,
			Model model) {
		Tasks task = new Tasks(account.getUserId(), categoryId, title, date, closingDate, progress, time, memo);
		// エラー
		List<String> errorList = new ArrayList<>();
		if (title.equals("")) {
			errorList.add("タイトルを入力してください");
		}
		if (date == null) {
			errorList.add("タスク開始日を入力してください");
		}
		if (closingDate == null) {
			errorList.add("期限を入力してください");
		}
		if (time == null) {
			errorList.add("予定所要時間を入力してください");
		}

		if (errorList.size() > 0) {
			model.addAttribute("errorList", errorList);
			model.addAttribute("task", task);
			return "taskForm";
		}

		tasksRepository.save(task);

		return "redirect:/tasks";
	}

	//	個別選択:更新画面遷移
	@GetMapping("/tasks/{taskId}/edit")
	public String edit(@PathVariable Integer taskId,
			Model model) {
		Tasks task = tasksRepository.findByUserIdAndTaskId(account.getUserId(), taskId);

		model.addAttribute("task", task);
		return "editTask";
	}

	//	タスク更新処理
	@PostMapping("/tasks/{taskId}/edit")
	public String edit(@RequestParam Integer categoryId, @RequestParam(defaultValue = "") String title,
			@DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate date,
			@DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate closingDate,
			@RequestParam(defaultValue = "") Integer time,
			@RequestParam(defaultValue = "") String memo,
			@RequestParam(defaultValue = "") Integer progress,
			Model model) {
		Tasks task = new Tasks(account.getUserId(), categoryId, title, date, closingDate, progress, time, memo);
		// エラー
		List<String> errorList = new ArrayList<>();
		if (title.equals("")) {
			errorList.add("タイトルを入力してください");
		}
		if (date == null) {
			errorList.add("タスク開始日を入力してください");
		}
		if (closingDate == null) {
			errorList.add("期限を入力してください");
		}
		if (time == null) {
			errorList.add("予定所要時間を入力してください");
		}

		if (errorList.size() > 0) {
			model.addAttribute("errorList", errorList);
			model.addAttribute("task", task);
			return "editTask";
		}

		tasksRepository.save(task);
		return "redirect:/tasks";
	}

	//	個別選択:タスク削除
	@PostMapping("/tasks/{taskId}/delete")
	public String delete(@PathVariable Integer taskId) {
		tasksRepository.deleteById(taskId);

		return "redirect:/tasks";
	}

	//	カテゴリーで表示制限、日付でソートするメソッド
	public void taskListView(Integer categoryId, Model model) {
		List<Tasks> taskList = null;
		if (categoryId == 3) {
			taskList = tasksRepository.findByUserIdOrderByDateAsc(account.getUserId());
		} else {
			taskList = tasksRepository.findByUserIdAndCategoryId(account.getUserId(), categoryId);
		}

		// 古い順にソートされるMapを用意
		Map<LocalDate, List<Tasks>> tasksByDate = new TreeMap<>();

		if (taskList != null && !taskList.isEmpty()) {
			for (Tasks task : taskList) {
				LocalDate startDate = task.getDate();
				LocalDate endDate = task.getClosingDate();

				if (startDate == null || endDate == null) {
					continue;
				}

				// 開始日から期限日まで1日Mapに登録
				LocalDate current = startDate;
				while (!current.isAfter(endDate)) {
					tasksByDate.computeIfAbsent(current, k -> new ArrayList<>()).add(task);
					current = current.plusDays(1);
				}
			}
		}

		// 画面に日付ごとのデータを渡す
		model.addAttribute("tasksByDate", tasksByDate);
		// データがあればtrue
		model.addAttribute("showContent", tasksByDate != null && !tasksByDate.isEmpty());
	}

}
