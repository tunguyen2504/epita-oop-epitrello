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
 * @author Anh Tu NGUYEN - Group 2 and Thanh Tung TRINH - Group 1
 *
 */
public class DataService {

	/**
	 * Constant for system messages
	 */
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

	private static String output; // a string to store all results of all methods

	public static String getOutput() {
		return output;
	}

	public DataService() {
		output = "";
	}

	/**
	 * @param name the name of the user that need to be created
	 * @return "User already existed!" if name exists in table USER, return
	 *         "Success" if user is created successfully, otherwise return "Failed"
	 */
	public String addUser(String name) {
		if (userJdbcDao.isUserExists(name)) {
			for (User u : userJdbcDao.getAllUser()) {
				users.putIfAbsent(u.getName(), u);
			}
			output += USER_EXISTED + "\n";
			return USER_EXISTED;
		}
		;

		User user = new User(name);
		if (userJdbcDao.createUser(user) != 0 && userJdbcDao.isUserExists(name)) {
			// To store all user in the table USER to hashmap
			for (User u : userJdbcDao.getAllUser()) {
				users.putIfAbsent(u.getName(), u);
			}
			output += SUCCESS + "\n";
			return SUCCESS;
		} else {
			output += FAILED + "\n";
			return FAILED;
		}
	}

	/**
	 * @param name the name of the list that need to be created
	 * @return "List already existed!" if name of list exists, return "Success" if
	 *         list is created successfully, otherwise return "Failed"
	 */
	public String addList(String name) {
		if (taskLists.containsKey(name)) {
			output += LIST_EXISTED + "\n";
			return LIST_EXISTED;
		}

		TaskList list = new TaskList(name);
		taskLists.put(name, list);

		if (taskLists.containsKey(name)) {
			output += SUCCESS + "\n";
			return SUCCESS;
		} else {
			output += FAILED + "\n";
			return FAILED;
		}
	}

	/**
	 * @param listName      the name of the list in which task is added
	 * @param name          the name of the task that need to be added into list
	 * @param estimatedTime the necessary time to complete the task
	 * @param priority      the importance level of the task
	 * @param description   the information that describe the task
	 * @return "Task already existed!" if name of task exists, return "List does not
	 *         exist!" if listName does not exist, return "Success" if task is
	 *         created and added into list successfully, otherwise return false
	 */
	public String addTask(String listName, String name, int estimatedTime, int priority, String description) {
		if (tasks.containsKey(name)) {
			output += TASK_EXISTED + "\n";
			return TASK_EXISTED;
		}

		if (!taskLists.containsKey(listName)) {
			output += LIST_NOT_EXISTED + "\n";
			return LIST_NOT_EXISTED;
		}

		Task task = new Task(name, description, estimatedTime, priority);

		tasks.put(name, task);
		TaskList list = taskLists.get(listName);
		List<Task> tasksInList = list.getTasksInList();
		tasksInList.add(task);
		list.setTasksInList(tasksInList);

		if (tasks.containsKey(name) && taskLists.get(listName).getTasksInList().contains(task)) {
			output += SUCCESS + "\n";
			return SUCCESS;
		} else {
			output += FAILED + "\n";
			return FAILED;
		}
	}

	/**
	 * @param name          the name of the task that need to be edited
	 * @param estimatedTime the necessary time to complete the task
	 * @param priority      the importance level of the task
	 * @param description   the information that describe the task
	 * @return "Task does not exist!" if name of the task does not exist, return
	 *         "Success" if task is edited successfully, otherwise return "Failed"
	 */
	public String editTask(String name, int estimatedTime, int priority, String description) {
		if (!tasks.containsKey(name)) {
			output += TASK_NOT_EXISTED + "\n";
			return TASK_NOT_EXISTED;
		}

		Task task = tasks.get(name);

		task.setEstimatedTime(estimatedTime);
		task.setPriority(priority);
		task.setDescription(description);

		if (tasks.containsKey(name) && tasks.get(name) == task) {
			output += SUCCESS + "\n";
			return SUCCESS;
		} else {
			output += FAILED + "\n";
			return FAILED;
		}
	}

