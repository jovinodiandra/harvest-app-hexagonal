package org.harvest.domain;

import java.util.UUID;

public record Ponds (UUID id, String name,int capacity,String location,UUID organizationId){
    public Ponds update(String name,int capacity,String location){
        return new Ponds(id,name,capacity,location,organizationId);
    }
}
