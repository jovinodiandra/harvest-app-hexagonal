package org.harvest.application.dto.value;

import org.harvest.shared.exception.ValidationException;

public enum FeedPriceStatus {
    ACTIVE, NONACTIVE;

    public String getDisplayName(){
        return this == ACTIVE ? "active" : "non active";
    }

     public static FeedPriceStatus fromString(String value){
        for (FeedPriceStatus feedPriceStatus : FeedPriceStatus.values()){
            if (feedPriceStatus.name().equalsIgnoreCase(value)){
                return feedPriceStatus;
            }
        }
        throw new ValidationException("unknown status" + value);
     }
}
