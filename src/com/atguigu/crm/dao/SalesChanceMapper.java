package com.atguigu.crm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.atguigu.crm.entity.SalesChance;

public interface SalesChanceMapper {
	@Delete("delete from sales_chances where id = #{id}")
	void delete(@Param("id")long id);
	
	/*"UPDATE sales_chances SET contact = #{contact}, contact_tel = #{contactTel}, "
	+ "create_date = #{createDate}, cust_name = #{custName}, description = #{description}, rate = #{rate}, "
	+ "source = #{source}, title = #{title} "
	+ "WHERE id = #{id}"*/
	@Update("UPDATE sales_chances SET contact = #{contact}, contact_tel = #{contactTel}, "
			+ "create_date = #{createDate}, cust_name = #{custName}, description = #{description}, rate = #{rate}, "
			+ "source = #{source}, title = #{title} "
			+ "WHERE id = #{id}")
	void update(SalesChance chance);

	/*@Select("SELECT s.id, contact, contact_tel, create_date, cust_name, s.description, rate, source, "
			+ "status, title, u.name as \"createBy.name\", r.name as \"createBy.role.name\" "
			+ "FROM sales_chances s "
			+ "LEFT OUTER JOIN users u "
			+ "ON s.created_user_id = u.id "
			+ "LEFT OUTER JOIN roles r "
			+ "ON u.role_id = r.id "
			+ "WHERE s.id = #{id}")*/
	
	@Select("SELECT s.id, contact, contact_tel, create_date, cust_name, s.description, rate, source, "
			+ "status, title, u.name as \"createBy.name\", r.name as \"createBy.role.name\" "
			+ "FROM sales_chances s "
			+ "LEFT OUTER JOIN users u "
			+ "ON s.created_user_id = u.id "
			+ "LEFT OUTER JOIN roles r "
			+ "ON u.role_id = r.id "
			+ "WHERE s.id = #{id}")
	SalesChance get(@Param("id")long id);
	
	@Insert("INSERT INTO sales_chances(id, contact, contact_tel, create_date, cust_name, description, rate, source, "
			+ "status, title, created_user_id)"
			+ "VALUES(crm_seq.nextval, #{contact}, #{contactTel}, #{createDate}, #{custName}, #{description}, #{rate}, #{source}, "
			+ "#{status}, #{title}, #{createBy.id})")
	void save(SalesChance salesChance);
	
	
	List<SalesChance> getContent(Map<String, Object> params);
	
	long getTotalElements(Map<String,Object> map);
	
}


