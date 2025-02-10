package kh.springboot.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kh.springboot.member.model.vo.Member;

public class CheckAdminInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		Member loginUser = (Member) session.getAttribute("loginUser");
		if (loginUser == null) {
			CheckLoginInterceptor l = new CheckLoginInterceptor();
			l.preHandle(request, response, handler);
		}else {
			if(!loginUser.getIsAdmin().equals("Y")) {
				String msg = "관리자만 접근 가능합니다.";
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().write("<script>alert('"+msg+"');"+
						"location.href='/home';</script>");
				return false;
			}
		}
	
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
