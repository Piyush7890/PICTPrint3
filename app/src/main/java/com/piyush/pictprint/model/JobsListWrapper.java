package com.piyush.pictprint.model;

import java.util.List;

public class JobsListWrapper {
    private List<JobStatus> jobs;
    private boolean success;

    public List<JobStatus> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobStatus> jobs) {
        this.jobs = jobs;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
}