	/**
	 * @param taskName the name of the task that need to be assigned
	 * @param userName the name of user who the task is assigned to
	 * @return "Task does not exist!" if name of the task does not exist, return
	 *         "User does not exist!" if name of the user does not exist, return
	 *         "Task already assigned!" if the task is assigned already, return
	 *         "Success" if the task is assigned to user successfully, otherwise
	 *         return "Failed"
	 */
	public String assignTask(String taskName, String userName) {
		if (!tasks.containsKey(taskName)) {
			output += TASK_NOT_EXISTED + "\n";
			return TASK_NOT_EXISTED;
		}

		if (!userJdbcDao.isUserExists(userName)) {
			output += USER_NOT_EXISTED + "\n";
			return USER_NOT_EXISTED;
		}

		Task task = tasks.get(taskName);
		if (task.isAssigned()) {
			output += TASK_ASSIGNED + "\n";
			return TASK_ASSIGNED;
		}

		User user = users.get(userName);
		List<Task> assignedTasks = user.getAssignedTask();
		task.setAssigned(true);
		assignedTasks.add(task);
		user.setAssignedTask(assignedTasks);

		if (user.getAssignedTask().contains(task)) {
			output += SUCCESS + "\n";
			return SUCCESS;
		} else {
			output += FAILED + "\n";
			return FAILED;
		}
	}

	/**
	 * @param taskName the name of the task that need to be printed
	 * @return "Task does not exist!" if name of the task does not exist, else
	 *         return a result containing task details
	 */
	public String printTask(String taskName) {
		if (!tasks.containsKey(taskName)) {
			output += TASK_NOT_EXISTED + "\n";
			return TASK_NOT_EXISTED;
		}

		Task task = tasks.get(taskName);

		String assignee = null;

		// To find user who is the assignee of the task
		for (User user : users.values()) {
			if (user.getAssignedTask().contains(task)) {
				assignee = user.getName();
			}
		}

		String results = task.getName() + "\n" + task.getDescription() + "\nPriority: " + task.getPriority()
				+ "\nEstimated Time: " + task.getEstimatedTime()
				+ (assignee != null ? "\nAssigned to " + assignee : "\nUnassigned") + "\n";

		output += results;
		return results;
	}

	/**
	 * @param taskName the name of the task that need to be completed
	 * @return "Task does not exist!" if name of the task does not exist, return
	 *         "Success" if task is marked completed, otherwise return "Failed"
	 */
	public String completeTask(String taskName) {
		if (!tasks.containsKey(taskName)) {
			output += TASK_NOT_EXISTED + "\n";
			return TASK_NOT_EXISTED;
		}

		if (tasks.get(taskName).isCompleted()) {
			output += "Task is already completed!" + "\n";
			return "Task is already completed!";
		}

		Task task = tasks.get(taskName);
		task.setCompleted(true);

		if (task.isCompleted() && tasks.get(taskName).isCompleted()) {
			output += SUCCESS + "\n";
			return SUCCESS;
		} else {
			output += FAILED + "\n";
			return FAILED;
		}
	}

	/**
	 * @param taskName the name of the task that need to be moved
	 * @param listName the name of the destination list
	 * @return "Task does not exist!" if name of the task does not exist, return
	 *         "List does not exist!" if listName does not exist, return "Success"
	 *         if the task is moved successfully, otherwise return "Failed"
	 */
	public String moveTask(String taskName, String listName) {
		if (!tasks.containsKey(taskName)) {
			output += TASK_NOT_EXISTED + "\n";
			return TASK_NOT_EXISTED;
		}

		if (!taskLists.containsKey(listName)) {
			output += LIST_NOT_EXISTED + "\n";
			return LIST_NOT_EXISTED;
		}

		Task task = tasks.get(taskName);
		TaskList oldList = new TaskList();
		TaskList newList = taskLists.get(listName);

		// To find which list is having the task currently
		for (TaskList list : taskLists.values()) {
			if (list.getTasksInList().contains(task)) {
				List<Task> tasksInOldList = list.getTasksInList();
				tasksInOldList.remove(task);
				list.setTasksInList(tasksInOldList);
				oldList = list;
			}
		}

		// To get all the tasks in the destination list then add this task
		List<Task> tasksInNewList = newList.getTasksInList();
		tasksInNewList.add(task);
		newList.setTasksInList(tasksInNewList);
		;

		if (!oldList.getTasksInList().contains(task) && newList.getTasksInList().contains(task)) {
			output += SUCCESS + "\n";
			return SUCCESS;
		} else {
			output += FAILED + "\n";
			return FAILED;
		}
	}

