package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateSeedResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateSeedResponse;
import org.springframework.http.ResponseEntity;

public class CreateSeedPresenter extends ResponsePresenter<CreateSeedResult>{
    @Override
    public void presentSuccess(CreateSeedResult createSeedResult) {
        BaseResponse<CreateSeedResponse> baseResponse = new BaseResponse<>();
        CreateSeedResponse createSeedResponse = CreateSeedResponse.from(createSeedResult);
        baseResponse.setData(createSeedResponse);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
