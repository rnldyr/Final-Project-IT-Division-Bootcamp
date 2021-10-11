package com.example.kepo.model;

public class GetTodoDetailResponse {
    private int status;
    private String message;
    private GetTodoDetailResponseData data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GetTodoDetailResponseData getData() {
        return data;
    }

    public void setData(GetTodoDetailResponseData data) {
        this.data = data;
    }
}
