package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateHarvestReminderResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateHarvestReminderResponse;
import org.springframework.http.ResponseEntity;

public class CreateHarvestReminderPresenter extends ResponsePresenter<CreateHarvestReminderResult> {
    @Override
    public void presentSuccess(CreateHarvestReminderResult result) {
        BaseResponse<CreateHarvestReminderResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(CreateHarvestReminderResponse.from(result));
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
