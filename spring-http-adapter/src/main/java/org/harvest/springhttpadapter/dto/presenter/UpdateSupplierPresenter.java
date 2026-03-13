package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateSupplierResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateSupplierResponse;
import org.springframework.http.ResponseEntity;

public class UpdateSupplierPresenter extends ResponsePresenter<UpdateSupplierResult> {
    @Override
    public void presentSuccess(UpdateSupplierResult result) {
        BaseResponse<UpdateSupplierResponse> baseResponse = new BaseResponse<>();
        UpdateSupplierResponse response = UpdateSupplierResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
