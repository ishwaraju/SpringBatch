package com.ishwaraju.batch.user;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.ishwaraju.entity.User;

@Component
public class UserProcessor implements ItemProcessor<User, User>{

	@Override
	public User process(User item) throws Exception {
		System.out.println("User detail.."+item.getName());
		return item;
	}

}
