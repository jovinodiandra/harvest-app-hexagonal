package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateUserResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateUserResponse;
import org.springframework.http.ResponseEntity;

public class UpdateUserPresenter extends ResponsePresenter<UpdateUserResult>{
    @Override
    public void presentSuccess(UpdateUserResult updateUserResult) {
        BaseResponse<UpdateUserResponse> baseResponse = new BaseResponse<>();
        UpdateUserResponse updateUserResponse = UpdateUserResponse.from(updateUserResult);
        baseResponse.setData(updateUserResponse);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
