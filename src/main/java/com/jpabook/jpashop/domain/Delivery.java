package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.util.Address;
import com.jpabook.jpashop.util.DeliveryStatus;
import jakarta.persistence.*;

@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Orders orders;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    public void setOrders(Orders orders) {
        this.orders = orders;
    }
}
