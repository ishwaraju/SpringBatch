package com.ishwaraju.batch.user;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ishwaraju.entity.User;
import com.ishwaraju.repository.UserRepository;

@Component
public class UserWriter implements ItemWriter<User> {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void write(List<? extends User> items) throws Exception {
		System.out.println("Writing user data");
		userRepository.saveAll(items);
	}

}
