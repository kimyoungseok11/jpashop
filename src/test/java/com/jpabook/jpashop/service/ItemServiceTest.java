package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.repository.ItemRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;
    @Autowired EntityManager em;

    @Test
    public void 상품등록() throws Exception {
        //given
        Book book = new Book();
        book.setName("책1");
        book.setPrice(10000);
        book.setStockQuantity(100);

        //when
        itemService.save(book);
        em.flush();
        em.clear();

        //then
        Item findItem = itemRepository.findOne(book.getId());
        assertNotNull(findItem);
        assertEquals(book.getId(), findItem.getId());
        assertEquals("책1", findItem.getName());
        assertEquals(10000, findItem.getPrice());
    }

    @Test
    public void 상품전체조회() throws Exception {
        //given
        int beforeCount = itemRepository.findAll().size();

        Book book1 = new Book();
        book1.setName("책1");

        Book book2 = new Book();
        book2.setName("책2");

        Book book3 = new Book();
        book3.setName("책3");

        //when
        itemService.save(book1);
        itemService.save(book2);
        itemService.save(book3);
        em.flush();
        em.clear();

        //then
        List<Item> items = itemRepository.findAll();
        assertEquals(beforeCount + 3, items.size());
    }

    @Test
    public void 상품_한개만_조회() throws Exception {
        //given
        Book book1 = new Book();
        book1.setName("책1");

        Book book2 = new Book();
        book2.setName("책2");

        Book book3 = new Book();
        book3.setName("책3");

        itemService.save(book1);
        itemService.save(book2);
        itemService.save(book3);
        em.flush();
        em.clear();

        //when
        Item findItem = itemRepository.findOne(book2.getId());

        //then
        assertNotNull(findItem);
        assertEquals(book2.getId(), findItem.getId());
        assertEquals("책2", findItem.getName());
    }
}