/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bl;

import beans.Student;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 *
 * @author Jan
 */
public class StudentBL {

    private List<Student> allStudents;
    private TreeSet<String> allAttendingClasses;

    public StudentBL() {
        allStudents = new ArrayList();
        allAttendingClasses = new TreeSet();
    }

    public void loadData(File file) throws FileNotFoundException, IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        bufferedReader.readLine();
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            String[] tokens = line.split(",");
            allAttendingClasses.add(tokens[0]);
            allStudents.add(new Student(tokens));
        }
    }

    public List<Student> getStudentsFromClass(String attendingClass) {
        if (attendingClass.isEmpty()) {
            return allStudents;
        } else {
            List<Student> filteredClassStudents = new ArrayList();
            for (Student student : allStudents) {
                if (student.getAttendingClass().equalsIgnoreCase(attendingClass)) {
                    filteredClassStudents.add(student);
                }
            }
            return filteredClassStudents;
        }
    }

    public void removeStudent(String[] removeCredentials) {
        for (Student student : allStudents) {
            if (student.getLastname().equalsIgnoreCase(removeCredentials[0])
                    && student.getAttendingClass().equalsIgnoreCase(removeCredentials[1])) {
                allStudents.remove(student);
                break;
            }
        }
    }

    public TreeSet<String> getAllAttendingClasses() {
        return allAttendingClasses;
    }

}
