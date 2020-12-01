package com.naturalmotion.token;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TokenManagerTest {

	private TokenManager tokenManager = new TokenManager();

	@Test
	public void testCreate() throws Exception {
		String token = tokenManager.create("test");
		TokenUser tokenUser = tokenManager.from(token);
		System.out.println(token);
		Assertions.assertThat(tokenUser.getDir()).isNotNull();
		Assertions.assertThat(tokenUser.getUser()).isEqualTo("test");
	}

}
