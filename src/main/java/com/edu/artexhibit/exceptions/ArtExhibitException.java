package com.edu.artexhibit.exceptions;

import lombok.Getter;


@Getter
public class ArtExhibitException extends RuntimeException {
    private final ErrorCode code;
    private final String details;

    public ArtExhibitException(ErrorCode code, String details) {
        super(code.name());
        this.code = code;
        this.details = details;
    }

    public ArtExhibitException(ErrorCode code) {
        super(code.name());
        this.code = code;
        this.details = code.getMsg();
    }
}