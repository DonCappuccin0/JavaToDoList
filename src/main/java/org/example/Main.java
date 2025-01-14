package org.example;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Scanner;

public class Main {
    final static Path TO_DO_LISTS_PATH = Paths.get("ToDoLists");
    static Scanner sc = new Scanner(System.in);
    static Gson gson = new Gson();
    static BufferedReader bufferedReader;
    static BufferedWriter bufferedWriter;

    public static void main(String[] args) throws IOException {
        String select;
        do {
            boolean isDirectoryEmpty = isDirectroyEmpty(TO_DO_LISTS_PATH);
            if (isDirectoryEmpty) {
                System.out.println("You currently have no to-do lists.");
            }
            toDoListMenu(isDirectoryEmpty);
            select = sc.nextLine();
            switch (select) {
                case "create":
                    createToDoList();
                    break;
                case "show":
                    showAllToDoLists(fetchForToDoLists());
                    break;
                case "update":
                    updateToDoList(fetchForToDoLists());
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Choose a valid selection !");
                    break;
            }
        } while (!select.equalsIgnoreCase("exit"));
    }

    public static void showAllToDoLists(Hashtable<String, ToDoList> toDoLists) throws IOException {
        for (ToDoList toDoList : toDoLists.values()) {
            System.out.println(toDoList.getToDoListName());
        }
            /*System.out.println("Do you want to update any of these ? (yes/no)");
            String select = "";
        do{
            select = sc.nextLine();
            if(select.equalsIgnoreCase("no")){
                return;
            } else if (select.equalsIgnoreCase("yes")) {
                updateToDoList(toDoLists);

            } else if(!select.equalsIgnoreCase("yes") && !select.equalsIgnoreCase("no")){
                System.out.println("Choose either yes or no !");
                select = sc.nextLine();
            }
        }while(!select.equalsIgnoreCase("yes") && !select.equalsIgnoreCase("no"))*/

    }

    private static void updateToDoList(Hashtable<String, ToDoList> toDoLists) throws IOException {
        System.out.println("Type the name of your to-do list (or \"none\" to cancel)");
        String toDoListName;
        do {
            toDoListName = sc.nextLine();
            if (!toDoListName.equalsIgnoreCase("none")) {
                if (toDoLists.get(toDoListName) != null) {
                    toDoLists.get(toDoListName).showToDoList();
                    taskManagerMenu(toDoLists.get(toDoListName));
                    break;
                } else {
                    System.out.println("You're trying to access a list that doesn't exist !");
                }
            }
        } while (!toDoListName.equals("none") || toDoLists.get(toDoListName) == null);
    }

