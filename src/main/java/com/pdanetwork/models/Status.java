package com.pdanetwork.models;

public class Status {

    private boolean success;
    private int code;
    private String description;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean getSuccess() {
        return success;
    }

    public String toString(){
        return "success: " + success + "\n"
                + "code: " + code + "\n"
                + "desc: " + description + "\n";

    }
}

