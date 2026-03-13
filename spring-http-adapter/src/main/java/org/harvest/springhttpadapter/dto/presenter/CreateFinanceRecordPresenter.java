package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateFinanceRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateFinanceRecordResponse;
import org.springframework.http.ResponseEntity;

public class CreateFinanceRecordPresenter extends ResponsePresenter<CreateFinanceRecordResult> {
    @Override
    public void presentSuccess(CreateFinanceRecordResult result) {
        BaseResponse<CreateFinanceRecordResponse> baseResponse = new BaseResponse<>();
        CreateFinanceRecordResponse response = CreateFinanceRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
