package com.atguigu.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.crm.dao.UserMapper;
import com.atguigu.crm.entity.User;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	public User login(String username, String password){
		User user = userMapper.getByName(username);
		
		if(user != null 
				&& user.getEnabled() == 1
				&& user.getPassword().equals(password)){
			return user;
		}
		
		return null;
	}
}
