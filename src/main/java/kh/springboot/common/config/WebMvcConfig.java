package kh.springboot.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kh.springboot.common.interceptor.CheckAdminInterceptor;
import kh.springboot.common.interceptor.CheckLoginInterceptor;
import kh.springboot.common.interceptor.LoginInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**") //매핑url 설정 (파일을 가지고 올 때의 경로를 설정)
			.addResourceLocations("file:///c:/uploadFiles/"
					, "classpath:/static/"); // 정적 리소스 위치
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CheckLoginInterceptor())
			.addPathPatterns("/member/myInfo",
					"/member/delete", "/member/edit",
					"/member/updatePassword" )
			.addPathPatterns("/board/**", "/attm/**")
			.excludePathPatterns("/board/list", "/attm/list",
					"/board/top"); //인터셉터가 가로챌 url 등록
		registry.addInterceptor(new CheckAdminInterceptor())
			.addPathPatterns("/admin/**");
		registry.addInterceptor(new LoginInterceptor())
			.addPathPatterns("/member/signIn");
	}
}
