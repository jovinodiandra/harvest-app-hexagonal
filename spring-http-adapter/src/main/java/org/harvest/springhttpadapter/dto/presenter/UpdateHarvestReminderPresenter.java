package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateHarvestReminderResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateHarvestReminderResponse;
import org.springframework.http.ResponseEntity;

public class UpdateHarvestReminderPresenter extends ResponsePresenter<UpdateHarvestReminderResult> {
    @Override
    public void presentSuccess(UpdateHarvestReminderResult result) {
        BaseResponse<UpdateHarvestReminderResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(UpdateHarvestReminderResponse.from(result));
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
