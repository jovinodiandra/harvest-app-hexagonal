package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewFishStatisticResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewFishStatisticsResponse;
import org.springframework.http.ResponseEntity;

public class ViewFishStatisticsPresenter extends ResponsePresenter<ViewFishStatisticResult> {
    @Override
    public void presentSuccess(ViewFishStatisticResult result) {
        BaseResponse<ViewFishStatisticsResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(ViewFishStatisticsResponse.from(result));
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
