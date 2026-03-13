package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateHarvestRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateHarvestRecordResponse;
import org.springframework.http.ResponseEntity;

public class UpdateHarvestRecordPresenter extends ResponsePresenter<UpdateHarvestRecordResult> {
    @Override
    public void presentSuccess(UpdateHarvestRecordResult result) {
        BaseResponse<UpdateHarvestRecordResponse> baseResponse = new BaseResponse<>();
        UpdateHarvestRecordResponse response = UpdateHarvestRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
