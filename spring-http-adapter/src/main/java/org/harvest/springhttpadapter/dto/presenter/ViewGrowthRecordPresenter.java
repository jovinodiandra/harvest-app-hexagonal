package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewGrowthRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewGrowthRecordResponse;
import org.springframework.http.ResponseEntity;

public class ViewGrowthRecordPresenter extends ResponsePresenter<ViewGrowthRecordResult> {
    @Override
    public void presentSuccess(ViewGrowthRecordResult result) {
        BaseResponse<ViewGrowthRecordResponse> baseResponse = new BaseResponse<>();
        ViewGrowthRecordResponse response = ViewGrowthRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
