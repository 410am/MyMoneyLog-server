// package com.mymoneylog.server.repository.record;


// import com.mymoneylog.server.entity.record.Record;

// import java.util.List;

// import org.springframework.data.jpa.repository.JpaRepository;

// public interface RecordRepository extends JpaRepository<Record, Long>,RecordRepositoryCustom {
//     List<Record> findByUserUserId(Long userId);
//     List<Record> findByCategoryCategoryId(Long categoryId);
// }

package com.mymoneylog.server.repository.record;

import com.mymoneylog.server.entity.record.Record;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecordRepository extends
        JpaRepository<Record, Long>,
        JpaSpecificationExecutor<Record>,
        RecordRepositoryCustom {

    List<Record> findByUserUserId(Long userId);
    List<Record> findByCategoryCategoryId(Long categoryId);
}
