package fr.epita.epitrello.datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anh Tu NGUYEN & Thanh Tung TRINH - Group 2
 *
 */
public class User {

	private int id;
	private String name;
	private int totalEstimatedTime;
	private List<Task> assignedTask = new ArrayList<Task>();
	
	public User() {
		
	}
	
	public User(String name) {
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

	public int getTotalEstimatedTime() {
		return totalEstimatedTime;
	}

	public void setTotalEstimatedTime(int totalEstimatedTime) {
		this.totalEstimatedTime = totalEstimatedTime;
	}

	public List<Task> getAssignedTask() {
		return assignedTask;
	}

	public void setAssignedTask(List<Task> assignedTask) {
		this.assignedTask = assignedTask;
	}

}
