Jad Fakhoury
101219772

Video link: https://youtu.be/X1DlLfIOBg0

SET-UP INSTRUCTIONS:

1. Launch PGADMIN and create a new Database. using the following data insert this data into your database:

create table students
(student_id serial primary key,
first_name varchar not null,
last_name varchar not null,
email varchar not null unique,
enrollment_date date
);

2. Once you have created your table, insert the following data:

INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES
('John', 'Doe', 'john.doe@example.com', '2023-09-01'),
('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01'),
('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02');

3. Launch the downloaded java files using your desired IDE or text editior - Note: I am using Intellij to run

4. To connect to the database, you must connect:
   url = "jdbc:postgresql://localhost:PORT_NUMBER/DATABASE_NAME";
   user = "YOUR USERNAME";
   password = "YOUR PASSWORD";
   .
   .
   .
   Class.forName("org.postgresql.Driver");
   connection = DriverManager.getConnection(url, user, password);
   statement = connection.createStatement();

5. Once the user has connected by filling in the required data, you can now run the program.

6. The user will be prompted with a menu and expected to select a number within the range (1-5).

   Please select a number to perform action

   1. Return table of students
   2. Add student to database
   3. Update student email
   4. Delete student from database
   5. Exit
