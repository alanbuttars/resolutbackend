package com.alanbuttars.resolut.backend.models;

/**
 * Simple POJO emulating a service-level action result.
 *
 * @param <T> The target class
 */
public class Result<T> {
    private final boolean succeeded;
    private final T target;
    private final String errorMessage;

    private Result(boolean succeeded, T target, String errorMessage) {
        this.succeeded = succeeded;
        this.target = target;
        this.errorMessage = errorMessage;
    }

    /**
     * Creates a successful result with its expected target value.
     *
     * @param target
     * @param <T>    The target class
     * @return
     */
    public static <T> Result<T> success(T target) {
        return new Result<T>(true, target, null);
    }

    /**
     * Creates a failure result with its expected error message.
     *
     * @param errorMessage
     * @param <T>          The target class
     * @return
     */
    public static <T> Result<T> failure(String errorMessage) {
        return new Result<T>(false, null, errorMessage);
    }

    public boolean succeeded() {
        return succeeded;
    }

    public T getTarget() {
        return target;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
