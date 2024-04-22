package org.example.obj;

public class Arrange {
    public String email;
    public String instance_path;
    public String solution_path;
    public String time;

    public Arrange(String email, String instance_path, String solution_path, String time) {
        this.email = email;
        this.instance_path = instance_path;
        this.solution_path = solution_path;
        this.time = time;
    }

    public Arrange() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstance_path() {
        return instance_path;
    }

    public void setInstance_path(String instance_path) {
        this.instance_path = instance_path;
    }

    public String getSolution_path() {
        return solution_path;
    }

    public void setSolution_path(String solution_path) {
        this.solution_path = solution_path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Arrange{" +
                "email='" + email + '\'' +
                ", instance_path='" + instance_path + '\'' +
                ", solution_path='" + solution_path + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
