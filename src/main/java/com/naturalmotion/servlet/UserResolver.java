package com.naturalmotion.servlet;

import java.util.Map;

import com.naturalmotion.webservice.configuration.ConfigSession;
import com.naturalmotion.webservice.configuration.json.Auth;

import csr.Checksum;

public class UserResolver {

	private ConfigSession configuration;

	public UserResolver(ConfigSession configuration) {
		this.configuration = configuration;
	}

	public String findUserFromId(String userId) {
		String resultUser = null;
		Map<String, Auth> list = configuration.get();
		Checksum checksum = new Checksum();
		for (String user : list.keySet()) {
			String computeHmac = checksum.computeHmac(list.get(user).getName());
			if (computeHmac.equals(userId)) {
				resultUser = user;
			}
		}
		return resultUser;
	}
}
