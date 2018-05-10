package com.atguigu.crm.orm;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class PropertyFilter {
	
	public static List<PropertyFilter> parseParamsToPropertyFilters(Map<String, Object> params){
		List<PropertyFilter> filters = new ArrayList<>();
		
		for(Map.Entry<String, Object> param: params.entrySet()){
			String key = param.getKey(); //EQI_age, GTD_birth
			Object propertyVal = param.getValue(); //20, 1990-12-12
			
			if(propertyVal == null || propertyVal.toString().trim().equals("")){
				continue;
			}
			
			String str = StringUtils.substringBefore(key, "_"); //EQI, GTD
			String matchCode = str.substring(0, str.length() - 1);//EQ, GT
			String typeCode = str.substring(str.length() - 1); //I, D
			
			String propertyName = StringUtils.substringAfter(key, "_");
			//Enum.valueOf 可以把一个字符串转为其对应的枚举类的对象
			MatchType matchType = Enum.valueOf(MatchType.class, matchCode);
			Class propertyType = Enum.valueOf(PropertyType.class, typeCode).getPropertyType();
			
			PropertyFilter filter = new PropertyFilter(propertyName, propertyVal, matchType, propertyType);
			filters.add(filter);
		}
		
		return filters;
	} 
	
	public static void main(String[] args) {
		String str = "EQ";
		MatchType matchType = Enum.valueOf(MatchType.class, str);
		System.out.println(matchType);
	}
	
	public PropertyFilter(String propertyName, Object propertyVal,
			MatchType matchType, Class propertyType) {
		this.propertyName = propertyName;
		this.propertyVal = propertyVal;
		this.matchType = matchType;
		this.propertyType = propertyType;
	}



	private String propertyName;
	private Object propertyVal;
	
	private MatchType matchType;
	private Class propertyType;
	
	public String getPropertyName() {
		return propertyName;
	}

	public Object getPropertyVal() {
		return propertyVal;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public Class getPropertyType() {
		return propertyType;
	}

	public enum MatchType{
		EQ, LT, LE, GT, GE, LIKE;
	}
	
	enum PropertyType{
		I(Integer.class), D(Date.class), F(Float.class), S(String.class), B(Boolean.class);
		
		private Class propertyType;
		
		private PropertyType(Class propertyType){
			this.propertyType = propertyType;
		}
		
		public Class getPropertyType() {
			return propertyType;
		}
	}
	
}
