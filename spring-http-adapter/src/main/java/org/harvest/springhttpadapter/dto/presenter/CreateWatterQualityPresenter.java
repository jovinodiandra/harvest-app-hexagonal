package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateWatterQualityResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateWatterQualityResponse;
import org.springframework.http.ResponseEntity;

public class CreateWatterQualityPresenter extends ResponsePresenter<CreateWatterQualityResult> {
    @Override
    public void presentSuccess(CreateWatterQualityResult result) {
        BaseResponse<CreateWatterQualityResponse> baseResponse = new BaseResponse<>();
        CreateWatterQualityResponse response = CreateWatterQualityResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
