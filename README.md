Digital Classroom Poll

A simple Java Swing application that allows a teacher (Admin) to create and conduct live polls. Students can join, vote on the active poll, and the admin can view the results in real-time as a bar graph.

This project was built to demonstrate core Java concepts, including OOP, Java Swing for GUIs, JDBC for database connectivity, and modular design. [Source: 1]

Features

Admin Panel:

Create new polls with a question and four options [Source: 28].

View a list of all previously created polls.

"Launch" a poll, making it the single active poll for students [Source: 26].

"End" any active poll [Source: 27].

View graphical bar chart results for any poll at any time [Source: 33].

Student Panel:

Automatically finds and displays the one active poll [Source: 29].

If no poll is active, it informs the user.

Allows for a single vote per student (the UI disables after a successful vote) [Source: 32].

Data & Results:

All polls, options, and votes are saved permanently in a local SQLite database (poll_app.db) [Source: 21, 34].

The results panel dynamically queries the database and generates a bar chart using the XChart library [Source: 22].

Screenshots

<img width="490" height="249" alt="image" src="https://github.com/user-attachments/assets/8926bcf8-da2c-4208-a227-c8c5b7f9c03a" />
<img width="979" height="743" alt="image" src="https://github.com/user-attachments/assets/f3244848-5773-4f40-9718-65b74928abfd" />



Tech Stack

Core: Java

GUI: Java Swing [Source: 3, 7]

Database: SQLite (via sqlite-jdbc library) [Source: 21]

Graphing: XChart [Source: 22, 23]

Prerequisites

Before you begin, ensure you have the following:

Java Development Kit (JDK) 8 or higher (to use javac and java).

The following two .jar files (external libraries). You must download them and place them in the same folder as all the .java files.

SQLite-JDBC: Download from GitHub

XChart: Download from GitHub

How to Run

Open a terminal or command prompt.

Navigate (cd) into the project directory where all the .java and .jar files are located.

Compile all .java files:

(Note: You must replace the filenames below with the exact versions you downloaded!)

# On Windows (using semicolon ';')
javac -cp ".;sqlite-jdbc-YOUR-VERSION.jar;xchart-YOUR-VERSION.jar" *.java


# On Mac/Linux (using colon ':')
javac -cp ".:sqlite-jdbc-YOUR-VERSION.jar:xchart-YOUR-VERSION.jar" *.java


Run the main application:

# On Windows
java -cp ".;sqlite-jdbc-YOUR-VERSION.jar;xchart-YOUR-VERSION.jar" MainApp


# On Mac/Linux
java -cp ".:sqlite-jdbc-YOUR-VERSION.jar:xchart-YOUR-VERSION.jar" MainApp


The application will launch. A poll_app.db file will be automatically created in the same directory to store all your data.

Project File Structure

The code is segregated into several files for modularity and to demonstrate OOP principles [Source: 35]:

MainApp.java: The main entry point. A simple window to launch either the Admin or Student panel.

DatabaseHelper.java: A static class that handles all communication with the SQLite database (all JDBC logic is here).

Poll.java: A simple data object (POJO) to hold poll information.

AdminPanel.java: The GUI and event logic for the teacher's interface [Source: 25].

StudentPanel.java: The GUI and event logic for the student's voting interface [Source: 29].

ResultPanel.java: The GUI window responsible for creating and displaying the XChart bar graph [Source: 33].
