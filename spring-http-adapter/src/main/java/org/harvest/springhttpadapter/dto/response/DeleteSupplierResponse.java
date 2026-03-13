package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.DefaultResult;

public record DeleteSupplierResponse(String message) {

    public static DeleteSupplierResponse from(DefaultResult result) {
        return new DeleteSupplierResponse("Supplier berhasil dihapus");
    }
}
