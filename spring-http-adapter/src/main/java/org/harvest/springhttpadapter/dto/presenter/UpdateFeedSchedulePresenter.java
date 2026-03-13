package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateFeedScheduleResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateFeedScheduleResponse;
import org.springframework.http.ResponseEntity;

public class UpdateFeedSchedulePresenter extends ResponsePresenter<UpdateFeedScheduleResult> {
    @Override
    public void presentSuccess(UpdateFeedScheduleResult result) {
        BaseResponse<UpdateFeedScheduleResponse> baseResponse = new BaseResponse<>();
        UpdateFeedScheduleResponse response = UpdateFeedScheduleResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
