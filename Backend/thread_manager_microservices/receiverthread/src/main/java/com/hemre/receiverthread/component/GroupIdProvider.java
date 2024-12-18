package com.hemre.receiverthread.component;

import org.springframework.stereotype.Component;

@Component
public class GroupIdProvider {
    // Topic adını dinamik olarak sağlıyoruz (örneğin, zaman damgası ile)
    public String provideName() {
        return "threading2-" + System.currentTimeMillis();  // Dinamik bir topic adı
    }
}