    public static Hashtable<String, ToDoList> fetchForToDoLists() {
        Hashtable<String, ToDoList> toDoLists = new Hashtable<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(TO_DO_LISTS_PATH)) {
            for (Path filePath : stream) {
                bufferedReader = new BufferedReader(new FileReader(filePath.toFile()));
                toDoLists.put(filePath.getFileName().toString().replace(".json", ""), gson.fromJson(bufferedReader, ToDoList.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toDoLists;
    }

    public static void createToDoList() throws IOException {
        String toDoListName = "";
        do {
            System.out.println("Enter the name of your to-do list");
            toDoListName = sc.nextLine();
            ToDoList toDoList = new ToDoList(toDoListName);
            writeIntoToDoListFile(toDoList, toDoListName);
        } while (toDoListName.isEmpty());
        System.out.println("To-do list created successfully !\n");
    }

    public static void writeIntoToDoListFile(ToDoList toDoList, String toDoListName) throws IOException {
        bufferedWriter = new BufferedWriter(new FileWriter(new File(TO_DO_LISTS_PATH.toString() + "/" + toDoListName + ".json")));
        gson.toJson(toDoList, bufferedWriter);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public static boolean isDirectroyEmpty(Path path) {
        if (Files.exists(path)) {
            try {
                if (Files.list(path).findAny().isEmpty()) {
                    return true;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static void toDoListMenu(boolean isEmpty) {
        System.out.println("create :  Create a new to-do list");
        if (!isEmpty) {
            System.out.print("""
                    show :  Display all existing to-do lists
                    delete :  Delete an exisiting to do lists
                    update :  Update an existing to do list
                    """);
        }
        System.out.println("exit :  Exit");
    }

    public static void taskManagerMenu(ToDoList toDoList) throws IOException {
        String select = "";
        do {
            System.out.println("""
                    add :  Add a task
                    delete : Delete a task
                    check : Becomes checked if unchecked and vice-versa
                    descr : Change description
                    exit : Exit
                    """);
            select = sc.nextLine().toLowerCase();
            switch (select) {
                case "add":
                    addTask(toDoList);
                    //toDolist.function(
                    break;
                case "delete":
                    deleteTask(toDoList);
                    break;
                case "check":
                    checkTask(toDoList);
                    break;
                case "descr":
                    updateDescr(toDoList);
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Choose a valid selection !");
                    break;
            }
        } while (!select.equalsIgnoreCase("exit"));
    }

    public static void updateDescr(ToDoList toDoList) throws IOException {
        toDoList.showToDoList();
        String descr = "";
        if (!toDoList.getToBeDoneList().isEmpty()) {
            int taskNumber = 0;
            do {
                System.out.println("Type the number of the list you want to change the description of : ");
                if (taskNumber > toDoList.getToBeDoneList().size() + 1) {
                    System.out.println("Unavailable task number !");
                }
                System.out.println("Type your new description :");
                descr = sc.nextLine();
                toDoList.getToBeDoneList().get(taskNumber - 1).setDescription(descr);
            } while (taskNumber > toDoList.getToBeDoneList().size() + 1);
            writeIntoToDoListFile(toDoList, toDoList.getToDoListName());
        }
    }

    public static void checkTask(ToDoList toDoList) throws IOException {
        toDoList.showToDoList();
        if (!toDoList.getToBeDoneList().isEmpty()) {
            int taskNumber = 0;
            do {
                System.out.println("Type the number of the list you want to check : ");
                taskNumber = sc.nextInt();
                sc.nextLine();
                if (taskNumber-1 >= toDoList.getToBeDoneList().size()) {
                    System.out.println("Unavailable task number !");
                }
                toDoList.getToBeDoneList().get(taskNumber - 1).doneToggle();
            } while (taskNumber-1 >= toDoList.getToBeDoneList().size() );
            writeIntoToDoListFile(toDoList, toDoList.getToDoListName());
        }
    }

    public static void deleteTask(ToDoList toDoList) throws IOException {
        toDoList.showToDoList();
        if (!toDoList.getToBeDoneList().isEmpty()) {
            int taskNumber = 0;
            do {
                System.out.println("Type the number of the list you want to delete of : ");
                taskNumber = sc.nextInt();
                sc.nextLine();
                if (taskNumber-1 >= toDoList.getToBeDoneList().size()) {
                    System.out.println("Unavailable task number !");
                }
            } while (taskNumber-1 >= toDoList.getToBeDoneList().size());
            toDoList.getToBeDoneList().remove(taskNumber - 1);
            writeIntoToDoListFile(toDoList, toDoList.getToDoListName());
        }
    }

    public static void addTask(ToDoList toDoList) throws IOException {
        String taskName = "";
        do {
            System.out.println("Type the name of your task :");
            taskName = sc.nextLine();
            if (taskName.isEmpty()) {
                System.out.println("Task name should not be empty !");
            }
        } while (taskName.isEmpty());
        System.out.println("Add a description (optional)");
        String descr = sc.nextLine();
        toDoList.getToBeDoneList().add(new ToBeDone(taskName, descr));
        writeIntoToDoListFile(toDoList, toDoList.getToDoListName());
    }
}