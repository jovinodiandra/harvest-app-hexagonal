package org.harvest.springhttpadapter.dto.presenter;

import lombok.Getter;
import org.harvest.application.port.outbound.Presenter;
import org.harvest.shared.exception.AuthenticationException;
import org.harvest.shared.exception.ValidationException;
import org.harvest.springhttpadapter.dto.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Getter
public abstract class ResponsePresenter <I> implements Presenter<I> {
    protected ResponseEntity<?> responseEntity;

    @Override
    public void presentError(Exception e) {
        BaseResponse<?> baseResponse = new BaseResponse<>();
        baseResponse.setMessage(e.getMessage());
        baseResponse.setSuccess(false);
        responseEntity = ResponseEntity.status(fromException(e)).body(baseResponse);
    }

    private HttpStatus fromException(Exception e){
        if(e instanceof ValidationException){
            return HttpStatus.BAD_REQUEST;
        }
        if(e instanceof AuthenticationException){
            return HttpStatus.UNAUTHORIZED;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
