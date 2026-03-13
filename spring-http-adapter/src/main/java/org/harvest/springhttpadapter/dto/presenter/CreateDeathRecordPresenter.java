package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateDeathRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateDeathRecordResponse;
import org.springframework.http.ResponseEntity;

public class CreateDeathRecordPresenter extends ResponsePresenter<CreateDeathRecordResult> {
    @Override
    public void presentSuccess(CreateDeathRecordResult result) {
        BaseResponse<CreateDeathRecordResponse> baseResponse = new BaseResponse<>();
        CreateDeathRecordResponse response = CreateDeathRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
