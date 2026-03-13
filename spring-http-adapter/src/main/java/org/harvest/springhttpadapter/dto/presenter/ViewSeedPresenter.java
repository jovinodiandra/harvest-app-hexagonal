package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewSeedResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewSeedResponse;
import org.springframework.http.ResponseEntity;

public class ViewSeedPresenter extends ResponsePresenter<ViewSeedResult>{
    @Override
    public void presentSuccess(ViewSeedResult viewSeedResult) {
        BaseResponse<ViewSeedResponse> baseResponse = new BaseResponse<>();
        ViewSeedResponse viewSeedResponse = ViewSeedResponse.from(viewSeedResult);
        baseResponse.setData(viewSeedResponse);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
