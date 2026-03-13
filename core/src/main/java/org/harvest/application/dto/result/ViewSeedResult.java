package org.harvest.application.dto.result;

import org.harvest.domain.Seed;
import java.util.List;

public record ViewSeedResult (List<Seed> seeds){
}
