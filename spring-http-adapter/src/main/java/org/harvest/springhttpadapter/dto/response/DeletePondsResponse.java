package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.DefaultResult;

public record DeletePondsResponse(String message) {
    public static DeletePondsResponse from(DefaultResult message) {
        return new DeletePondsResponse("Ponds deleted successfully");
    }
}
