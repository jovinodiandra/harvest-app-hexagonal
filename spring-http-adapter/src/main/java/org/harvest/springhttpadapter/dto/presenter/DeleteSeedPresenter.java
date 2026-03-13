package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.DefaultResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.DeleteSeedResponse;
import org.springframework.http.ResponseEntity;

public class DeleteSeedPresenter extends ResponsePresenter<DefaultResult>{
    @Override
    public void presentSuccess(DefaultResult defaultResult) {
        BaseResponse<DeleteSeedResponse> baseResponse = new BaseResponse<>();
        DeleteSeedResponse deleteSeedResponse = DeleteSeedResponse.from(defaultResult);
        baseResponse.setData(deleteSeedResponse);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
