package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewWatterQualityResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewWatterQualityResponse;
import org.springframework.http.ResponseEntity;

public class ViewWatterQualityPresenter extends ResponsePresenter<ViewWatterQualityResult> {
    @Override
    public void presentSuccess(ViewWatterQualityResult result) {
        BaseResponse<ViewWatterQualityResponse> baseResponse = new BaseResponse<>();
        ViewWatterQualityResponse response = ViewWatterQualityResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
