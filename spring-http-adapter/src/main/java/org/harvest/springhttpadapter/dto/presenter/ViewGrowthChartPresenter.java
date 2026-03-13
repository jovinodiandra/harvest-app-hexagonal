package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewGrowthChartResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewGrowthChartResponse;
import org.springframework.http.ResponseEntity;

public class ViewGrowthChartPresenter extends ResponsePresenter<ViewGrowthChartResult> {
    @Override
    public void presentSuccess(ViewGrowthChartResult result) {
        BaseResponse<ViewGrowthChartResponse> baseResponse = new BaseResponse<>();
        baseResponse.setData(ViewGrowthChartResponse.from(result));
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
