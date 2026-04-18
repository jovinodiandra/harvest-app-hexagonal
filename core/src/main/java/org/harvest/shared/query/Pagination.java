package org.harvest.shared.exception;

import org.harvest.shared.exception.ValidationException;

public class Pagination {


    private final int page;
    private final int limit;

    public Pagination(int page, int limit) {
        if (page < 1){
            throw new ValidationException("page must be greater than 1");
        }
        if (limit < 1 || limit > 100){
            throw new ValidationException("limit must be between 1 and 100");
        }
        this.page = page;
        this.limit = limit;
    }


    public int getLimit() {
        return limit;
    }

    public int offset(  ){
        return (page -1) * limit;
    }
}
