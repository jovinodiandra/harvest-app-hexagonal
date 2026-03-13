package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewFinanceRecordResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewFinanceRecordResponse;
import org.springframework.http.ResponseEntity;

public class ViewFinanceRecordPresenter extends ResponsePresenter<ViewFinanceRecordResult> {
    @Override
    public void presentSuccess(ViewFinanceRecordResult result) {
        BaseResponse<ViewFinanceRecordResponse> baseResponse = new BaseResponse<>();
        ViewFinanceRecordResponse response = ViewFinanceRecordResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
