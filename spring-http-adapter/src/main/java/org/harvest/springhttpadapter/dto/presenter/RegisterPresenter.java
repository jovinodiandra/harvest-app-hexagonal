package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.RegisterResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.LoginResponse;
import org.harvest.springhttpadapter.dto.response.RegisterResponse;
import org.springframework.http.ResponseEntity;

public class RegisterPresenter extends ResponsePresenter<RegisterResult> {
    @Override
    public void presentSuccess(RegisterResult registerResult) {
        BaseResponse<RegisterResponse> baseResponse = new BaseResponse<>();
        RegisterResponse registerResponse = RegisterResponse.from(registerResult);
        baseResponse.setData(registerResponse);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
