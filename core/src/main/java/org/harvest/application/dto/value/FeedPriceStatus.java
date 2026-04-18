package org.harvest.application.dto.value;

import org.harvest.shared.exception.ValidationException;

public enum FeedPriceStatus {
    ACTIVE, NONACTIVE;

    public String getDisplayName(){
        return this == ACTIVE ? "active" : "non active";
    }

     public static FeedPriceStatus fromString(String value){
<<<<<<< HEAD
        for (FeedPriceStatus feedPriceStatus : FeedPriceStatus.values()){
            if (feedPriceStatus.name().equalsIgnoreCase(value)){
                return feedPriceStatus;
=======
        for (FeedPriceStatus status : FeedPriceStatus.values()){
            if (status.name().equalsIgnoreCase(value)){
                return status;
>>>>>>> 5fca61f997e86ffa6a1f371ada4d939223defe5a
            }
        }
        throw new ValidationException("unknown status" + value);
     }
}
