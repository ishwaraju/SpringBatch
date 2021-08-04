package com.ishwaraju.batch.user;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.ishwaraju.entity.User;

@Component
public class UserFieldSetMapper implements FieldSetMapper<User> {

	@Override
	public User mapFieldSet(FieldSet fieldSet) throws BindException {
		final User user = new User();
		user.setName(fieldSet.readString("name"));
		user.setSalary(fieldSet.readInt("salary"));
		return user;
	}

}
