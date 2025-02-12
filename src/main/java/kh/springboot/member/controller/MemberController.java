package kh.springboot.member.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import kh.springboot.member.model.exception.MemberException;
import kh.springboot.member.model.service.MemberService;
import kh.springboot.member.model.vo.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@SessionAttributes("loginUser")
@RequestMapping("/member")
@Slf4j
public class MemberController {
	
	private final MemberService mService;
	private final BCryptPasswordEncoder bcrypt;
	//private Logger log = LoggerFactory.getLogger(MemberController.class);
	
	@GetMapping("/signIn")
	public String signIn() {
		return "/login";
	}
	
//	@GetMapping("/member/logout")
//	public String logout(HttpSession session) {
//		session.removeAttribute("loginUser");
//		return "views/home";
//	}
	
	@GetMapping("/logout")
	public String logout(SessionStatus session) {
		session.setComplete();
		return "redirect:/home";
	}
	
//	@PostMapping("/member/signIn")
//	public void login(HttpServletRequest request) {
//		String id = request.getParameter("id");
//		String pwd = request.getParameter("pwd");
//		System.out.println(id+pwd);
//	}
	
//	@PostMapping("/member/signIn")
//	public void login(@RequestParam("id") String userId,
//			@RequestParam("pwd") String userPwd) {
//		String id = userId;
//		String pwd = userPwd;
//		System.out.println(id+pwd);
//	}
	
//	@PostMapping("/member/signIn")
//	public String login(@ModelAttribute Member m, HttpSession session) {
//		Member loginUser = mService.login(m);
//		if (loginUser != null && bcrypt.matches(m.getPwd(), loginUser.getPwd())) {
//			session.setAttribute("loginUser", loginUser);
//			return "redirect:/home";
//		}else {
//			throw new MemberException("로그인 실패");
//		}
//	}
	
	@PostMapping("/signIn")
	public String login(@ModelAttribute Member m, Model model,
			@RequestParam("beforeURL") String beforeURL) {
		Member loginUser = mService.login(m);
		if (loginUser != null && bcrypt.matches(m.getPwd(), loginUser.getPwd())) {
			model.addAttribute("loginUser", loginUser);
			if(loginUser.getIsAdmin().equals("Y")) {
				return "redirect:/admin/home";
			}else {
				//log.debug(loginUser.getId());
				return "redirect:"+beforeURL;
			}
		}else {
			throw new MemberException("로그인 실패");
		}
	}
	
	@GetMapping("/enroll")
	public String enroll() {
		//log.fatal("회원가입 페이지");
//		log.error("회원가입 페이지");
//		log.warn("회원가입 페이지");
//		log.info("회원가입 페이지");
//		log.debug("회원가입 페이지");
//		log.trace("회원가입 페이지");
		return "/enroll";
	}
	
	@PostMapping("/enroll")
	public String insertMember(@ModelAttribute Member m,
			@RequestParam("emailId") String emailId,
			@RequestParam("emailDomain") String emailDomain) {
		if(!emailId.trim().equals("")) {
			m.setEmail(emailId+"@"+emailDomain);
		}
		m.setPwd(bcrypt.encode(m.getPwd()));
		int result = mService.insertMember(m);
		if(result >0) {
			return "redirect:/home";
		}else {
			throw new MemberException("회원가입 실패");
		}
	}
	
//	@GetMapping("/member/myInfo")
//	public String myInfo(HttpSession session, Model model) {
//		Member loginUser = (Member) session.getAttribute("loginUser");
//		if(loginUser != null) {
//			String id = loginUser.getId();
//			ArrayList<HashMap<String, Object>> list = mService.selectMyList(id);
//			model.addAttribute("list", list);
//		}
//		return "views/member/myInfo";
//	}
	
	@GetMapping("/myInfo")
	public ModelAndView myInfo(HttpSession session, ModelAndView mv) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		if(loginUser != null) {
			String id = loginUser.getId();
			ArrayList<HashMap<String, Object>> list = mService.selectMyList(id);
			mv.addObject("list", list);
			mv.setViewName("/myInfo");
		}
		return mv;
	}
	
	@GetMapping("/edit")
	public String toEdit() {
		return "/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute Member m, 
			@RequestParam("emailId") String emailId,
			@RequestParam("emailDomain") String emailDomain,
			Model model, SessionStatus session) {
		if(!emailId.trim().equals("")) {
			m.setEmail(emailId+"@"+emailDomain);
		}
		System.out.println(m.getNickName());
		int result = mService.editMemberInfo(m);
		if (result>0) {
			model.addAttribute("loginUser", mService.login(m));
			return "redirect:/member/myInfo";
		}else {
			throw new MemberException("eidt 실패");
		}
	}
	
	@PostMapping("/updatePassword")
	public String updatePassword(@RequestParam("currentPwd") String current,
			@RequestParam("newPwd")String newp, 
			Model model) {
		Member loginUser = (Member)model.getAttribute("loginUser");
		if (loginUser != null && bcrypt.matches(current, loginUser.getPwd())) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", loginUser.getId());
			map.put("newPwd", bcrypt.encode(newp));
			int result = mService.updatePassword(map);
			
			if (result >0) {
				model.addAttribute("loginUser", mService.login(loginUser));
				return "redirect:/home";
			}else {
				throw new MemberException("failed");
			}
		}else {
			throw new MemberException("failed");
		}
	}
	
	@GetMapping("/delete")
	public String deleteMember(Model model, SessionStatus session) {
		Member loginUser = (Member)model.getAttribute("loginUser");
		int result = mService.deleteMember(loginUser.getId());
		if (result>0) {
			session.setComplete();
			return "redirect:/member/logout";
		}else {
			throw new MemberException("failed");
		}
	}
	
	@GetMapping("/checkValue")
	@ResponseBody
	public int checkValue(@RequestParam("col") String col,
			@RequestParam("value") String value) {
		HashMap<String, String>map = new HashMap<>();
		map.put("col", col);
		map.put("value",value);
		int count = mService.checkValue(map);
		return count;
	}
	
	@PostMapping("/profile")
	@ResponseBody
	public int updateProfile(@RequestParam(value="profile", required=false) MultipartFile profile,
			Model model) {
		//System.out.println(profile);
		int result = 0;
		String renameFileName = null;
		Member m = (Member) model.getAttribute("loginUser");
		
		String savePath = "c:\\profiles";
		File folder = new File(savePath);
		if(!folder.exists()) folder.mkdir();
		
		if(m.getProfile()!=null) {
			File f = new File(savePath+"\\"+m.getProfile());
			f.delete();
		}
		
		if(profile != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			int ranNum = (int)(Math.random()*100000);
			String originalFileName = profile.getOriginalFilename();
			renameFileName = sdf.format(
								new Date())+ranNum+
								originalFileName.substring(
								originalFileName.lastIndexOf(".")); 
			
			try {
				profile.transferTo(new File(folder+"\\"+renameFileName));
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		HashMap<String, String> map = new HashMap<>();
		map.put("id", m.getId());
		map.put("profile", renameFileName);
		
		result = mService.updateProfile(map);
		if(result == 1) {
			m.setProfile(renameFileName);
			model.addAttribute("loginUser", m);
		}
		
		return result;
	}
	
}
