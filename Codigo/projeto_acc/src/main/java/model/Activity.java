package model;

import java.util.Date;

public class Activity {

    private String name;
    private String description;
    private Date date;
    private int hours;
    private Status status;
    private String response;

    private ActivityType activityType;
    private Student student;

    private Document document;

    private boolean isVerified;

    public Activity() {
    }

    public Activity(String name, String description, Date date, int hours,
            Status status, String response,
            ActivityType activityType, Student student,
            Document document, boolean isVerified) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.hours = hours;
        this.status = status;
        this.response = response;
        this.activityType = activityType;
        this.student = student;
        this.document = document;
        this.isVerified = isVerified; // ✅ CORRETO AGORA
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Student getStudent() {
        return student;
    }

    public String getStudentEmail() {
        return (student != null ? student.getEmail() : "");
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    @Override
    public String toString() {
        return name + " (" + status + ")";
    }

}
