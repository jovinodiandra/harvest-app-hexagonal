package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewSupplierResult;
import org.harvest.domain.Supplier;

import java.util.List;
import java.util.UUID;

public record ViewSupplierResponse(List<SupplierItem> suppliers) {

    public static ViewSupplierResponse from(ViewSupplierResult result) {
        List<SupplierItem> items = result.data().stream()
                .map(SupplierItem::from)
                .toList();
        return new ViewSupplierResponse(items);
    }

    public record SupplierItem(UUID id, String name, String address, String phone, String email, String notes) {
        public static SupplierItem from(Supplier supplier) {
            return new SupplierItem(supplier.id(), supplier.name(), supplier.address(), supplier.phone(), supplier.email(), supplier.notes());
        }
    }
}
