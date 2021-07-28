package com.naturalmotion.servlet;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.naturalmotion.webservice.configuration.ConfigSession;
import com.naturalmotion.webservice.configuration.Configuration;

public class UserResolverTest {

	private Configuration configuration = new Configuration();

	@Test
	public void testFindUserFromId() throws Exception {
		Assertions.assertThat(new UserResolver(new ConfigSession(configuration))
				.findUserFromId("e62e32fa4c0292375d2107e5256e51bc051416ca")).isEqualTo("buzz");
	}

}
