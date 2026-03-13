package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.LoginResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.LoginResponse;
import org.springframework.http.ResponseEntity;

public class LoginPresenter  extends ResponsePresenter<LoginResult>
{
    @Override
    public void presentSuccess(LoginResult loginResult) {
        BaseResponse<LoginResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(LoginResponse.from(loginResult));
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
