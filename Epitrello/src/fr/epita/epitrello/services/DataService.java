package fr.epita.epitrello.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.epita.epitrello.datamodel.Task;
import fr.epita.epitrello.datamodel.TaskList;
import fr.epita.epitrello.datamodel.User;

/**
 * @author Anh Tu NGUYEN & Thanh Tung TRINH - Group 2
 *
 */
public class DataService {

	private static final String SUCCESS = "Success";
	private static final String FAILED = "Failed";
	private static final String NO_RESULT = "No result.";
	private static final String USER_EXISTED = "User already existed!";
	private static final String USER_NOT_EXISTED = "User does not exist!";
	private static final String TASK_EXISTED = "Task already existed!";
	private static final String TASK_NOT_EXISTED = "Task does not exist!";
	private static final String LIST_EXISTED = "List already existed!";
	private static final String LIST_NOT_EXISTED = "List does not exist!";
	private static final String TASK_ASSIGNED = "Task already assigned!";

	private static Map<String, Task> tasks = new LinkedHashMap<String, Task>();
	private static Map<String, TaskList> taskLists = new LinkedHashMap<String, TaskList>();
	private static Map<String, User> users = new LinkedHashMap<String, User>();

	private static UserJDBCDAO userJdbcDao = new UserJDBCDAO();

	public DataService() {

	}

