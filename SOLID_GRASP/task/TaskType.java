package task;

public interface TaskType {

    void complete();
    String getDetails();
    String getTitle();
    String getDescription();
    String getDueDate();
    String getStatus();
    int getPriority();
    boolean isComplete();
}
