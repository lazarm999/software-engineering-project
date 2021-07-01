package com.parovi.zadruga;

public class CustomResponse<T> {
    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public enum Status{
        OK, BAD_REQUEST, SERVER_ERROR, LOCAL_DB_ERROR, LOCAL_IMAGE_NOT_FOUND, SERVICE_NOT_AVAILABLE, EXCEPTION_ERROR,
        PERMISSION_NOT_GRANTED, TAGS_NOT_CHOSEN, NO_MORE_DATA
    }

    private Status status;
    private T body;
    private boolean isLocal;
    private String message;

    public CustomResponse(Status status, T body) {
        this.status = status;
        this.body = body;
    }

    public CustomResponse(Status status, T body, boolean isLocal) {
        this.status = status;
        this.body = body;
        this.isLocal = isLocal;
    }

    public CustomResponse(Status status, String message, boolean isLocal) {
        this.status = status;
        this.message = message;
        this.isLocal = isLocal;
    }

    public CustomResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public CustomResponse(Status status, T body, String message) {
        this.status = status;
        this.body = body;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
