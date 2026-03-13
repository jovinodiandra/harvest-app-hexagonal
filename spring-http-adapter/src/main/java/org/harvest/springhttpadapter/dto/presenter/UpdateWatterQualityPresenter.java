package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateWatterQualityResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateWatterQualityResponse;
import org.springframework.http.ResponseEntity;

public class UpdateWatterQualityPresenter extends ResponsePresenter<UpdateWatterQualityResult> {
    @Override
    public void presentSuccess(UpdateWatterQualityResult result) {
        BaseResponse<UpdateWatterQualityResponse> baseResponse = new BaseResponse<>();
        UpdateWatterQualityResponse response = UpdateWatterQualityResponse.from(result);
        baseResponse.setData(response);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
