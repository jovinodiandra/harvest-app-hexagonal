package org.harvest.application.dto.result;

import org.harvest.domain.GrowthRecord;

import java.util.List;

public record ViewGrowthRecordResult (List<GrowthRecord> growthRecords){
}
