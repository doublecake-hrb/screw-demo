package com.screw;

import com.screw.function.DocumentGeneration;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class ScrewApplicationTests {

    @Autowired
    DocumentGeneration documentGeneration;

    @Test
    void contextLoads() {
        documentGeneration.documentGeneration();
    }


}
