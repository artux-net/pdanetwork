package net.artux.pdanetwork.models;

public class Status {

    private boolean success;
    private int code;
    private String description;
    private String token;

    public Status(int code, String description, boolean success) {
        this.success = success;
        this.code = code;
        this.description = description;
    }

    public Status(boolean success, String description) {
        this.success = success;
        if (success)
            code=200;
        else
            code=400;
        this.description = description;
    }

    public Status(String token){
        success = true;
        code = 0;
        this.token = token;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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


    @Override
    public String toString() {
        return "{\"success\" : " + success
                + ",\"code\" : " + code
                + ",\"description\" : " + (description == null ? null : "\"" + description + "\"") + ",\"token\" : " + (token == null ? null : "\"" + token + "\"") + "}";
    }
}
