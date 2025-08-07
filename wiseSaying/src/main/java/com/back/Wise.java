package com.back;

public class Wise {
    private int id;  //명언 번호
    private String quote;    //명언 내용
    private String author;   //명언 작가

    public Wise(int id, String quote, String author) {
        this.id = id;
        this.quote = quote;
        this.author = author;
    }

    public int getId() {
        return this.id;
    }

    public String getQuote() {
        return this.quote;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
