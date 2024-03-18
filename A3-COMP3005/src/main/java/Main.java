import java.sql.*;
import java.util.Scanner;
import java.util.regex.*;

public class Main {

    static Statement statement;
    static Connection connection;
    static Scanner menu = new Scanner(System.in);
    public static void main(String[] args) {
        /*
        Please do the following:

        String url = "jdbc:postgresql://localhost:PORT_NUMBER/DATABASE_NAME";
        String user = "YOUR USERNAME";
        String password = "YOUR PASSWORD";
         */

        String url = "jdbc:postgresql://localhost:5433/COMP3005-A3";
        String user = "postgres";
        String password = "student";

        try {
            //Using the values from above, we can now connect to the database by doing the following:
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();

            boolean flag = true;
            //keep running program until user exits
            while (flag) {

                System.out.println("\nPlease select a number to perform action");
                System.out.println ("1. Return table of students" + "\n2. Add student to database" + "\n3. Update student email" + "\n4. Delete student from database" + "\n5. Exit");

                //ensure user input is valid
                if (!menu.hasNextInt()) {
                    System.out.println("Invalid input. Please enter an integer\n");
                } else {
                   int user_choice = menu.nextInt();

                    switch (user_choice) {
                        case 1: //user selects option 1 - get all students
                            getAllStudents();
                            break;

                        case 2:
                            menu.nextLine();

                            //get first name
                            String first_name = getFirstName();
                            //get last name
                            String last_name = getLastName();
                            //get user email
                            String email = getEmail();
                            //get enrollment date
                            String enrollment_date = getDate();

                            //call function with arguments
                            addStudent(first_name, last_name, email, enrollment_date);
                            break;

                        case 3:
                            //get student ID
                            String student_id = getID();
                            //get new email
                            String new_email = getEmail();

                            //call function with arguments
                            updateStudentEmail(student_id, new_email);
                            break;

                        case 4:
                            //get student ID
                            String stu_id = getID();

                            //call function with argument
                            deleteStudent(stu_id);
                            break;

                        case 5:
                            //exit program
                            System.out.println("\nExiting program...");
                            flag = false;
                            break;

                        //default if user input is not valid
                        default: System.out.println("Invalid selection. Please select options from 1-5\n");
                    }
                }

            }

        } catch (Exception e) {
            //ERROR MESSAGE
            System.out.println("\nPROGRAM ERROR: Exiting program...");
        }
    }

    // Function to return all the students within the DB
    private static void getAllStudents(){

        try{
            // creating connection, SQL query, and executing that query
            Statement stmt = connection.createStatement();
            String SQL = "SELECT * FROM students ORDER BY student_id";
            ResultSet rs = stmt.executeQuery(SQL);

            //print and format coloumn names
            System.out.printf("%-20s %-20s %-20s %-30s %-15s", "student_id", "first_name", "last_name", "email", "enrollment_date");

            //retrieving data from DB
            while(rs.next()) {
                String student_id = rs.getString("student_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                String enrollment_date = rs.getString("enrollment_date");

                //print and format data
                System.out.printf("\n%-20s %-20s %-20s %-30s %-15s", student_id, first_name, last_name, email, enrollment_date);
            }
            //close connection
            rs.close();
            stmt.close();
            System.out.println("\n All students returned successfully\n");
        } catch (SQLException e) {
            System.out.println("Error: Failed to find student table in the database");
        }

    }

    //Function to add student to DB
    private static void addStudent(String first_name, String last_name, String email, String enrollment_date) {
        try {
            //SQL query
            String insertSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";

            //retrieve arguments to use within SQL query
            PreparedStatement pstmt = connection.prepareStatement(insertSQL);
            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, email);

            pstmt.setDate(4, Date.valueOf(enrollment_date));

            //run the query
            pstmt.executeUpdate();
            //close connection
            pstmt.close();
            //show outcome of function
            getAllStudents();
            System.out.println("\nStudent: " + first_name + " " + last_name + " was added successfully to the database\n");
        } catch (SQLException e){
            System.out.println("Error: Failed to add student");
        }

    }


