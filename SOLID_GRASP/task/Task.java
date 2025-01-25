package task;

public abstract class Task implements TaskType{

    protected String title;
    protected String description;
    protected String dueDate;
    protected String status;
    protected int priority;
    protected boolean complete;

    public protected Task(String title, String description, String dueDate, String status, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.priority = priority;
        this.complete = false;
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public String getDescription() { return description; }

    @Override
    public String getDueDate() { return dueDate; }

    @Override
    public String getStatus() { return status; }

    @Override
    public int getPriority() { return priority; }

    @Override
    public String getDetails() {
        return String.format("Title: %s Description: %s Due: %s Status: %s Priority: %s",
                title, description, dueDate, status, priority););
    }

    public void setStatus(String status) { this.status = status; }

    public void complete() {this.complete = true; }

    public boolean isComplete() {return complete; }

    public abstract void handleTask();
}
