package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.CreateFeedPriceResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.CreateFeedPriceResponse;
import org.springframework.http.ResponseEntity;

public class CreateFeedPricePresenter extends ResponsePresenter<CreateFeedPriceResult> {
    @Override
    public void presentSuccess(CreateFeedPriceResult createFeedPriceResult) {
        BaseResponse<CreateFeedPriceResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(CreateFeedPriceResponse.from(createFeedPriceResult));
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
