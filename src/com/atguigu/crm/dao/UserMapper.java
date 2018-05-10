package com.atguigu.crm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.atguigu.crm.entity.SalesChance;
import com.atguigu.crm.entity.User;

public interface UserMapper {

	@Select("SELECT u.id, u.enabled, u.name, password, salt, r.name as \"role.name\" "
			+ "FROM users u "
			+ "LEFT OUTER JOIN roles r "
			+ "ON u.role_id = r.id "
			+ "WHERE u.name = #{name}")
	User getByName(@Param("name") String name);
	
}
