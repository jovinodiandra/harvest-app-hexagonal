package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewUserResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewUserResponse;
import org.springframework.http.ResponseEntity;

public class ViewUserPresenter extends ResponsePresenter<ViewUserResult>{
    @Override
    public void presentSuccess(ViewUserResult viewUserResult) {
        BaseResponse<ViewUserResponse> baseResponse = new BaseResponse<>();
        ViewUserResponse viewUserResponse = ViewUserResponse.from(viewUserResult);
        baseResponse.setData(viewUserResponse);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
