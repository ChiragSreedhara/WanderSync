package task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class RecurringTask extends Task {
    private int recurrenceInterval;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public RecurringTask(String title, String description, String dueDate, int recurrenceInterval) {
        super(title, description, dueDate, "Pending", 2);
        this.recurrenceInterval = recurrenceInterval;
    }

    @Override
    public void handleTask() {

        updateDueDate();


    }

    private void updateDueDate() {
        // Parse the existing due date
        LocalDate currentDueDate = LocalDate.parse(dueDate, dateFormatter);

        // Add the recurrence interval (in days) to the due date
        LocalDate newDueDate = currentDueDate.plusDays(recurrenceInterval);

        // Update the dueDate field with the new date
        dueDate = newDueDate.format(dateFormatter);
    }

}
