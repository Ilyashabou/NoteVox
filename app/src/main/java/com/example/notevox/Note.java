package com.example.notevox;

public class Note {
    private String id;  // Change from int to String
    private String name;
    private String content;

    // Constructor with all fields
    public Note(String id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    // Default constructor
    public Note() {}

    // Getter and setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
