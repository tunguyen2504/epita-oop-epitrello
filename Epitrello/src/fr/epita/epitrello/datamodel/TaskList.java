package fr.epita.epitrello.datamodel;

import java.util.ArrayList;
import java.util.List;

public class TaskList {

	private int id;
	private String name;
	private List<Task> tasksInList = new ArrayList<Task>();

	public TaskList() {

	}

	public TaskList(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Task> getTasksInList() {
		return tasksInList;
	}

	public void setTasksInList(List<Task> tasksInList) {
		this.tasksInList = tasksInList;
	}

}
