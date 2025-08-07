package com.mymoneylog.server.entity.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 469914365L;

    public static final QUser user = new QUser("user");

    public final ListPath<com.mymoneylog.server.entity.aiReport.AiReport, com.mymoneylog.server.entity.aiReport.QAiReport> aiReports = this.<com.mymoneylog.server.entity.aiReport.AiReport, com.mymoneylog.server.entity.aiReport.QAiReport>createList("aiReports", com.mymoneylog.server.entity.aiReport.AiReport.class, com.mymoneylog.server.entity.aiReport.QAiReport.class, PathInits.DIRECT2);

    public final ListPath<com.mymoneylog.server.entity.category.Category, com.mymoneylog.server.entity.category.QCategory> categories = this.<com.mymoneylog.server.entity.category.Category, com.mymoneylog.server.entity.category.QCategory>createList("categories", com.mymoneylog.server.entity.category.Category.class, com.mymoneylog.server.entity.category.QCategory.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath providerId = createString("providerId");

    public final ListPath<com.mymoneylog.server.entity.record.Record, com.mymoneylog.server.entity.record.QRecord> records = this.<com.mymoneylog.server.entity.record.Record, com.mymoneylog.server.entity.record.QRecord>createList("records", com.mymoneylog.server.entity.record.Record.class, com.mymoneylog.server.entity.record.QRecord.class, PathInits.DIRECT2);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

