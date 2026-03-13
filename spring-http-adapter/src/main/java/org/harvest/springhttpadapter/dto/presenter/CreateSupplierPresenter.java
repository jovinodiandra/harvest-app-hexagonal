package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateSupplierResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateSupplierResponse;
import org.springframework.http.ResponseEntity;

public class CreateSupplierPresenter extends ResponsePresenter<CreateSupplierResult> {
    @Override
    public void presentSuccess(CreateSupplierResult result) {
        BaseResponse<CreateSupplierResponse> baseResponse = new BaseResponse<>();
        CreateSupplierResponse response = CreateSupplierResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
