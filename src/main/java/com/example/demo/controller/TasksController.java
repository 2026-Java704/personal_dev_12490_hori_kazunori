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

	private final TasksRepository tasksRepository;

	public TasksController(HttpSession session, Account account, UsersRepository usersRepository,
			CategoriesRepository categoriesRepository, TasksRepository tasksRepository) {
		this.session = session;
		this.account = account;
		this.tasksRepository = tasksRepository;
	}

	//	一覧表示
	@GetMapping("/tasks")
	public String index(@RequestParam(defaultValue = "0") Integer categoryId,
			Model model) {
		List<Tasks> taskList = null;
		if (categoryId == 0) {
			taskList = tasksRepository.findByUserIdOrderByDateAsc(account.getUserId());
		} else {
			taskList = tasksRepository.findByUserIdAndCategoryIdOrderByDateAsc(account.getUserId(), categoryId);
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
	public String edit(@PathVariable Integer taskId, @RequestParam Integer categoryId,
			@RequestParam(defaultValue = "") String title,
			@DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate date,
			@DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate closingDate,
			@RequestParam(defaultValue = "") Integer time,
			@RequestParam(defaultValue = "") String memo,
			@RequestParam(defaultValue = "") Integer progress,
			Model model) {
		Tasks task = new Tasks(account.getUserId(), taskId, categoryId, title, date, closingDate, progress, time, memo);
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
		tasksRepository.deleteByUserIdAndTaskId(account.getUserId(), taskId);

		return "redirect:/tasks";
	}

	//	完了済みタスク一括削除
	@PostMapping("/tasks/delete-completed")
	public String deleteCompleted() {
		tasksRepository.deleteByUserIdAndProgress(account.getUserId(), 2);

		return "redirect:/tasks";
	}

	//	選択されたタスク一括進捗更新(一段階)
	@PostMapping("/tasks/batch-update")
	public String batchUpdate(@RequestParam(required = false) List<Integer> selectedTasksId) {
		//		データが来たか判別
		if (selectedTasksId != null && !selectedTasksId.isEmpty()) {
			for (Integer taskId : selectedTasksId) {
				// 現在の自分のタスクを取得
				Tasks task = tasksRepository.findByUserIdAndTaskId(account.getUserId(), taskId);
				//				受け取ったタスクがあったなら処理を行う
				if (task != null) {
					Integer currentProgress = task.getProgress();
					// 未着手なら進行中に、進行中なら完了に進める
					if (currentProgress == 0 || currentProgress == 1) {
						task.setProgress(currentProgress + 1);
						tasksRepository.save(task);
					}
				}
			}
		}
		return "redirect:/tasks";
	}

	//	選択されたタスク一括削除
	@PostMapping("/tasks/batch-delete")
	public String batchDelete(@RequestParam(required = false) List<Integer> selectedTasksId) {
		//		データが来たか判別
		if (selectedTasksId != null && !selectedTasksId.isEmpty()) {
			for (Integer taskId : selectedTasksId) {
				// 自分のユーザーIDに紐づくタスクのみ削除(個別削除を複数回)
				tasksRepository.deleteByUserIdAndTaskId(account.getUserId(), taskId);
			}
		}
		return "redirect:/tasks";
	}

}
