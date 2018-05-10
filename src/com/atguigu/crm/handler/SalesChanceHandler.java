package com.atguigu.crm.handler;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.atguigu.crm.entity.SalesChance;
import com.atguigu.crm.entity.User;
import com.atguigu.crm.orm.Page;
import com.atguigu.crm.service.SalesChanceService;

@Controller
@RequestMapping("/chance")
public class SalesChanceHandler {

	@Autowired
	private SalesChanceService salesChanceService;
	@RequestMapping(value="/{id}",method=RequestMethod.DELETE)
	public String delete(@PathVariable("id")long id) {
		salesChanceService.delete(id);
		return "redirect:/chance/list";
	}
	@RequestMapping("/{id}")
	public String edit(@PathVariable("id")long id,Map<String,Object> map) {
		SalesChance chance = salesChanceService.get(id);
		map.put("chance", chance);
		return "chance/input";
	}
	
	@RequestMapping(value="/{id}",method=RequestMethod.PUT)
	public String update(SalesChance chance) {
		salesChanceService.update(chance);
		return "redirect:/chance/list";
	}
	
	@RequestMapping(value="/",method=RequestMethod.POST)
	public String save(SalesChance chance,RedirectAttributes attributes,HttpSession session) {
		User user = (User) session.getAttribute("user");	
		chance.setCreateBy(user);
		chance.setStatus(1);
		salesChanceService.save(chance);
		attributes.addFlashAttribute("message", "添加成功");
		return "redirect:/chance/list";
	}
	
	/*@RequestMapping(value="/list")
	public String list(@RequestParam(value="pageNo",required=false,defaultValue="1") String pageNoStr,
			@RequestParam(value="pageSize",required=false,defaultValue="4") String pageSizeStr, 
			Map<String, Object> map){
		int pageSize = 4;
		Page<SalesChance> page = new Page<>();
		page.setPageNo(pageNoStr);
		try {
			pageSize = Integer.parseInt(pageSizeStr);
			page.setPageSize(pageSize);
		} catch (NumberFormatException e) {}
		
		page = salesChanceService.getPage(page);
		map.put("page", page);
		
		return "chance/list";
	}
	*/
	@RequestMapping(value="/list")
	public String list2(@RequestParam(value="pageNo",required=false,defaultValue="1") String pageNoStr,
			@RequestParam(value="pageSize",required=false,defaultValue="4") String pageSizeStr, 
			HttpServletRequest request,
			Map<String, Object> map){
		//1. 获取 search_ 开头的请求参数,去前缀search_
		Map<String, Object> params = WebUtils.getParametersStartingWith(request, "search_");
		
		System.out.println(params);
		
		int pageSize = 4;
		Page<SalesChance> page = new Page<>();
		page.setPageNo(pageNoStr);
		try {
			pageSize = Integer.parseInt(pageSizeStr);
			page.setPageSize(pageSize);
		} catch (NumberFormatException e) {}
		//2、把带查询条件的请求参数params传入service中
		page = salesChanceService.getPage(page,params);
		//3、把params序列化为一个查询字符串，加前缀
		String pathString = encodeParameterStringWithPrefix(params, "search_");
		//4、把字符串传回页面上
		map.put("page", page);
		map.put("pathString", pathString);
		return "chance/list";
	}
	/**
	 * 对一个map对象进行加前缀且转化为path字符创
	 * @param params
	 * @param prefix
	 * @return
	 */
	public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
		if ((params == null) || (params.size() == 0)) {
			return "";
		}

		if (prefix == null) {
			prefix = "";
		}

		StringBuilder queryStringBuilder = new StringBuilder();
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			queryStringBuilder.append(prefix).append(entry.getKey()).append('=').append(entry.getValue());
			if (it.hasNext()) {
				queryStringBuilder.append('&');
			}
		}
		return queryStringBuilder.toString();
	}
	
	@RequestMapping("/")
	public String input(Map<String,Object> map) {
		map.put("chance", new SalesChance());
		return "chance/input";
	}
	
	
}
