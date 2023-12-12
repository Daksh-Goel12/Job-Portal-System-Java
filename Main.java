import jobportal.JobPortal;
import utils2.InputUtils;
import exceptions.JobPortalException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

class Main {
    static final String details_file_path = "details.txt";
    public static void main(String[] args) {
        JobPortal jobPortal = new JobPortal();

        System.out.print("Enter username: ");
        String username = InputUtils.getStringInput();
        System.out.print("Enter password: ");
        String password = InputUtils.getStringInput();

        try {
            authenticateAndStoreCredentials(username, password);
            System.out.println("Login successful!");
        } catch (JobPortalException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

            // Perform other actions like job search, application, etc.
            while (true) {
                System.out.println("1. Add a job");
                System.out.println("2. Display all jobs");
                System.out.println("3. Search for jobs");
                System.out.println("4. Apply for a job");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = InputUtils.getIntInput();

                try {
                    switch (choice) {
                        case 1:
                            System.out.print("Enter job title: ");
                            String title = InputUtils.getStringInput();
                            System.out.print("Enter company name: ");
                            String company = InputUtils.getStringInput();
                            System.out.print("Enter job description: ");
                            String description = InputUtils.getStringInput();
                            jobPortal.addJob(jobPortal.jobCount + 1, title, company, description);
                            jobPortal.insertJob(title, company, description);
                            break;
                        case 2:
                            jobPortal.displayAllJobs();
                            break;
                        case 3:
                            System.out.print("Enter a keyword to search for: ");
                            String keyword = InputUtils.getStringInput();
                            jobPortal.searchJobs(keyword);
                            break;
                        case 4:
                            System.out.print("Enter the job ID to apply for: ");
                            int jobIdToApply = InputUtils.getIntInput();
                            jobPortal.applyForJob(jobIdToApply);
                            break;
                        case 5:
                            System.out.println("Exiting... Goodbye!");
                            return;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (JobPortalException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        }

         static void authenticateAndStoreCredentials(String username, String password) throws JobPortalException {
        if (!isValidUsername(username)) {
            throw new JobPortalException("Invalid username format. Username must not contain numbers or special characters.");
        }

        // Store credentials in a file
        try {
            storeCredentialsToFile(username, password);
        } catch (IOException e) {
            throw new JobPortalException("Error storing credentials to file.");
        }
    }

     static void storeCredentialsToFile(String username, String password) throws IOException {
        File file = new File(details_file_path);
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write("Username: " + username + "\n");
            writer.write("Password: " + password + "\n");
        }
    }

     static boolean isValidUsername(String username) {
    return Pattern.matches("[a-zA-Z\\s]+", username);
} 
}

