package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.ViewPondsResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.ViewPondsResponse;
import org.springframework.http.ResponseEntity;

public class ViewPondsPresenter extends ResponsePresenter<ViewPondsResult>{
    @Override
    public void presentSuccess(ViewPondsResult pondsResult) {
        BaseResponse<ViewPondsResponse> baseResponse = new BaseResponse<>();
        ViewPondsResponse viewPondsResponse = ViewPondsResponse.from(pondsResult);
        baseResponse.setData(viewPondsResponse );
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
