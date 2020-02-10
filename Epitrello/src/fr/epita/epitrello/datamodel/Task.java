package fr.epita.epitrello.datamodel;

/**
 * @author Anh Tu NGUYEN & Thanh Tung TRINH - Group 2
 *
 */
public class Task {

	private int id;
	private String name;
	private String description;
	private int estimatedTime;
	private int priority;
	private boolean isCompleted = false;
	private boolean isAssigned = false;

	public Task() {
		
	}
	
	public Task(String name, String description, int estimatedTime, int priority) {
		this.name = name;
		this.description = description;
		this.estimatedTime = estimatedTime;
		this.priority = priority;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(int estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public boolean isAssigned() {
		return isAssigned;
	}

	public void setAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

}
