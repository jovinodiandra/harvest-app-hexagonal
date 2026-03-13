package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateUserResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateUserResponse;
import org.springframework.http.ResponseEntity;

public class CreateUserPresenter extends ResponsePresenter<CreateUserResult> {
    @Override
    public void presentSuccess(CreateUserResult createUserResult) {
        BaseResponse<CreateUserResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(CreateUserResponse.from(createUserResult));
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
