package pl.swsdn.zettarus.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.swsdn.zettarus.model.Photos;
import pl.swsdn.zettarus.model.Photos.PhotoEntry;

@Controller
public class FirstController {

	private Photos photos;
	private int pageSize = 100;

	@Autowired
	public FirstController(Photos photos) {
		this.photos = photos;
	}

	@RequestMapping("/")
	public String index(Model model, @RequestParam(defaultValue = "0") int page) {
		List<PhotoEntry> pagedPhotos = photos.getPhotos().subList(getFrom(page, photos.getCount()),
				getTo(page, photos.getCount()));
		model.addAttribute("photos", pagedPhotos);
		return "index";
	}

	private int getFrom(int page, int count) {
		int from = page * pageSize;
		return (from > count - 1) ? count - 1 : from;
	}

	private int getTo(int page, int count) {
		int to = (page + 1) * pageSize;
		return to > count ? count : to;
	}

}
