package com.november.exception;

/**
 * @author skrT
 * @create 2018/11/14 11:29
 */
//  这是全局异常
public class LibraryException extends RuntimeException {

    public LibraryException() {
        super();
    }

    public LibraryException(String message) {
        super(message);
    }

    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }

    public LibraryException(Throwable cause) {
        super(cause);
    }

    protected LibraryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
