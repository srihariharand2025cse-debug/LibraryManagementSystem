package com.example.demo.dto;

public class BookRequest {
    private String title;
    private float price;
    private String authorName;

    public BookRequest() {
    }

    public BookRequest(String title, float price, String authorName) {
        this.title = title;
        this.price = price;
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
