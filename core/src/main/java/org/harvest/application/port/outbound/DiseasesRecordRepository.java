package org.harvest.application.port.outbound;

import org.harvest.domain.DiseasesRecord;

import java.util.List;
import java.util.UUID;

public interface DiseasesRecordRepository {
    UUID nextId();
    void save(DiseasesRecord diseasesRecord);
    void update(DiseasesRecord diseasesRecord);
    void delete(DiseasesRecord diseasesRecord);
    DiseasesRecord findById(UUID id);
    List<DiseasesRecord>findAllByPondsId(UUID pondsId,int offset, int limit);

}
