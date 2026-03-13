package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateGrowthRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateGrowthRecordResponse;
import org.springframework.http.ResponseEntity;

public class UpdateGrowthRecordPresenter extends ResponsePresenter<UpdateGrowthRecordResult> {
    @Override
    public void presentSuccess(UpdateGrowthRecordResult result) {
        BaseResponse<UpdateGrowthRecordResponse> baseResponse = new BaseResponse<>();
        UpdateGrowthRecordResponse response = UpdateGrowthRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
