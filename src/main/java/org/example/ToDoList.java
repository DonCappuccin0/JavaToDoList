package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ToDoList {
    private String toDoListName;
    private List<ToBeDone> toBeDoneList;
    private String creationDate;

    public ToDoList(String toDoListName) {
        this.toDoListName = toDoListName;
        this.toBeDoneList = new ArrayList<>();
        this.creationDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public ToDoList(String toDoListName, ArrayList<ToBeDone> toBeDoneList) {
        this.toBeDoneList = toBeDoneList;
        this.creationDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public String  getCreationDate() {
        return creationDate;
    }

    public List<ToBeDone> getToBeDoneList() {
        return toBeDoneList;
    }

    public void addTask(ToBeDone task) {
        if (task.getTaskName() == null || task.getTaskName().isEmpty()) {
            System.err.print("The task must have a name !");
        } else {
            if (task.getDescription() == null || task.getDescription().isEmpty()) {
                task.setDescription("No descprition provided.");
            }
            getToBeDoneList().add(task);
        }
    }

    public String getToDoListName() {
        return toDoListName;
    }
    public void showToDoList(){
        if(!getToBeDoneList().isEmpty()){
            for(int i = 0; i < getToBeDoneList().size(); i++){
                System.out.println(
                        (i+1)+"."+
                                (toBeDoneList.get(i).isDone() ? "[X] " : "[ ] ")+toBeDoneList.get(i).getTaskName() +
                                " --> "+"("+toBeDoneList.get(i).getDescription()+")" +
                                "[made on the "+toBeDoneList.get(i).getCreationDate()+"]");
            }
        }else{
            System.out.println("Your to do list is empty !");
        }
    }
}
