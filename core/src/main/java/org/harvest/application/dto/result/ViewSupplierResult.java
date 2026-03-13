package org.harvest.application.dto.result;

import org.harvest.domain.Supplier;

import java.util.List;

public record ViewSupplierResult (List<Supplier> data){
}
