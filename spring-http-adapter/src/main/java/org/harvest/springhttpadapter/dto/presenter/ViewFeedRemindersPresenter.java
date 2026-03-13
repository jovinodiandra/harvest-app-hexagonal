package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewFeedRemindersResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewFeedRemindersResponse;
import org.springframework.http.ResponseEntity;

public class ViewFeedRemindersPresenter extends ResponsePresenter<ViewFeedRemindersResult> {
    @Override
    public void presentSuccess(ViewFeedRemindersResult result) {
        BaseResponse<ViewFeedRemindersResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(ViewFeedRemindersResponse.from(result));
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
