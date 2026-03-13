package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.DefaultResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public class DeleteHarvestReminderPresenter extends ResponsePresenter<DefaultResult> {
    @Override
    public void presentSuccess(DefaultResult result) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        baseResponse.setMessage("Pengingat panen berhasil dihapus");
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
