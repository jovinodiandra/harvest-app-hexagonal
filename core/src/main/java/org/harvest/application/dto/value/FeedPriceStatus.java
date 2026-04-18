package org.harvest.application.dto.value;

import org.harvest.shared.exception.ValidationException;

public enum StatusPrice {
    ACTIVE, NONACTIVE;

    public String getDisplayName(){
        return this == ACTIVE ? "active" : "non active";
    }

     public static StatusPrice fromString(String value){
        for (StatusPrice statusPrice : StatusPrice.values()){
            if (statusPrice.name().equalsIgnoreCase(value)){
                return statusPrice;
            }
        }
        throw new ValidationException("unknown status" + value);
     }
}
