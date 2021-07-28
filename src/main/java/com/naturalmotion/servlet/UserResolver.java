package com.naturalmotion.servlet;

import com.naturalmotion.webservice.configuration.ConfigSession;

import csr.Checksum;

public class UserResolver {

	private ConfigSession configuration;

	public UserResolver(ConfigSession configuration) {
		this.configuration = configuration;
	}

	public String findUserFromId(String userId) {
		String resultUser = null;
		String list = configuration.getString("users.list");
		String[] split = list.split(",");
		Checksum checksum = new Checksum();
		for (String user : split) {
			String computeHmac = checksum.computeHmac(user);
			if (computeHmac.equals(userId)) {
				resultUser = user;
			}
		}
		return resultUser;
	}
}
