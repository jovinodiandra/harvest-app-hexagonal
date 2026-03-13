package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateDeathRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateDeathRecordResponse;
import org.springframework.http.ResponseEntity;

public class UpdateDeathRecordPresenter extends ResponsePresenter<UpdateDeathRecordResult> {
    @Override
    public void presentSuccess(UpdateDeathRecordResult result) {
        BaseResponse<UpdateDeathRecordResponse> baseResponse = new BaseResponse<>();
        UpdateDeathRecordResponse response = UpdateDeathRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
