package com.example.sprintproject.model;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton class responsible for simulating database connection
 */
public class DatabaseManagerModel {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManagerModel.class.getName());
    private static final AtomicReference<DatabaseManagerModel> INSTANCE =
            new AtomicReference<>();
    private LogDecorator logDecorator;
    private DatabaseManagerModel() {
        this.logDecorator = new LogDecorator(LOGGER); }

    /**
     * Provides access to the singleton use.
     * @return the single instance of DatabaseManagerModel
     */
    public static DatabaseManagerModel getInstance() {
        DatabaseManagerModel currentInstance = INSTANCE.get();
        if (currentInstance != null) {
            return currentInstance;
        }

        synchronized (DatabaseManagerModel.class) {
            currentInstance = INSTANCE.get();
            if (currentInstance == null) {
                DatabaseManagerModel newInstance = new DatabaseManagerModel();
                if (INSTANCE.compareAndSet(null, newInstance)) {
                    return newInstance;
                } else {
                    return INSTANCE.get(); // Return the already set instance
                }
            }
            return currentInstance; // Return the instance found after sync
        }
    }

    private void logConnectionStatus(String message) {
        logDecorator.log(Level.INFO, message); }

    /**
     * Simulates connecting to the database.
     */
    public void connect() {
        logConnectionStatus("Database connected.");
    }

    /**
     * Simulates disconnecting from the database.
     */
    public void disconnect() {
        logConnectionStatus("Database disconnected.");
    }

    /**
     * Decorator for Logger that adds custom prefixes to log messages.
     */
    private static class LogDecorator {
        private final Logger logger;

        public LogDecorator(Logger logger) {
            this.logger = logger; }

        public void log(Level level, String message) {
            String decoratedMessage = "[DatabaseManager] " + message;
            logger.log(level, decoratedMessage);
        }
    }
}
