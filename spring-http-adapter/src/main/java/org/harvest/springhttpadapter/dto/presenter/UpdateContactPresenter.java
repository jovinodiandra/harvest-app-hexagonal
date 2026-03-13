package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateContactResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateContactResponse;
import org.springframework.http.ResponseEntity;

public class UpdateContactPresenter extends ResponsePresenter<UpdateContactResult> {
    @Override
    public void presentSuccess(UpdateContactResult result) {
        BaseResponse<UpdateContactResponse> baseResponse = new BaseResponse<>();
        UpdateContactResponse response = UpdateContactResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
