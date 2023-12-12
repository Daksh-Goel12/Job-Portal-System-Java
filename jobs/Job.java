package jobs;


public abstract class Job {
int jobId;
String title;
String company;
String description;


public Job(int jobId, String title, String company, String description) {
this.jobId = jobId;
this.title = title;
this.company = company;
this.description = description;
}

public int getJobId() {
return jobId;
}

public String getTitle() {
return title;
}

public String getCompany() {
return company;
}

public String getDescription() {
return description;
}

public abstract void displayJobDetails();
}
