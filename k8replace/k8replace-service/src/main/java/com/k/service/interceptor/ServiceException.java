package com.k.service.interceptor;

public class ServiceException extends Exception {

    private static final long serialVersionUID = 8766746989649456423L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }
}
