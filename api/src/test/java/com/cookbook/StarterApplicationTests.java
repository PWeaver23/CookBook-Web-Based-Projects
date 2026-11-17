package com.cookbook;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// The test recipes are turned off here so this test can run without a MongoDB server.
@SpringBootTest(properties = "cookbook.seed-test-recipes=false")
class StarterApplicationTests {

	@Test
	void contextLoads() {
	}

}
