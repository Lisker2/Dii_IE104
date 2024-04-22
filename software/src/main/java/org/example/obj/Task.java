package org.example.obj;

public class Task {
    public String email;
    public String submit_time;
    public String instance_path;
    public String status;

    public Task(String email, String submit_time, String instance_path, String status) {
        this.email = email;
        this.submit_time = submit_time;
        this.instance_path = instance_path;
        this.status = status;
    }

    public Task() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(String submit_time) {
        this.submit_time = submit_time;
    }

    public String getInstance_path() {
        return instance_path;
    }

    public void setInstance_path(String instance_path) {
        this.instance_path = instance_path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "email='" + email + '\'' +
                ", submit_time='" + submit_time + '\'' +
                ", instance_path='" + instance_path + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
