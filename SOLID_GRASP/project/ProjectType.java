package project;

import java.util.List;

public interface ProjectType {
    String getName();
    String getDescription();
    String getStartDate();
    String getEndDate();
    void setName(String name);
    void setDescription(String description);
    void setStartDate(String startDate);
    void setEndDate(String endDate);

    List<TaskType> getTasks();
    TaskType getTaskByTitle(String taskTitle);
    void addTask(TaskType task);
    void removeTask(TaskType task);
    void addTeamMember(String name);
    void removeTeamMember(String name);
}
