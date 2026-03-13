package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreatePondsResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreatePondsResponse;
import org.springframework.http.ResponseEntity;

public class CreatePondsPresenter extends ResponsePresenter<CreatePondsResult> {
    @Override
    public void presentSuccess(CreatePondsResult createPondsResult) {
        BaseResponse<CreatePondsResponse> baseResponse = new BaseResponse<>();
        CreatePondsResponse createPondsResponse = CreatePondsResponse.from(createPondsResult);
        baseResponse.setData(createPondsResponse);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
