package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateFeedReminderResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateFeedReminderResponse;
import org.springframework.http.ResponseEntity;

public class CreateFeedReminderPresenter extends ResponsePresenter<CreateFeedReminderResult> {
    @Override
    public void presentSuccess(CreateFeedReminderResult result) {
        BaseResponse<CreateFeedReminderResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(CreateFeedReminderResponse.from(result));
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
