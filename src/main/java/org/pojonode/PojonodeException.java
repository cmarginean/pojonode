package org.pojonode;

/**
 * @author Cosmin Marginean, Oct 26, 2010
 */
public class PojonodeException extends Exception {

    public PojonodeException() {
    }

    public PojonodeException(String message) {
        super(message);
    }

    public PojonodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PojonodeException(Throwable cause) {
        super(cause);
    }
}