	public String addUser(String name) {
		if (userJdbcDao.isUserExists(name)) {
			for (User u : userJdbcDao.getAllUser()) {
				users.putIfAbsent(u.getName(), u);
			}
			;
			return USER_EXISTED;
		}
		;

		User user = new User(name);
		if (userJdbcDao.createUser(user) != 0 && userJdbcDao.isUserExists(name)) {
			for (User u : userJdbcDao.getAllUser()) {
				users.putIfAbsent(u.getName(), u);
			}
			;
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

		Task task = new Task(name, description, estimatedTime, priority);

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

		if (!userJdbcDao.isUserExists(userName)) {
			return USER_NOT_EXISTED;
		}

		Task task = tasks.get(taskName);
		if (task.isAssigned()) {
			return TASK_ASSIGNED;
		}

		User user = users.get(userName);
		List<Task> assignedTasks = user.getAssignedTask();
		task.setAssigned(true);
		assignedTasks.add(task);
		user.setAssignedTask(assignedTasks);

		if (user.getAssignedTask().contains(task)) {
			return SUCCESS;
		} else {
			return FAILED;
		}
	}

	public String printTask(String taskName) {
		if (!tasks.containsKey(taskName)) {
			return TASK_NOT_EXISTED;
		}

		Task task = tasks.get(taskName);

		String assignee = null;

		for (User user : users.values()) {
			if (user.getAssignedTask().contains(task)) {
				assignee = user.getName();
			}
		}

		return task.getName() + "\n" + task.getDescription() + "\nPriority: " + task.getPriority()
				+ "\nEstimated Time: " + task.getEstimatedTime()
				+ (assignee != null ? "\nAssigned to " + assignee : "\nUnassigned") + "\n";
	}

	public String completeTask(String taskName) {
		if (!tasks.containsKey(taskName)) {
			return TASK_NOT_EXISTED;
		}

		if (tasks.get(taskName).isCompleted()) {
			return "Task is already completed!";
		}

		Task task = tasks.get(taskName);
		task.setCompleted(true);

		if (task.isCompleted() && tasks.get(taskName).isCompleted()) {
			return SUCCESS;
		} else {
			return FAILED;
		}
	}

	public String moveTask(String taskName, String listName) {
		if (!tasks.containsKey(taskName)) {
			return TASK_NOT_EXISTED;
		}

		if (!taskLists.containsKey(listName)) {
			return LIST_NOT_EXISTED;
		}

		Task task = tasks.get(taskName);
		TaskList oldList = new TaskList();
		TaskList newList = taskLists.get(listName);
		for (TaskList list : taskLists.values()) {
			if (list.getTasksInList().contains(task)) {
				List<Task> tasksInOldList = list.getTasksInList();
				tasksInOldList.remove(task);
				list.setTasksInList(tasksInOldList);
				oldList = list;
			}
		}

		List<Task> tasksInNewList = newList.getTasksInList();
		tasksInNewList.add(task);
		newList.setTasksInList(tasksInNewList);
		;

		if (!oldList.getTasksInList().contains(task) && newList.getTasksInList().contains(task)) {
			return SUCCESS;
		} else {
			return FAILED;
		}
	}

	public String deleteTask(String name) {
		if (!tasks.containsKey(name)) {
			return TASK_NOT_EXISTED;
		}

		Task task = tasks.get(name);
		tasks.remove(name);
		TaskList list = new TaskList();
		for (TaskList l : taskLists.values()) {
			if (l.getTasksInList().contains(task)) {
				List<Task> tasksInList = l.getTasksInList();
				tasksInList.remove(task);
				l.setTasksInList(tasksInList);
				list = l;
			}
		}

		if (!tasks.containsKey(name) && !tasks.containsValue(task) && !list.getTasksInList().contains(task)) {
			return SUCCESS;
		} else {
			return FAILED;
		}
	}

	public String deleteList(String name) {
		if (!taskLists.containsKey(name)) {
			return LIST_NOT_EXISTED;
		}

		TaskList list = taskLists.get(name);
		taskLists.remove(list);
		List<Task> tasksInList = list.getTasksInList();
		for (Task task : tasksInList) {
			if (tasks.containsValue(task)) {
				tasks.remove(task);
			}
		}

		if (!taskLists.containsKey(name) && !taskLists.containsValue(list)) {
			return SUCCESS;
		} else {
			return FAILED;
		}
	}

	public String printList(String name) {
		if (!taskLists.containsKey(name)) {
			return LIST_NOT_EXISTED;
		}

		TaskList list = taskLists.get(name);
		List<Task> tasksInList = list.getTasksInList();

		String[] assignee = new String[tasksInList.size()];

		String results = "List " + list.getName() + "\n";
		for (Task task : tasksInList) {
			for (User user : users.values()) {
				if (user.getAssignedTask().contains(task)) {
					assignee[tasksInList.indexOf(task)] = user.getName();
				}
			}
			results += task.getPriority() + " | " + task.getName() + " | "
					+ (assignee[tasksInList.indexOf(task)] != null ? assignee[tasksInList.indexOf(task)] : "Unassigned")
					+ " | " + task.getEstimatedTime() + "h\n";
		}
		return !results.equals("") ? results : NO_RESULT;
	}

	public String printAllLists() {
		String results = "";
		for (TaskList list : taskLists.values()) {
			results += printList(list.getName()) + "\n";
		}
		return !results.equals("") ? results : NO_RESULT;
	}

	public String printTotalEstimatedTime() {
		String results = "Total estimated time";
		for (User user : users.values()) {
			List<Task> assignedTasks = user.getAssignedTask();
			int totalEstimatedTime = 0;
			for (Task task : assignedTasks) {
				totalEstimatedTime += task.getEstimatedTime();
			}
			results += "\n" + user.getName() + ": " + totalEstimatedTime + "h";
		}
		return !results.equals("") ? results : NO_RESULT;
	}

	public String printTotalRemainingTime() {
		String results = "Total remaining time";
		for (User user : users.values()) {
			List<Task> assignedTasks = user.getAssignedTask();
			int totalEstimatedTime = 0;
			for (Task task : assignedTasks) {
				if (!task.isCompleted()) {
					totalEstimatedTime += task.getEstimatedTime();
				}
			}
			results += "\n" + user.getName() + ": " + totalEstimatedTime + "h";
		}
		return !results.equals("") ? results : NO_RESULT;
	}

	public String userWorkLoad(String userName) {
		String results = "";
		User user = users.get(userName);
		List<Task> assignedTasks = user.getAssignedTask();
		int totalEstimatedTime = 0;
		for (Task task : assignedTasks) {
			totalEstimatedTime += task.getEstimatedTime();
		}
		results = user.getName() + ": " + totalEstimatedTime + "h\n";
		return results;
	}

	public String printUsersByPerformance() {
		String results = "";
		List<User> userList = new ArrayList<User>();
		for (User user : users.values()) {
			List<Task> assignedTasks = user.getAssignedTask();
			int totalEstimatedTime = 0;
			for (Task task : assignedTasks) {
				if (!task.isCompleted()) {
					totalEstimatedTime += task.getEstimatedTime();
				}
			}
			user.setTotalEstimatedTime(totalEstimatedTime);
			userList.add(user);
		}
		Collections.sort(userList, (u1, u2) -> u1.getTotalEstimatedTime() - u2.getTotalEstimatedTime());
		for (User user : userList) {
			results += user.getName() + "\n";
		}
		return !results.equals("") ? results : NO_RESULT;
	}

	public String printUsersByWorkload() {
		String results = "";
		List<User> userList = new ArrayList<User>();
		for (User user : users.values()) {
			List<Task> assignedTasks = user.getAssignedTask();
			int totalEstimatedTime = 0;
			for (Task task : assignedTasks) {
				totalEstimatedTime += task.getEstimatedTime();
			}
			user.setTotalEstimatedTime(totalEstimatedTime);
			userList.add(user);
		}
		Collections.sort(userList, (u1, u2) -> u1.getTotalEstimatedTime() - u2.getTotalEstimatedTime());
		for (User user : userList) {
			results += user.getName() + "\n";
		}
		return !results.equals("") ? results : NO_RESULT;
	}

	public String printUnassignedTasksByPriority() {
		String results = "";
		List<Task> unassignedTasks = new ArrayList<Task>();
		for (Task task : tasks.values()) {
			if (!task.isAssigned()) {
				unassignedTasks.add(task);
			}
		}
		Collections.sort(unassignedTasks, (t1, t2) -> t1.getPriority() - t2.getPriority());
		for (Task task : unassignedTasks) {
			results += task.getPriority() + " | " + task.getName() + " | " + "Unassigned | " + task.getEstimatedTime()
					+ "h\n";
		}
		return !results.equals("") ? results : NO_RESULT;
	}

	public String printAllUnfinishedTasksByPriority() {
		String results = "";
		List<Task> unfinishedTasks = new ArrayList<Task>();
		for (Task task : tasks.values()) {
			if (!task.isCompleted()) {
				unfinishedTasks.add(task);
			}
		}
		Collections.sort(unfinishedTasks, (t1, t2) -> t1.getPriority() - t2.getPriority());
		String[] assignee = new String[unfinishedTasks.size()];
		for (Task task : unfinishedTasks) {
			for (User user : users.values()) {
				if (user.getAssignedTask().contains(task)) {
					assignee[unfinishedTasks.indexOf(task)] = user.getName();
				}
			}
			results += task.getPriority() + " | " + task.getName() + " | "
					+ (assignee[unfinishedTasks.indexOf(task)] != null ? assignee[unfinishedTasks.indexOf(task)]
							: "Unassigned")
					+ " | " + task.getEstimatedTime() + "h\n";
		}
		return !results.equals("") ? results : NO_RESULT;
	}

	public String printUserTasks(String userName) {
		if (!users.containsKey(userName)) {
			return USER_NOT_EXISTED;
		}

		String results = "";
		User user = users.get(userName);
		List<Task> assignedTasks = user.getAssignedTask();
		for (Task task : assignedTasks) {
			results += task.getPriority() + " | " + task.getName() + " | " + user.getName() + " | "
					+ task.getEstimatedTime() + "h\n";
		}
		return !results.equals("") ? results : NO_RESULT;
	}

	public String printUserUnfinishedTasks(String userName) {
		if (!users.containsKey(userName)) {
			return USER_NOT_EXISTED;
		}

		String results = "";
		User user = users.get(userName);
		List<Task> assignedTasks = user.getAssignedTask();
		for (Task task : assignedTasks) {
			if (!task.isCompleted()) {
				results += task.getPriority() + " | " + task.getName() + " | " + user.getName() + " | "
						+ task.getEstimatedTime() + "h\n";
			}
		}
		return !results.equals("") ? results : NO_RESULT;
	}
}
