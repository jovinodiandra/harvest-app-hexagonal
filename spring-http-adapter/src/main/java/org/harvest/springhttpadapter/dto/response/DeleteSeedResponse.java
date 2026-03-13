package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.DefaultResult;

public record DeleteSeedResponse(String message) {

    public static DeleteSeedResponse from(DefaultResult message) {
        return new DeleteSeedResponse("seed deleted successfully");
    }
}
