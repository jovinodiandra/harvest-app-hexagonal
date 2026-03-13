package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateFinanceRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateFinanceRecordResponse;
import org.springframework.http.ResponseEntity;

public class UpdateFinanceRecordPresenter extends ResponsePresenter<UpdateFinanceRecordResult> {
    @Override
    public void presentSuccess(UpdateFinanceRecordResult result) {
        BaseResponse<UpdateFinanceRecordResponse> baseResponse = new BaseResponse<>();
        UpdateFinanceRecordResponse response = UpdateFinanceRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
