package com.example.aitor.flickry;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.aitor.flickry.Resource.Status.SUCCESS;
import static com.example.aitor.flickry.Resource.Status.ERROR;
import static com.example.aitor.flickry.Resource.Status.LOADING;

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
public final class Resource<T> {

    @NonNull public final Status status;
    @Nullable public final Throwable error;
    @Nullable public final T data;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(Throwable t) {
        return error(t, null);
    }

    public static <T> Resource<T> error(Throwable t, @Nullable T data) {
        return new Resource<>(ERROR, data, t);
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(LOADING, null, null);
    }

    public boolean isLoading() {
        return status == LOADING;
    }

    public boolean isError() {
        return status == ERROR;
    }

    public boolean isSuccess() {
        return status == SUCCESS;
    }

    @Override
    public String toString() {
        return "Resource{"
            + "status=" + status
            + ", error='" + error
            + '\''
            + ", data=" + data + '}';
    }

    enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}
