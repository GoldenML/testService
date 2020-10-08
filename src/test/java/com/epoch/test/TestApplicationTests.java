package com.epoch.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class TestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void test1 () {
        String sql = "dasda/sdsadasdasdadsaa/dsadad.xad";
        System.out.println(sql.substring(sql.lastIndexOf("/") + 1));
    }
}
