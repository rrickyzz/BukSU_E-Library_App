package com.dacoders.buksue_libraryapp.CollectionDataAccessObject;

public class Collection_DAO {
    public Collection_DAO() {
    }

    public Collection_DAO(String author, String category, String datePublish, String fileName, String fileUrl,String permission, String title) {
        this.author = author;
        this.category = category;
        this.datePublish = datePublish;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.permission = permission;
        this.title = title;


    }

    String author;
    String category;
    String datePublish;
    String fileName;
    String fileUrl;
    String permission;
    String title;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
