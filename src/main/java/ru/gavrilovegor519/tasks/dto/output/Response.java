package ru.gavrilovegor519.tasks.dto.output;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Response<T> {
    private T data;
    private String message;
    private boolean success;

    public Response() {
    }

    public Response(T data, String message, boolean success) {
        this.data = data;
        this.message = message;
        this.success = success;
    }

    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
