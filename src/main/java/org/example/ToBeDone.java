package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ToBeDone {
    private String taskName;
    private boolean isDone;
    private String description;
    private String creationDate;


    public ToBeDone(String taskName, String description) {
        this.taskName = taskName;
        this.isDone = false;
        this.description = description;
        this.creationDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public ToBeDone(String taskName) {
        this.taskName = taskName;
        this.isDone = false;
        this.description = null;
        this.creationDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public boolean isDone() {
        return isDone;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDescription() {
        if (description == null || description.isEmpty()) {
            return "No description has beenn given";
        }
        return description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void doneToggle() {
        if (isDone) {
            isDone = false;
        }else{
            isDone = true;
        }
    }
}
