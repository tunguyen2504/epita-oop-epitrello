package fr.epita.epitrello.datamodel;

public class User {
	
	private int id;
	private String name;
	private String department;
	private Task assignedTask;
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Task getAssignedTask() {
		return assignedTask;
	}
	public void setAssignedTask(Task assignedTask) {
		this.assignedTask = assignedTask;
	}
	
		

}
