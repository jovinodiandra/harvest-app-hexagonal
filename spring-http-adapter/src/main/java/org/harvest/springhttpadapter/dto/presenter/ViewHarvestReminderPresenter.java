package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewHarvestReminderResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewHarvestReminderResponse;
import org.springframework.http.ResponseEntity;

public class ViewHarvestReminderPresenter extends ResponsePresenter<ViewHarvestReminderResult> {
    @Override
    public void presentSuccess(ViewHarvestReminderResult result) {
        BaseResponse<ViewHarvestReminderResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(ViewHarvestReminderResponse.from(result));
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
