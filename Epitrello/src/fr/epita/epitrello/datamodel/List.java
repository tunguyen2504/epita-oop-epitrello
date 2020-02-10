package fr.epita.epitrello.datamodel;

/**
 * @author Anh Tu NGUYEN - Group 2 and Thanh Tung TRINH - Group 1
 *
 */
public class List {
	
	private int id;
	private String name;
	private Task tasks;
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
	public Task getTasks() {
		return tasks;
	}
	public void setTasks(Task tasks) {
		this.tasks = tasks;
	}
	
	
	
	

}
