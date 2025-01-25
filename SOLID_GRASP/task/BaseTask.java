package task;

public class BaseTask extends Task {
    public BaseTask(String title, String description, String dueDate, String status, int priority) {
        super(title, description, dueDate, status, priority);
    }

    @Override
    public void handleTask() {

        this.complete();

    }
}
