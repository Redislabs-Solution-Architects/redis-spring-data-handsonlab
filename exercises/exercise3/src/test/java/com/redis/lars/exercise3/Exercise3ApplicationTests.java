package com.redis.lars.exercise3;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = { Exercise3ApplicationTests.Initializer.class })
class Exercise3ApplicationTests {

	static final String REDIS_IMAGE = "redislabs/redismod:latest";
	static final int REDIS_PORT = 6379;

	@ClassRule
	@SuppressWarnings("rawtypes")
	public static GenericContainer redisContainer = new GenericContainer(DockerImageName.parse(REDIS_IMAGE))
			.withExposedPorts(REDIS_PORT);

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

			redisContainer.start();

			TestPropertyValues
					.of("spring.redis.host=" + redisContainer.getHost(),
							"spring.redis.port=" + redisContainer.getFirstMappedPort())
					.applyTo(configurableApplicationContext.getEnvironment());
		}
	}

	@Test
	void contextLoads() {
	}

}
