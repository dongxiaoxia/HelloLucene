package xyz.dongxiaoxia.hellolucene;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class HelloController {
	@RequestMapping(value = "hello",method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}

	@RequestMapping( method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "index";
	}

	@RequestMapping(value = "main",method = RequestMethod.GET)
	public String search(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "main";
	}
}