package com.ishwaraju.config;

import org.springframework.batch.item.ItemProcessor;

import com.ishwaraju.entity.User;

public class UserProcessor implements ItemProcessor<User, User> {

	@Override
	public User process(User item) throws Exception {
		final String name= item.getName();
		final Integer salary = item.getSalary();
		final User processedUser = new User();
		processedUser.setName(name);
		processedUser.setSalary(salary);
		System.out.println("++++++++"+processedUser.getName());
		return processedUser;
	}

}
