package fr.epita.epitrello.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.epita.epitrello.datamodel.Task;
import fr.epita.epitrello.datamodel.TaskList;
import fr.epita.epitrello.datamodel.User;

public class DataService {

	private static final String SUCCESS = "Success!";
	private static final String FAILED = "Failed!";
	private static final String USER_EXISTED = "User already existed!";
	private static final String USER_NOT_EXISTED = "User does not exist!";
	private static final String TASK_EXISTED = "Task already existed!";
	private static final String TASK_NOT_EXISTED = "Task does not exist!";
	private static final String LIST_EXISTED = "List already existed!";
	private static final String LIST_NOT_EXISTED = "List does not exist!";

	private static Map<String, Task> tasks = new HashMap<String, Task>();
	private static Map<String, TaskList> taskLists = new HashMap<String, TaskList>();
	private static Map<String, User> users = new HashMap<String, User>();

	public DataService() {

	}

	public String addUser(String name) {
		if (users.containsKey(name)) {
			return USER_EXISTED;
		}

		User user = new User(name);
		users.put(name, user);

		if (users.containsKey(name)) {
			return SUCCESS;
		} else {
			return FAILED;
		}
	}

	public String addList(String name) {
		if (taskLists.containsKey(name)) {
			return LIST_EXISTED;
		}

		TaskList list = new TaskList(name);
		taskLists.put(name, list);

		if (taskLists.containsKey(name)) {
			return SUCCESS;
		} else {
			return FAILED;
		}
	}

	public String addTask(String listName, String name, int estimatedTime, int priority, String description) {
		if (tasks.containsKey(name)) {
			return TASK_EXISTED;
		}

		if (!taskLists.containsKey(listName)) {
			return LIST_NOT_EXISTED;
		}

		Task task = new Task(name, description, priority, estimatedTime);

		tasks.put(name, task);
		TaskList list = taskLists.get(listName);
		List<Task> tasksInList = list.getTasksInList();
		tasksInList.add(task);
		list.setTasksInList(tasksInList);

		if (tasks.containsKey(name) && taskLists.get(listName).getTasksInList().contains(task)) {
			return SUCCESS;
		} else {
			return FAILED;
		}
	}

	public String editTask(String name, int estimatedTime, int priority, String description) {
		if (!tasks.containsKey(name)) {
			return TASK_NOT_EXISTED;
		}

		Task task = tasks.get(name);

		task.setEstimatedTime(estimatedTime);
		task.setPriority(priority);
		task.setDescription(description);
		tasks.put(name, task);

		if (tasks.containsKey(name) && tasks.get(name) == task) {
			return SUCCESS;
		} else {
			return FAILED;
		}
	}

	public String assignTask(String taskName, String userName) {
		if (!tasks.containsKey(taskName)) {
			return TASK_NOT_EXISTED;
		}

		if (!users.containsKey(userName)) {
			return USER_NOT_EXISTED;
		}

		Task task = tasks.get(taskName);
		User user = users.get(userName);

		List<Task> assignedTask = user.getAssignedTask();
		assignedTask.add(task);
		user.setAssignedTask(assignedTask);

		if (users.get(userName).getAssignedTask().contains(task)) {
			return SUCCESS;
		} else {
			return FAILED;
		}
	}
	
}