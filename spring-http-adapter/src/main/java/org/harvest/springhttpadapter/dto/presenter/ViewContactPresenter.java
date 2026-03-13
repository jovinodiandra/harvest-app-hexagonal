package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewContactResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewContactResponse;
import org.springframework.http.ResponseEntity;

public class ViewContactPresenter extends ResponsePresenter<ViewContactResult> {
    @Override
    public void presentSuccess(ViewContactResult result) {
        BaseResponse<ViewContactResponse> baseResponse = new BaseResponse<>();
        ViewContactResponse response = ViewContactResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
