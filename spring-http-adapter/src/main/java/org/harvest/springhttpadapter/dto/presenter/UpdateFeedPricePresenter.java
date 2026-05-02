package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdateFeedPriceResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdateFeedPriceResponse;
import org.springframework.http.ResponseEntity;

public class UpdateFeedPricePresenter extends ResponsePresenter<UpdateFeedPriceResult>{
    @Override
    public void presentSuccess(UpdateFeedPriceResult result) {
        BaseResponse<UpdateFeedPriceResponse> response = new BaseResponse<>();
        response.setData(UpdateFeedPriceResponse.from(result));
        responseEntity = ResponseEntity.ok(response);
    }
}
