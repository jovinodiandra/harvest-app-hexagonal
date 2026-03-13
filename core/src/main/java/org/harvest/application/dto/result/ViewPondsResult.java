package org.harvest.application.dto.result;

import org.harvest.domain.Ponds;

import java.util.List;

public record ViewPondsResult(List<Ponds> ponds) {
}
