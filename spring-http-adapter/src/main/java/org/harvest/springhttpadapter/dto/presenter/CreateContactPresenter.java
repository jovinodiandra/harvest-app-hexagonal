package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateContactResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateContactResponse;
import org.springframework.http.ResponseEntity;

public class CreateContactPresenter extends ResponsePresenter<CreateContactResult> {
    @Override
    public void presentSuccess(CreateContactResult result) {
        BaseResponse<CreateContactResponse> baseResponse = new BaseResponse<>();
        CreateContactResponse response = CreateContactResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
