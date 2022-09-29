package com.github.WeeiaEduTeam.InfinityFinanceAPI.exception;


public class ResourceNotFoundException extends RuntimeException {

    public static ResourceNotFoundException createWith(String errorMessage) {
        return new ResourceNotFoundException(errorMessage);
    }

    public static ResourceNotFoundException createWith(long id) {
        return new ResourceNotFoundException(id);
    }

    private ResourceNotFoundException(long id) {
        super(String.format("Resource with id: %s not found", id));
    }

    private ResourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}