	/**
	 * @param name the name of the task that need to be deleted
	 * @return "Task does not exist!" if name of the task does not exist, return
	 *         "Success" if the task is moved successfully, otherwise return
	 *         "Failed"
	 */
	public String deleteTask(String name) {
		if (!tasks.containsKey(name)) {
			output += TASK_NOT_EXISTED + "\n";
			return TASK_NOT_EXISTED;
		}

		Task task = tasks.get(name);
		tasks.remove(name);

		// To find the list containing this task then delete the task from the list
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
			output += SUCCESS + "\n";
			return SUCCESS;
		} else {
			output += FAILED + "\n";
			return FAILED;
		}
	}

	/**
	 * @param name the name of the list that need to be deleted
	 * @return "List does not exist!" if name of list does not exist, return
	 *         "Success" if list and all tasks in the list are deleted successfully,
	 *         otherwise return "Failed"
	 */
	public String deleteList(String name) {
		if (!taskLists.containsKey(name)) {
			output += LIST_NOT_EXISTED + "\n";
			return LIST_NOT_EXISTED;
		}

		TaskList list = taskLists.get(name);
		taskLists.remove(list);

		// To find all tasks in the list then delete them
		List<Task> tasksInList = list.getTasksInList();
		for (Task task : tasksInList) {
			if (tasks.containsValue(task)) {
				tasks.remove(task);
			}
		}

		if (!taskLists.containsKey(name) && !taskLists.containsValue(list)) {
			output += SUCCESS + "\n";
			return SUCCESS;
		} else {
			output += FAILED + "\n";
			return FAILED;
		}
	}

	/**
	 * @param name the name of the list that need to be printed
	 * @return "List does not exist!" if name of list does not exist, else return a
	 *         result containing name of list and details of all tasks in the list
	 */
	public String printList(String name) {
		if (!taskLists.containsKey(name)) {
			output += LIST_NOT_EXISTED + "\n";
			return LIST_NOT_EXISTED;
		}

		TaskList list = taskLists.get(name);
		List<Task> tasksInList = list.getTasksInList();

		String[] assignee = new String[tasksInList.size()];

		String results = "List " + list.getName() + "\n";
		for (Task task : tasksInList) {
			// To find assignee of each task
			for (User user : users.values()) {
				if (user.getAssignedTask().contains(task)) {
					assignee[tasksInList.indexOf(task)] = user.getName();
				}
			}
			results += task.getPriority() + " | " + task.getName() + " | "
					+ (assignee[tasksInList.indexOf(task)] != null ? assignee[tasksInList.indexOf(task)] : "Unassigned")
					+ " | " + task.getEstimatedTime() + "h\n";
		}
		output += !results.equals("") ? results : (NO_RESULT + "\n");
		return !results.equals("") ? results : NO_RESULT;
	}

	/**
	 * @return a result containing all lists and details of all tasks in those lists
	 */
	public String printAllLists() {
		String results = "";
		for (TaskList list : taskLists.values()) {
			results += printList(list.getName()) + "\n";
		}
		return !results.equals("") ? results : NO_RESULT;
	}

	/**
	 * @return a result containing all users and their total estimated time for all
	 *         tasks
	 */
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
		output += !results.equals("") ? results : (NO_RESULT + "\n");
		return !results.equals("") ? results : NO_RESULT;
	}

	/**
	 * @return a result containing all users and their total remaining time for all
	 *         incomplete tasks
	 */
	public String printTotalRemainingTime() {
		String results = "Total remaining time";
		for (User user : users.values()) {
			List<Task> assignedTasks = user.getAssignedTask();
			int totalEstimatedTime = 0;
			// To calculate the total estimated time for all incomplete tasks
			for (Task task : assignedTasks) {
				if (!task.isCompleted()) {
					totalEstimatedTime += task.getEstimatedTime();
				}
			}
			results += "\n" + user.getName() + ": " + totalEstimatedTime + "h\n";
		}
		output += !results.equals("") ? results : (NO_RESULT + "\n");
		return !results.equals("") ? results : NO_RESULT;
	}

	/**
	 * @param userName the name of user that need to be printed with work load
	 * @return a result containing userName and the total estimated time for all
	 *         tasks
	 */
	public String userWorkLoad(String userName) {
		String results = "";
		User user = users.get(userName);
		List<Task> assignedTasks = user.getAssignedTask();
		int totalEstimatedTime = 0;
		// To calculate the total estimated time for all tasks
		for (Task task : assignedTasks) {
			totalEstimatedTime += task.getEstimatedTime();
		}
		results = user.getName() + ": " + totalEstimatedTime + "h\n";
		output += results + "\n";
		return results;
	}

	/**
	 * @return a result containing all userNames in descending order of total
	 *         estimated time
	 */
	public String printUsersByPerformance() {
		String results = "";
		List<User> userList = new ArrayList<User>();
		for (User user : users.values()) {
			List<Task> assignedTasks = user.getAssignedTask();
			int totalEstimatedTime = 0;
			// To calculate the total estimated time for all incomplete tasks
			for (Task task : assignedTasks) {
				if (!task.isCompleted()) {
					totalEstimatedTime += task.getEstimatedTime();
				}
			}
			user.setTotalEstimatedTime(totalEstimatedTime);
			userList.add(user);
		}
		// To sort the user list by descending order of total estimated time
		Collections.sort(userList, (u1, u2) -> u1.getTotalEstimatedTime() - u2.getTotalEstimatedTime());

		for (User user : userList) {
			results += user.getName() + "\n";
		}
		output += (!results.equals("") ? results : NO_RESULT) + "\n";
		return !results.equals("") ? results : NO_RESULT;
	}

	/**
	 * @return a result containing all userNames in ascending order of total
	 *         estimated time for all tasks
	 */
	public String printUsersByWorkload() {
		String results = "";
		List<User> userList = new ArrayList<User>();
		for (User user : users.values()) {
			List<Task> assignedTasks = user.getAssignedTask();
			int totalEstimatedTime = 0;
			// To calculate the total estimated time for all tasks
			for (Task task : assignedTasks) {
				totalEstimatedTime += task.getEstimatedTime();
			}
			user.setTotalEstimatedTime(totalEstimatedTime);
			userList.add(user);
		}
		// To sort the user list by ascending order of total estimated time
		Collections.sort(userList, (u1, u2) -> u1.getTotalEstimatedTime() - u2.getTotalEstimatedTime());

		for (User user : userList) {
			results += user.getName() + "\n";
		}
		output += (!results.equals("") ? results : NO_RESULT) + "\n";
		return !results.equals("") ? results : NO_RESULT;
	}

	/**
	 * @return a result containing all unassigned tasks and their details in order
	 *         of priority level
	 */
	public String printUnassignedTasksByPriority() {
		String results = "";
		List<Task> unassignedTasks = new ArrayList<Task>();
		// To find which task is unassigned
		for (Task task : tasks.values()) {
			if (!task.isAssigned()) {
				unassignedTasks.add(task);
			}
		}
		// To sort the task list by priority level
		Collections.sort(unassignedTasks, (t1, t2) -> t1.getPriority() - t2.getPriority());
		for (Task task : unassignedTasks) {
			results += task.getPriority() + " | " + task.getName() + " | " + "Unassigned | " + task.getEstimatedTime()
					+ "h\n";
		}
		output += !results.equals("") ? results : (NO_RESULT + "\n");
		return !results.equals("") ? results : NO_RESULT;
	}

	/**
	 * @return a result containing all incomplete tasks and their details in order
	 *         of priority level
	 */
	public String printAllUnfinishedTasksByPriority() {
		String results = "";
		List<Task> unfinishedTasks = new ArrayList<Task>();
		// To find which task is incomplete
		for (Task task : tasks.values()) {
			if (!task.isCompleted()) {
				unfinishedTasks.add(task);
			}
		}
		// To sort the task list by priority level
		Collections.sort(unfinishedTasks, (t1, t2) -> t1.getPriority() - t2.getPriority());
		String[] assignee = new String[unfinishedTasks.size()];

		// To find the assignee of each task
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
		output += !results.equals("") ? results : (NO_RESULT + "\n");
		return !results.equals("") ? results : NO_RESULT;
	}

	/**
	 * @param userName the name of user that need to be printed with task details
	 * @return "User does not exist!" if userName does not exist in table USER, else
	 *         return all tasks assigned to that user and details
	 */
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
		output += !results.equals("") ? results : (NO_RESULT + "\n");
		return !results.equals("") ? results : NO_RESULT;
	}

	/**
	 * @param userName the name of user that need to be printed with task details
	 * @return "User does not exist!" if userName does not exist in table USER, else
	 *         return all incomplete tasks assigned to that user and details
	 */
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
		output += !results.equals("") ? results : (NO_RESULT + "\n");
		return !results.equals("") ? results : NO_RESULT;
	}
}
