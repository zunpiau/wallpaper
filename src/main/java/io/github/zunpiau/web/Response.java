package io.github.zunpiau.web;

import com.fasterxml.jackson.annotation.JsonValue;

public class Response<T> {

    private ResponseCode code;
    private T data;

    public Response(ResponseCode code) {
        this.code = code;
    }

    public Response(T data) {
        code = ResponseCode.OK;
        this.data = data;
    }

    public Response(ResponseCode code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseCode getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public enum ResponseCode {

        OK(200),
        BAD_REQUEST(400),
        INNER_EXCEPTION(500);

        private int statusCode;

        ResponseCode(int statusCode) {
            this.statusCode = statusCode;
        }

        @JsonValue
        public int getStatusCode() {
            return statusCode;
        }
    }
}
