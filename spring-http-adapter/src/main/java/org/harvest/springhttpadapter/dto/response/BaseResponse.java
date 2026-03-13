package org.harvest.springhttpadapter.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseResponse <T>{
    private boolean success = true;
    private String message = "success";
    private T data;
}
