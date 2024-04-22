package org.example.obj;

public class Event {
    public String id;
    public String start;
    public String end;
    public String title;
    public String color;
    public String className;

    public Event(String id, String start, String end, String title, String color, String className) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.title = title;
        this.color = color;
        this.className = className;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", title='" + title + '\'' +
                ", color='" + color + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
