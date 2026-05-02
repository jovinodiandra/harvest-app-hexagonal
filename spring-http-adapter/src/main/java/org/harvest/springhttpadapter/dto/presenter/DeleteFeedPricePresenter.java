package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.DeleteFeedPriceResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.DeleteFeedPriceResponse;
import org.springframework.http.ResponseEntity;

public class DeleteFeedPricePresenter extends ResponsePresenter<DeleteFeedPriceResult>{
    @Override
    public void presentSuccess(DeleteFeedPriceResult result) {
        BaseResponse<DeleteFeedPriceResponse> response = new BaseResponse<>();
        response.setData(DeleteFeedPriceResponse.from(result));
        responseEntity = ResponseEntity.ok(response) ;
    }
}
