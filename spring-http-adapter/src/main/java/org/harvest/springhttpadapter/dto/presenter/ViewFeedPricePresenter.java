package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewFeedPriceResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewFeedPriceResponse;
import org.springframework.http.ResponseEntity;

public class ViewFeedPricePresenter extends ResponsePresenter<ViewFeedPriceResult> {
    @Override
    public void presentSuccess(ViewFeedPriceResult result) {
        BaseResponse<ViewFeedPriceResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(ViewFeedPriceResponse.from(result));
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
