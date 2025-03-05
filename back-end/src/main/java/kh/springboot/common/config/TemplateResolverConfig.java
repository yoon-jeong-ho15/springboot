package kh.springboot.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
@Configuration
public class TemplateResolverConfig {
	
	@Bean
	public ClassLoaderTemplateResolver memberResolver() {
		ClassLoaderTemplateResolver mResolver = new ClassLoaderTemplateResolver();
		mResolver.setPrefix("templates/views/member");
		mResolver.setSuffix(".html");
		mResolver.setTemplateMode(TemplateMode.HTML);
		mResolver.setCharacterEncoding("UTF-8");
		mResolver.setCacheable(false);
		mResolver.setCheckExistence(true);
		return mResolver;
	}
	
	@Bean
	public ClassLoaderTemplateResolver boardResolver() {
		ClassLoaderTemplateResolver bResolver = new ClassLoaderTemplateResolver();
		bResolver.setPrefix("templates/views/board");
		bResolver.setSuffix(".html");
		bResolver.setTemplateMode(TemplateMode.HTML);
		bResolver.setCharacterEncoding("UTF-8");
		bResolver.setCacheable(false);
		bResolver.setCheckExistence(true);
		return bResolver;
	}
	
	@Bean
	public ClassLoaderTemplateResolver adminResolver() {
		ClassLoaderTemplateResolver aResolver = new ClassLoaderTemplateResolver();
		aResolver.setPrefix("templates/views/admin");
		aResolver.setSuffix(".html");
		aResolver.setTemplateMode(TemplateMode.HTML);
		aResolver.setCharacterEncoding("UTF-8");
		aResolver.setCacheable(false);
		aResolver.setCheckExistence(true);
		return aResolver;
	}
}
