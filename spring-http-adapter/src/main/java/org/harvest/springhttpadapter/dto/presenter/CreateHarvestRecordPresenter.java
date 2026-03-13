package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateHarvestRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateHarvestRecordResponse;
import org.springframework.http.ResponseEntity;

public class CreateHarvestRecordPresenter extends ResponsePresenter<CreateHarvestRecordResult> {
    @Override
    public void presentSuccess(CreateHarvestRecordResult result) {
        BaseResponse<CreateHarvestRecordResponse> baseResponse = new BaseResponse<>();
        CreateHarvestRecordResponse response = CreateHarvestRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
