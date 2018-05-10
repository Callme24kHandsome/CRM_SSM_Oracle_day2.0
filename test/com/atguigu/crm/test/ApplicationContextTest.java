package com.atguigu.crm.test;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.atguigu.crm.dao.UserMapper;
import com.atguigu.crm.entity.User;

public class ApplicationContextTest {
	
	private ApplicationContext ctx = null;
	private UserMapper userMapper;
	
	{
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		userMapper = ctx.getBean(UserMapper.class);
	}
	
	@Test
	public void testUserMapper(){
		User user = userMapper.getByName("bcde");
		
		System.out.println(user.getPassword());
		System.out.println(user.getRole().getName());
	}
	
	@Test
	public void testDataSource() throws SQLException {
		DataSource dataSource = ctx.getBean(DataSource.class);
		System.out.println(dataSource.getConnection());
	}

}
