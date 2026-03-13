package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateGrowthRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateGrowthRecordResponse;
import org.springframework.http.ResponseEntity;

public class CreateGrowthRecordPresenter extends ResponsePresenter<CreateGrowthRecordResult> {
    @Override
    public void presentSuccess(CreateGrowthRecordResult result) {
        BaseResponse<CreateGrowthRecordResponse> baseResponse = new BaseResponse<>();
        CreateGrowthRecordResponse response = CreateGrowthRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
