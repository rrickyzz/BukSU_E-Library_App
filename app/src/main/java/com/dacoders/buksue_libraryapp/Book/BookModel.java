package com.dacoders.buksue_libraryapp.Book;

public class BookModel {
    String DateMonthAdded;
    String DateYearAdded;
    String author;
    String bookAddedBy;
    String bookDownload;
    String bookRead;
    String callNumber;
    String datePublish;
    String fileName;
    String fileUrl;
    String isbn;
    String location;
    String pageNumber;
    String permission;
    String section;
    String subject;
    String title;

    public BookModel() {
    }

    public BookModel(String dateMonthAdded, String dateYearAdded, String author, String bookAddedBY, String bookDownloade, String bookRead, String callNumber, String datePublish, String fileName, String fileUrl, String isbn, String location, String pageNumber, String permission, String section, String subject, String title) {
        this.DateMonthAdded = dateMonthAdded;
        this.DateYearAdded = dateYearAdded;
        this.author = author;
        this.bookAddedBy = bookAddedBY;
        this.bookDownload = bookDownloade;
        this.bookRead = bookRead;
        this.callNumber = callNumber;
        this.datePublish = datePublish;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.isbn = isbn;
        this.location = location;
        this.pageNumber = pageNumber;
        this.permission = permission;
        this.section = section;
        this.subject = subject;
        this.title = title;
    }

    public String getDateMonthAdded() {
        return DateMonthAdded;
    }

    public void setDateMonthAdded(String dateMonthAdded) {
        DateMonthAdded = dateMonthAdded;
    }

    public String getDateYearAdded() {
        return DateYearAdded;
    }

    public void setDateYearAdded(String dateYearAdded) {
        DateYearAdded = dateYearAdded;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookAddedBY() {
        return bookAddedBy;
    }

    public void setBookAddedBY(String bookAddedBY) {
        this.bookAddedBy = bookAddedBY;
    }

    public String getBookDownloade() {
        return bookDownload;
    }

    public void setBookDownloade(String bookDownloade) {
        this.bookDownload = bookDownloade;
    }

    public String getBookRead() {
        return bookRead;
    }

    public void setBookRead(String bookRead) {
        this.bookRead = bookRead;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getDatePublish() {
        return datePublish;
    }

    public void setDatePublish(String datePublish) {
        this.datePublish = datePublish;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
