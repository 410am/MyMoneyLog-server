package com.mymoneylog.server.repository.user;

import com.mymoneylog.server.entity.user.User;
import com.mymoneylog.server.entity.user.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    

    private final JPAQueryFactory queryFactory;
    QUser user = QUser.user;

    @Override
    public User findUserById(Long id) {
        return queryFactory
                .selectFrom(user)
                .where(user.userId.eq(id)) 
                .fetchOne();
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        queryFactory
                .delete(user)
                .where(user.userId.eq(id)) 
                .execute();
    }
}
