package com.mymoneylog.server.repository.record;

import java.util.Optional;
import com.mymoneylog.server.entity.record.Record;

public interface RecordRepositoryCustom {
    Optional<Record> findWithUserAndCategoryById(Long recordId);
}
