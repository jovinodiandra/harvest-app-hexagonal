package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewHarvestRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewHarvestRecordResponse;
import org.springframework.http.ResponseEntity;

public class ViewHarvestRecordPresenter extends ResponsePresenter<ViewHarvestRecordResult> {
    @Override
    public void presentSuccess(ViewHarvestRecordResult result) {
        BaseResponse<ViewHarvestRecordResponse> baseResponse = new BaseResponse<>();
        ViewHarvestRecordResponse response = ViewHarvestRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
