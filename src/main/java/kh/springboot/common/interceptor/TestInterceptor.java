package kh.springboot.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//디스패쳐서블릿이 컨트롤러를 호출하기 전에 수행
		System.out.println("====================START====================");
		System.out.println(request.getRequestURI()+"\n");
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//컨트롤러에서 디스페쳐서브릸으로 리턴되는 순간에 진행
		//HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
		System.out.println("--------------------view--------------------");
		System.out.println(request.getRequestURI());
		if(modelAndView != null) {
			System.out.println(modelAndView.getModel());
			System.out.println(modelAndView.getViewName()+"\n");
		}
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//뷰에서 모든 작업이 완료된 후에 수행
		//HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
		System.out.println("~~~~~~~~~~~~~~~~~~~~END~~~~~~~~~~~~~~~~~~~~");
		System.out.println(request.getRequestURI()+"\n");
	}
	
	
}
