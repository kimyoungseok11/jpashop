package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Orders;
import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.exception.NotEnoughStockException;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.util.Address;
import com.jpabook.jpashop.util.OrderStatus;
import jakarta.persistence.EntityManager;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        Member member = getMember("회원1");
        Book book = getBook("시골 jpa", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Orders orders = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, orders.getOrderStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, orders.getOrderItems().size(), "주문한 상품 종류 수가 정확해야한다");
        assertEquals(orderCount * 10000, orders.getTotalPrice(), "주문 가격은 가격 * 수량이다");
        assertEquals(8, book.getStockQuantity(), "주문한 수량 만큼 재고가 줄어야한다");
    }

    @Test
    public void 상품주문취소() throws Exception {
        Member member = getMember("회원1");
        Item item = getBook("시골 jpa", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        Orders orders = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, orders.getOrderStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, orders.getOrderItems().size(), "주문한 상품 종류 수가 정확해야한다");
        assertEquals(orderCount * 10000, orders.getTotalPrice(), "주문 가격은 가격 * 수량이다");
        assertEquals(8, item.getStockQuantity(), "주문한 수량 만큼 재고가 줄어야한다");

        orderService.cancelOrder(orders.getId());
        assertEquals(OrderStatus.CANCEL, orders.getOrderStatus(), "상품 주문 취소시 CANCEL");
        assertEquals(10, item.getStockQuantity(), "주문한 수량 만큼 재고가 줄어야한다");


    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        Member member = getMember("회원1");
        Item item = getBook("시골 jpa", 10000, 10);

        int orderCount = 11;

        assertThrows(NotEnoughStockException.class, () ->
                orderService.order(member.getId(), item.getId(), orderCount));
    }

    private @NonNull Book getBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private @NonNull Member getMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);
        return member;
    }
}