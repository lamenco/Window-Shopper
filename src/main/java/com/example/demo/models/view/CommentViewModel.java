package com.example.demo.models.view;

public class CommentViewModel {

    private String authorName;
    private String text;

    public CommentViewModel() {
    }



    public CommentViewModel(String authorName, String text) {
        this.authorName = authorName;
        this.text = text;
    }



    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
