package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewDeathRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewDeathRecordResponse;
import org.springframework.http.ResponseEntity;

public class ViewDeathRecordPresenter extends ResponsePresenter<ViewDeathRecordResult> {
    @Override
    public void presentSuccess(ViewDeathRecordResult result) {
        BaseResponse<ViewDeathRecordResponse> baseResponse = new BaseResponse<>();
        ViewDeathRecordResponse response = ViewDeathRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
