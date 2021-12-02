package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.controllers;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.commons.exceptions.ServiceException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class APIController {

    private static final int DEFAULT_PAGE_SIZE = 50;

    public Pageable generatePageable(Integer pageIndex, Integer pageSize) {
        if (pageIndex == null || pageIndex < 0) {
            pageIndex = 0;
        }

        if (pageSize == null || pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        return PageRequest.of(pageIndex, pageSize);
    }

    public ResponseEntity generateErrorResponseEntity(ServiceException e) {
        if (e.getHttpStatus() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }
}
