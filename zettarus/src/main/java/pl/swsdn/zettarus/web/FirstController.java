package pl.swsdn.zettarus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.swsdn.zettarus.model.Photos;

@Controller
public class FirstController {

	private Photos photos;

	@Autowired
	public FirstController(Photos photos) {
		this.photos = photos;
	}

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("photos", photos.getPhotos());
		return "index";
	}

}
