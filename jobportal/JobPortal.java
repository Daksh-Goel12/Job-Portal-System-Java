package jobportal;

import exceptions.JobPortalException;
import jobs.Job;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JobPortal extends Job implements Searchable {

Job[] jobs;
public int jobCount;
static final int MAX_SIZE = 20;

static final String INSERT_JOB_QUERY = "INSERT INTO jobs (title, company, description) VALUES (?, ?, ?)";
static final String SELECT_ALL_JOBS_QUERY = "SELECT * FROM jobs";

public JobPortal() {
super(0, "", "", "");
jobs = new Job[MAX_SIZE];
jobCount = 0;
}
static Connection getConnection() throws SQLException {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        throw new SQLException("MySQL JDBC driver not found", e);
    }

    // Replace "your_username" and "your_password" with your actual database username and password
    String url = "jdbc:mysql://localhost:3306/jobportal";
    String username = "root";
    String password = "root";
    return DriverManager.getConnection(url, username, password);
}


public void displayJobDetails() {
if (jobCount == 0) {
    System.out.println("No jobs found");
} 
else {
for (int i = 0; i < jobCount; i++) {
Job job = jobs[i];
System.out.println("Job ID: " + job.getJobId());
System.out.println("Title: " + job.getTitle());
System.out.println("Company: " + job.getCompany());
System.out.println("Description: " + job.getDescription());
System.out.println();
}
}
}


public void searchJobs(String keyword) {
    boolean found = false;
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM jobs WHERE LOWER(title) LIKE ? OR LOWER(company) LIKE ? OR LOWER(description) LIKE ?");
    ) {
        preparedStatement.setString(1, "%" + keyword.toLowerCase() + "%");
        preparedStatement.setString(2, "%" + keyword.toLowerCase() + "%");
        preparedStatement.setString(3, "%" + keyword.toLowerCase() + "%");

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                System.out.println("Job ID: " + resultSet.getInt("job_id"));
                System.out.println("Title: " + resultSet.getString("title"));
                System.out.println("Company: " + resultSet.getString("company"));
                System.out.println("Description: " + resultSet.getString("description"));
                System.out.println();
                found = true;
            }
        }
    } catch (SQLException e) {
        System.out.println("Error searching for jobs: " + e.getMessage());
    }

    if (!found) {
        System.out.println("No matching jobs found");
    }
}


public void addJob(int jobId, String title, String company, String description) throws JobPortalException {
if (jobCount < MAX_SIZE) {
    
Job newJob = new Job(jobId, title, company, description) {
public void displayJobDetails() {
System.out.println("Job ID: " + getJobId());
System.out.println("Title: " + getTitle());
System.out.println("Company: " + getCompany());
System.out.println("Description: " + getDescription());
System.out.println();
}
};

jobs[jobCount] = newJob;
jobCount++;
System.out.println("Job added successfully");
} 
else {
throw new JobPortalException("Job portal is full, cannot add more jobs");
}
}

public void applyForJob(int jobId) throws JobPortalException {
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM jobs WHERE job_id = ?")) {
        preparedStatement.setInt(1, jobId);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                System.out.println("You have applied for the following job:");
                System.out.println("Job ID: " + resultSet.getInt("job_id"));
                System.out.println("Title: " + resultSet.getString("title"));
                System.out.println("Company: " + resultSet.getString("company"));
                System.out.println("Description: " + resultSet.getString("description"));
                System.out.println("Application submitted successfully");
            } else {
                throw new JobPortalException("Invalid job ID. Please enter a valid job ID.");
            }
        }
    } catch (SQLException e) {
        throw new JobPortalException("Error applying for job: " + e.getMessage());
    }
}



public void insertJob(String title, String company, String description) throws JobPortalException {
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(INSERT_JOB_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {

        preparedStatement.setString(1, title);
        preparedStatement.setString(2, company);
        preparedStatement.setString(3, description);

        preparedStatement.executeUpdate();

        // Retrieve the auto-generated job_id
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                System.out.println("Job added successfully to the database with ID: " + generatedId);
            } else {
                throw new JobPortalException("Error retrieving generated job ID.");
            }
        }

    } catch (SQLException e) {
        throw new JobPortalException("Error adding job to the database: " + e.getMessage());
    }
}

public void displayAllJobs() throws JobPortalException {
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_JOBS_QUERY);
         ResultSet resultSet = preparedStatement.executeQuery()) {

        while (resultSet.next()) {
            System.out.println("Job ID: " + resultSet.getInt("job_id"));
            System.out.println("Title: " + resultSet.getString("title"));
            System.out.println("Company: " + resultSet.getString("company"));
            System.out.println("Description: " + resultSet.getString("description"));
            System.out.println();
        }
    } catch (SQLException e) {
        throw new JobPortalException("Error retrieving jobs from the database: " + e.getMessage());
    }
}
}

