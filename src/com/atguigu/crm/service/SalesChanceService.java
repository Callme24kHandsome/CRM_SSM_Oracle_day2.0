package com.atguigu.crm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.atguigu.crm.dao.SalesChanceMapper;
import com.atguigu.crm.entity.SalesChance;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.orm.PropertyFilter;
import com.atguigu.crm.orm.PropertyFilter.MatchType;
import com.atguigu.crm.utils.ReflectionUtils;

@Service
public class SalesChanceService {

	@Autowired
	private SalesChanceMapper salesChanceMapper;
	@Transactional
	public SalesChance get(long id) {
		return salesChanceMapper.get(id);
	}
	@Transactional
	public void update(SalesChance chance) {
		salesChanceMapper.update(chance); 
	}
	@Transactional
	public void delete(long id) {
		salesChanceMapper.delete(id);
	}
	@Transactional
	public void save(SalesChance chance) {
		chance.setCreateDate(new Date());
		chance.setStatus(1);
		salesChanceMapper.save(chance);
	}
	@Transactional(readOnly=true)
	public Page<SalesChance> getPage(Page<SalesChance> page,Map<String, Object> params2){
		
		//1、把传入的请求参数map转化为PropertyFilter集合
		List<PropertyFilter> propertyFilters = PropertyFilter.parseParamsToPropertyFilters(params2);
		//2、把propertyFilters转化为mybatis可用的分map
		Map<String,Object> map = parsePropertyFilters2MybatisParams(propertyFilters);
		//加入额外的查询条件
		map.put("status",1);
		//3. 再调用 myBatis 方法时, 传入 Map<String, Object> 类型的参数
		long totalElements = salesChanceMapper.getTotalElements(map);
		
		System.out.println(map);
		
		int fromIndex = (page.getPageNo() - 1) * page.getPageSize() + 1;
		int endIndex = fromIndex + page.getPageSize();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fromIndex", fromIndex);
		params.put("endIndex", endIndex);
		//4.加入分页的参数
		map.putAll(params);
		List<SalesChance> content = salesChanceMapper.getContent(map);

		System.out.println(totalElements);
		
		page.setTotalElements((int)totalElements);
		page.setContent(content);
		
		return page;
	}
	/**
	 * 把propertyFilters转化为mybatis可用的分map
	 * @param propertyFilters
	 * @return
	 */
	private Map<String, Object> parsePropertyFilters2MybatisParams(List<PropertyFilter> filters) {
		Map<String,Object> map = new HashMap<String,Object>();
		if(filters == null || filters.size() == 0) {
			return map;
		}
	
		for (PropertyFilter filter : filters) {
			
			String name = filter.getPropertyName();
			Object val = filter.getPropertyVal();
			Class propertyType = filter.getPropertyType();
			System.out.println(propertyType);
			MatchType matchType = filter.getMatchType();
			//1、把val转化为实际类型
			
			val = ReflectionUtils.convertValue(val, propertyType);
			System.out.println(val);
			if(matchType.equals(MatchType.LIKE)) {
				val = "%"+val+"%";
			}
			System.out.println(val);
			map.put(name, val);
		}
		
		return map;
	}
}
