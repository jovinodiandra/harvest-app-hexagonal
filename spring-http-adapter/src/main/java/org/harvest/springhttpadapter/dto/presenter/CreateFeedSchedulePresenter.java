package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateFeedScheduleResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateFeedScheduleResponse;
import org.springframework.http.ResponseEntity;

public class CreateFeedSchedulePresenter extends ResponsePresenter<CreateFeedScheduleResult> {
    @Override
    public void presentSuccess(CreateFeedScheduleResult result) {
        BaseResponse<CreateFeedScheduleResponse> baseResponse = new BaseResponse<>();
        CreateFeedScheduleResponse response = CreateFeedScheduleResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
