package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewHarvestReportResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewHarvestReportResponse;
import org.springframework.http.ResponseEntity;

public class ViewHarvestReportPresenter extends ResponsePresenter<ViewHarvestReportResult> {
    @Override
    public void presentSuccess(ViewHarvestReportResult result) {
        BaseResponse<ViewHarvestReportResponse> baseResponse = new BaseResponse<>();
        ViewHarvestReportResponse response = ViewHarvestReportResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
