package com.habuma.spitter.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import com.habuma.spitter.domain.Spitter;
import com.habuma.spitter.domain.Spittle;
import com.habuma.spitter.service.SpitterService;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.List;

import javax.inject.Inject;

@Controller
@RequestMapping("/spitters") // Корневой путь в URL
public class SpitterController {
	private final SpitterService spitterService;

	@Inject
	public SpitterController(SpitterService spitterService) {
		this.spitterService = spitterService;
	}

	// Обрабатывает GET-запросы к URL /spitter/spittles
	@RequestMapping(value = "/spittles", method = GET)
	public String listSpittlesForSpitter(@RequestParam("spitter") String username, Model model) {
		Spitter spitter = spitterService.getSpitter(username);
		System.out.println(spitter);
		model.addAttribute(spitter); // Заполнение модели
		List<Spittle> spittlesList = spitterService.getSpittlesForSpitter(username);
		System.out.println(spittlesList);
		model.addAttribute(spittlesList);

		return "spittles/list";
	}
}