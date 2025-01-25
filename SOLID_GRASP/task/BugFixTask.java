package task;

public class BugFixTask extends Task {
    private String bugId;

    public BugFixTask(String title, String description, String dueDate, String status, int priority, String bugId) {
        super(title, description, dueDate, status, priority);
        this.bugId = bugId;
    }

    @Override
    public void handleTask() {
        this.complete();
    }

    public String getBugId() {
        return bugId;
    }
}