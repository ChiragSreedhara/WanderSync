package project;

import java.util.ArrayList;
import java.util.List;

public class Project implements ProjectType{
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private List<TaskType> tasks;
    private List<String> members;

    public Project(String name, String description, String startDate, String endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tasks = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Override
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public List<TaskType> getTasks() {
        return tasks;
    }

    @Override
    public TaskType getTaskByTitle(String taskTitle) {
        for (TaskType t: tasks) {
            if (t.getTitle().equals(taskTitle)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public void addTask(TaskType task) {
        tasks.add(task);
    }

    @Override
    public void removeTask(TaskType task) {
        tasks.remove(task);
    }

    @Override
    public void addTeamMember(String teamMemberName) {
        if (!members.contains(teamMemberName)) {
            members.add(teamMemberName);
            MemberManager.getInstance().addTeamMember(this, teamMemberName);
        }
    }

    @Override
    public void removeTeamMember(String teamMemberName) {
        if (members.contains(teamMemberName)) {
            members.remove(teamMemberName);
            MemberManager.getInstance().removeTeamMember(this, teamMemberName);
        }
    }
}
