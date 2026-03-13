package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateFeedReminderResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateFeedReminderResponse;
import org.springframework.http.ResponseEntity;

public class UpdateFeedReminderPresenter extends ResponsePresenter<UpdateFeedReminderResult> {
    @Override
    public void presentSuccess(UpdateFeedReminderResult result) {
        BaseResponse<UpdateFeedReminderResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(UpdateFeedReminderResponse.from(result));
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
