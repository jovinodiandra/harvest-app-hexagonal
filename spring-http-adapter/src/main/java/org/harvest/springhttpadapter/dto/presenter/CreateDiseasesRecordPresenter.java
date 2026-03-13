package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateDiseasesRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateDiseasesRecordResponse;
import org.springframework.http.ResponseEntity;

public class CreateDiseasesRecordPresenter extends ResponsePresenter<CreateDiseasesRecordResult> {
    @Override
    public void presentSuccess(CreateDiseasesRecordResult result) {
        BaseResponse<CreateDiseasesRecordResponse> baseResponse = new BaseResponse<>();
        CreateDiseasesRecordResponse response = CreateDiseasesRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
