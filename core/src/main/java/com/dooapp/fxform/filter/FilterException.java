package com.dooapp.fxform.filter;

/**
 * Created at 09/10/12 15:07.<br>
 *
 * @author Antoine Mischler <antoine@dooapp.com>
 */
public class FilterException extends Exception {

    public FilterException() {
    }

    public FilterException(String message) {
        super(message);
    }

    public FilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilterException(Throwable cause) {
        super(cause);
    }

    public FilterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}