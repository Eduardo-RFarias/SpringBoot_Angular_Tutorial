package edu.unb.reddit.service;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailContentBuilder {
	private final TemplateEngine templateEngine;

	String build(String message, String link) {
		var context = new Context();

		context.setVariable("message", message);
		context.setVariable("link", link);

		return templateEngine.process("mailTemplate", context);
	}
}
