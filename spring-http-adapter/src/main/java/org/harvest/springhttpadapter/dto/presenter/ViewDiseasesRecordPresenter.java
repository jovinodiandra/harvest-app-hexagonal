package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewDiseasesRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewDiseasesRecordResponse;
import org.springframework.http.ResponseEntity;

public class ViewDiseasesRecordPresenter extends ResponsePresenter<ViewDiseasesRecordResult> {
    @Override
    public void presentSuccess(ViewDiseasesRecordResult result) {
        BaseResponse<ViewDiseasesRecordResponse> baseResponse = new BaseResponse<>();
        ViewDiseasesRecordResponse response = ViewDiseasesRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
