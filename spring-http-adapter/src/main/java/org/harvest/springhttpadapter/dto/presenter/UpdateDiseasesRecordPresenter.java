package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateDiseasesRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateDiseasesRecordResponse;
import org.springframework.http.ResponseEntity;

public class UpdateDiseasesRecordPresenter extends ResponsePresenter<UpdateDiseasesRecordResult> {
    @Override
    public void presentSuccess(UpdateDiseasesRecordResult result) {
        BaseResponse<UpdateDiseasesRecordResponse> baseResponse = new BaseResponse<>();
        UpdateDiseasesRecordResponse response = UpdateDiseasesRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
