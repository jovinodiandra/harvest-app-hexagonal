package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateSeedResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateSeedResponse;
import org.springframework.http.ResponseEntity;

public class UpdateSeedPresenter extends ResponsePresenter<UpdateSeedResult>{
    @Override
    public void presentSuccess(UpdateSeedResult updateSeedResult) {
        BaseResponse<UpdateSeedResponse> baseResponse = new BaseResponse<>();
        UpdateSeedResponse updateSeedResponse = UpdateSeedResponse.from(updateSeedResult);
        baseResponse.setData(updateSeedResponse);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
