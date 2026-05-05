package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Orders;
import com.jpabook.jpashop.domain.QMember;
import com.jpabook.jpashop.domain.QOrders;
import com.jpabook.jpashop.util.OrderStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public void save(Orders order) {
        em.persist(order);
    }

    public Orders findOne(Long id) {
        return em.find(Orders.class, id);
    }

    public List<Orders> findAll(OrderSearch orderSearch) {
        QOrders o = QOrders.orders;
        QMember m = QMember.member;

        return queryFactory
                .select(o)
                .from(o)
                .join(o.member, m)
                .where(
                        statusEq(o, orderSearch.getOrderStatus()),
                        nameLike(m, orderSearch.getMemberName())
                )
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEq(QOrders o, OrderStatus status) {
        return status != null ? o.orderStatus.eq(status) : null;
    }

    private BooleanExpression nameLike(QMember m, String name) {
        return StringUtils.hasText(name) ? m.name.like("%" + name + "%") : null;
    }
}
