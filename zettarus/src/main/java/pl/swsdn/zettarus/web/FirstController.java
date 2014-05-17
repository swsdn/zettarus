package pl.swsdn.zettarus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.swsdn.zettarus.model.UserUUID;

@Controller
public class FirstController {

	UserUUID userUUID;

	@Autowired
	public FirstController(UserUUID userUUID) {
		this.userUUID = userUUID;
	}

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("uuid", userUUID.getSessionValue());
		return "index";
	}
	
}
