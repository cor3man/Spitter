package com.habuma.spitter.mvc;

import javax.inject.Inject;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.habuma.spitter.service.SpitterService;

@Controller // Объявить как контроллер
public class HomeController {
	public static final int DEFAULT_SPITTLES_PER_PAGE = 25;
	private SpitterService spitterService;

	@Inject // Внедрить SpitterService
	public HomeController(SpitterService spitterService) {
		this.spitterService = spitterService;
	}

	@RequestMapping({ "/", "home/" }) // Обрабатывать запросы на получение
	// главной страницы
	public String showHomePage(Map<String, Object> model) {
		model.put("spittles", spitterService.getRecentSpittles(DEFAULT_SPITTLES_PER_PAGE)); // Добавить
																							// сообщения
																							// в
																							// модель
		return "home"; // Вернуть имя представления
	}
}