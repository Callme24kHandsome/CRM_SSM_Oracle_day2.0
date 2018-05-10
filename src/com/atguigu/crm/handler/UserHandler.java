package com.atguigu.crm.handler;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.atguigu.crm.entity.User;
import com.atguigu.crm.service.UserService;

//@SessionAttributes({"user"})
@RequestMapping("/user")
@Controller
public class UserHandler {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	/**
	 * 1. 如何把 Service 返回的结果放到 session 中呢 ? 使用 Map, 结合类上面标识的 @SessionAttributes 注解
	 * 注意: 若通过 redirect 返回结果, 则 Map 结合 @SessionAttributes 注解的方式不能使 key-value 放入到 session 中.
	 * 所以使用原生的 HttpSession!
	 * 2. 使用了 ResourceBundleMessageSource, 从国际化资源文件中来获取国际化消息. 且 hanlder 方法中传入了 Locale 对象.
	 * 3. 表单提交, 若出错, 最好使用重定向的方式返回表单页面, 好处是不会有表单重复提交的问题. 
	 * 4. SpringMVC 在重定向的情况下, 也可以向 Map 中放入键值对:
	 * 1). 使用的不是 Map, 而是 RedirectAttributes
	 * 2). 不能直接重定向到实际的物理页面(例如: redirect:/index.jsp), 必须重定向到 SpringMVC 映射过的一个页面. 
	 * 例如:
	 * return "redirect:/index";
	 * <mvc:view-controller path="index" view-name="index"/>
	 * 3). 原理: 把 key-value 放到 session 中, 从 session 中获取, 放入到 request 中, 再从 session 中移除. 
	 */
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(@RequestParam(value="username") String username, 
			@RequestParam(value="password") String password, Map<String, Object> map, 
			RedirectAttributes attributes, Locale locale, HttpSession session){
		User user = userService.login(username, password);
		
		if(user != null){
			session.setAttribute("user", user);
			return "redirect:/success";
			//map.put("user", user);
			//return "home/success";
		}
		
		String message = messageSource.getMessage("error.crm.user.login", null, locale);
		attributes.addFlashAttribute("message", message);
		attributes.addFlashAttribute("name", username);
		
		return "redirect:/index";
	}
	
}
