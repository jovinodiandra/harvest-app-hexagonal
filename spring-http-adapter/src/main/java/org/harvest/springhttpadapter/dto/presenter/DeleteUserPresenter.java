package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.DefaultResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.DeleteUserResponse;
import org.springframework.http.ResponseEntity;

public class DeleteUserPresenter extends ResponsePresenter<DefaultResult>{
    @Override
    public void presentSuccess(DefaultResult defaultResult) {
        BaseResponse<DeleteUserResponse> baseResponse = new BaseResponse<>();
        DeleteUserResponse deleteUserResponse = DeleteUserResponse.from(defaultResult);
        baseResponse.setData(deleteUserResponse);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
