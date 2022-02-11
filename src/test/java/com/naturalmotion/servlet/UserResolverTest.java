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
		        .findUserFromId("f94107af94d34a5e3c4280b6d58fd84109ab4209")).isEqualTo("73336037157");
	}

}
