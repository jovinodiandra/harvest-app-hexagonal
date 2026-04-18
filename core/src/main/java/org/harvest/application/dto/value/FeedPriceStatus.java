package org.harvest.application.dto.value;

import org.harvest.shared.exception.ValidationException;

public enum FeedPriceStatus {
    ACTIVE, NONACTIVE;

    public String getDisplayName(){
        return this == ACTIVE ? "active" : "non active";
    }

     public static FeedPriceStatus fromString(String value){
        for (FeedPriceStatus status : FeedPriceStatus.values()){
            if (status.name().equalsIgnoreCase(value)){
                return status;
            }
        }
        throw new ValidationException("unknown status" + value);
     }
}
