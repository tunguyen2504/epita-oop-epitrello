package fr.epita.epitrello.datamodel;

import java.sql.Time;

public class Task {
	
	private int id;
	private String name;
	private String description;
	private Time startTime;
	private Time endTime;
	private Time estimatedTime;
	private int priority;
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
	public Time getStartTime() {
		return startTime;
	}
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	public Time getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(Time estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
	

}
