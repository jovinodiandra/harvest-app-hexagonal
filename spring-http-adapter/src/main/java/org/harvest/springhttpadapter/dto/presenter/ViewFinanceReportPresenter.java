package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewFinanceReportResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewFinanceReportResponse;
import org.springframework.http.ResponseEntity;

public class ViewFinanceReportPresenter extends ResponsePresenter<ViewFinanceReportResult> {
    @Override
    public void presentSuccess(ViewFinanceReportResult result) {
        BaseResponse<ViewFinanceReportResponse> baseResponse = new BaseResponse<>();
        ViewFinanceReportResponse response = ViewFinanceReportResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
