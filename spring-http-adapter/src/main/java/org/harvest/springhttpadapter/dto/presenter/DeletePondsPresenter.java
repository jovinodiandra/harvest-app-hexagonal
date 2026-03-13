package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.DefaultResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.DeletePondsResponse;
import org.springframework.http.ResponseEntity;

public class DeletePondsPresenter extends ResponsePresenter<DefaultResult>{

    @Override
    public void presentSuccess(DefaultResult defaultResult) {
        BaseResponse<DeletePondsResponse> baseResponse = new BaseResponse<>();
        DeletePondsResponse deletePondsResponse = DeletePondsResponse.from(defaultResult);
        baseResponse.setData(deletePondsResponse);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
