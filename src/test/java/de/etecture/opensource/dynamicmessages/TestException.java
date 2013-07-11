package de.etecture.opensource.dynamicmessages;

/**
 *
 * @author rhk
 */
public class TestException extends Exception {

    private static final long serialVersionUID = 1L;

    public TestException(String message) {
        super(message);
    }

    public TestException(String message, Throwable cause) {
        super(message, cause);
    }
}
