package pl.swsdn.zettarus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FirstController {

	public FirstController() {
	}

	@RequestMapping("/")
	public String index(Model model) {
		return "index";
	}

}
