package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewSupplierResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewSupplierResponse;
import org.springframework.http.ResponseEntity;

public class ViewSupplierPresenter extends ResponsePresenter<ViewSupplierResult> {
    @Override
    public void presentSuccess(ViewSupplierResult result) {
        BaseResponse<ViewSupplierResponse> baseResponse = new BaseResponse<>();
        ViewSupplierResponse response = ViewSupplierResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
