package ru.kata.spring.boot_security.demo.model;

public class AdultUrl {
    private String url;
    private boolean success;

    public AdultUrl(String url, boolean success) {
        this.url = url;
        this.success = success;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
