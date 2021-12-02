package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.exceptions;

import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {

    private HttpStatus httpStatus;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