    //Function to update student email
    private static void updateStudentEmail (String student_id, String new_email) {

        try {
            //SQL query
            String SQL = "UPDATE students SET email = ? WHERE student_id = ?";

            //retrieve arguments to use within SQL query
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, new_email);
            pstmt.setInt(2, Integer.parseInt(student_id));

            //run the query
            pstmt.executeUpdate();
            //close connection
            pstmt.close();
            //show outcome of function
            getAllStudents();
            System.out.println("\nUpdated student " + student_id + " email successfully\n");
        } catch (SQLException e){
            System.out.println("Error: Failed to update student email");
        }

    }

    //Function to delete student
    private static void deleteStudent(String student_id)  {
        try {
            //SQL query
            String SQL = "DELETE FROM students WHERE student_id = ?";

            //retrieve arguments to use within SQL query
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, Integer.parseInt(student_id));

            //run the query
            pstmt.executeUpdate();
            //close connection
            pstmt.close();
            //show outcome of function
            getAllStudents();
            System.out.println("\nDeleted student " + student_id + " successfully\n");
        } catch (SQLException e){
            System.out.println("Error: Failed to delete student");
        }

    }

    //Helper function to check if name is valid input
    private static boolean checkName(String name) {
        //regex for alphabets and space characters
        String name_regex = "^[a-zA-Z ]+$";
        Pattern name_ptrn = Pattern.compile(name_regex);
        //check if user input matches regex
        return name_ptrn.matcher(name).matches();
    }

    //Helper function to get student first name - reduce code redundancy and cleaner
    private static String getFirstName() {
        //prompts user to keep entering input until valid
        while (true) {
            String first_name;
            System.out.println("Enter student first name: ");
            first_name = menu.nextLine();
            //checking using helper function from above
            //if matched, return name
            //else prompt user again
            if(checkName(first_name)) {
                return first_name;
            } else {
                System.out.println("\nError: Invalid first name, please try again\n");
            }
        }
    }

    //Helper function to get student last name - reduce code redundancy and cleaner
    private static String getLastName() {
        //prompts user to keep entering input until valid
        while (true) {
            String last_name;
            System.out.println("Enter student last name: ");
            last_name = menu.nextLine();
            //checking using helper function
            //if matched, return name
            //else prompt user again
            if(checkName(last_name)) {
                return last_name;
            } else {
                System.out.println("\nError: Invalid last name, please try again\n");
            }
        }
    }

    //Helper function to check if email is valid input
    private static boolean checkEmail(String email) {
        //regex to ensure email format correct exmaple@example.com
        String email_regex = "^(.+)@(.+)$";
        Pattern email_ptrn = Pattern.compile(email_regex);
        //check if user input matches regex format
        return email_ptrn.matcher(email).matches();
    }

    //Helper function to get student email - reduce code redundancy and cleaner
    private static String getEmail() {
        while (true) {
            String email;
            System.out.println("Enter student email: ");
            email = menu.next();
            //if email matches format, return email
            //else, prompt user to enter email again
            if(checkEmail(email)) {
                return email;
            } else {
                System.out.println("\nError: Invalid email address, please try again\n");
            }
        }
    }

    //Helper function to check date format
    private static boolean checkDates (String date) {
        //regex format ####-##-##
        String date_regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";
        Pattern date_ptrn = Pattern.compile(date_regex);
        return date_ptrn.matcher(date).matches();
    }

    //Helper function to ensure date is valid
    private static boolean validDate(String date){
        int year, month, day;
        //slice string to get 'YYYY' 'MM' 'DD'
        year = Integer.parseInt(date.substring(0,4));
        month = Integer.parseInt(date.substring(5,7));
        day = Integer.parseInt(date.substring(8));

        //check if date is possible
        return (0 < year && year <= 9999) && (0 < month && month <=12) && (0 < day && day <= 31);
    }


    //Helper function to get date
    private static String getDate() {
        while(true) {
            String enrollment_date;
            System.out.println("Enter student enrollment date (YYYY-MM-DD)");
            enrollment_date = menu.next();
            //if date matches format, return date
            //else, prompt user to enter date again
            if (checkDates(enrollment_date) && validDate(enrollment_date)) {
                return enrollment_date;
            } else {
                System.out.println("\nError: Invalid enrollment date, please try again\n");
            }
        }
    }

    //Helper function to ensure student
    private static boolean checkID (String student_id){
        //
        try {
            //return if input is a positive integer
            return Integer.parseInt(student_id) > 0;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    //Helper function get student ID
    private static String getID() {
        while(true) {
            String student_id;
            System.out.println("Enter student ID: ");
            student_id = menu.next();
            //if id matches format, return id
            //else, prompt user to enter id again
            if (checkID(student_id)) {
                return student_id;
            } else {
                System.out.println("\nError: Invalid student ID, please try again\n");
            }
        }
    }

}
