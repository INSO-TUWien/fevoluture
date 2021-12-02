package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.exceptions;

import org.springframework.http.HttpStatus;

public class ElementNotFoundException extends ServiceException {
    public ElementNotFoundException(String message) {
        super(message);
    }
    public ElementNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }
}
