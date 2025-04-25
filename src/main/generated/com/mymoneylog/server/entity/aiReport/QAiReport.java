package com.mymoneylog.server.entity.aiReport;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAiReport is a Querydsl query type for AiReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAiReport extends EntityPathBase<AiReport> {

    private static final long serialVersionUID = 1879556509L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAiReport aiReport = new QAiReport("aiReport");

    public final NumberPath<Long> aiReportId = createNumber("aiReportId", Long.class);

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath month = createString("month");

    public final com.mymoneylog.server.entity.user.QUser user;

    public QAiReport(String variable) {
        this(AiReport.class, forVariable(variable), INITS);
    }

    public QAiReport(Path<? extends AiReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAiReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAiReport(PathMetadata metadata, PathInits inits) {
        this(AiReport.class, metadata, inits);
    }

    public QAiReport(Class<? extends AiReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.mymoneylog.server.entity.user.QUser(forProperty("user")) : null;
    }

}

