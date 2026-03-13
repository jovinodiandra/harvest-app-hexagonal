package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewFeedScheduleResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewFeedScheduleResponse;
import org.springframework.http.ResponseEntity;

public class ViewFeedSchedulePresenter extends ResponsePresenter<ViewFeedScheduleResult> {
    @Override
    public void presentSuccess(ViewFeedScheduleResult result) {
        BaseResponse<ViewFeedScheduleResponse> baseResponse = new BaseResponse<>();
        ViewFeedScheduleResponse response = ViewFeedScheduleResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
