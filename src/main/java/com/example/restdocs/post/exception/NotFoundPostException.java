package com.example.restdocs.post.exception;

import com.example.restdocs.error.exception.BaseException;
import com.example.restdocs.error.exception.ErrorType;

public class NotFoundPostException extends BaseException {

    public NotFoundPostException() {
        super(ErrorType.NOT_FOUND_POST);
    }

}
