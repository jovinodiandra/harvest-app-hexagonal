package org.harvest.springhttpadapter.dto.presenter;

import org.harvest.application.dto.result.UpdatePondsResult;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.harvest.springhttpadapter.dto.response.UpdatePondsResponse;
import org.harvest.springhttpadapter.dto.response.UpdateUserResponse;
import org.springframework.http.ResponseEntity;

public class UpdatePondsPresenter extends ResponsePresenter<UpdatePondsResult>{
    @Override
    public void presentSuccess(UpdatePondsResult updatePondsResult) {
        BaseResponse<UpdatePondsResponse> baseResponse = new BaseResponse<>();
        UpdatePondsResponse updatePondsResponse = new UpdatePondsResponse(updatePondsResult.id(),updatePondsResult.name(),updatePondsResult.location(), updatePondsResult.capacity(),updatePondsResult.updatedAt());
        baseResponse.setData(updatePondsResponse);
        responseEntity = ResponseEntity.ok(baseResponse);
    }
}
