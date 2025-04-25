package com.mymoneylog.server.repository.record;

import org.springframework.stereotype.Repository;
import java.util.Optional;

import com.mymoneylog.server.entity.category.QCategory;
import com.mymoneylog.server.entity.record.QRecord;
import com.mymoneylog.server.entity.user.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.mymoneylog.server.entity.record.Record;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecordRepositoryImpl implements RecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Record> findWithUserAndCategoryById(Long recordId) {
        QRecord record = QRecord.record;
        QUser user = QUser.user;
        QCategory category = QCategory.category;

        return Optional.ofNullable(queryFactory
                .selectFrom(record)
                .join(record.user, user).fetchJoin()
                .join(record.category, category).fetchJoin()
                .where(record.recordId.eq(recordId))
                .fetchOne());
    }
}
