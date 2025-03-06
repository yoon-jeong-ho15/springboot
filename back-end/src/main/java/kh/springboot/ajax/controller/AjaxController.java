package kh.springboot.ajax.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import kh.springboot.board.model.service.BoardService;
import kh.springboot.board.model.vo.Board;
import kh.springboot.board.model.vo.Reply;
import kh.springboot.member.model.service.MemberService;
import kh.springboot.member.model.vo.Member;
import kh.springboot.member.model.vo.TodoList;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/member", "/board", "/admin"})
@RequiredArgsConstructor
@SessionAttributes("loginUser")
public class AjaxController {
	private final MemberService mService;
	private final BoardService bService;
	private final JavaMailSender mailSender;
	
	@GetMapping("/checkValue")
	public int checkValue(@RequestParam("col") String col,
			@RequestParam("value") String val) {
		HashMap<String, String>map = new HashMap<>();
		map.put("col", col);
		map.put("val",val);
		int count = mService.checkValue(map);
		return count;
	}
	
	@PutMapping("/profile")
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
	
	@GetMapping("/echeck")
	public String checkEmail(@RequestParam("email") String email) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		
		String subject = "[SpringBoot] 이메일 확인";
		String body = "<h1 align = 'center'> SpringBoot 이메일 확인</h1><br>";
		body += "<div style='border: 3px solid green; text-align: center;"
				+ " font-size: 15px;'>본 메일은 이메일을 확인하기 위해 발송되었습니다.<br>";
		body += "아래 숫자를 인증번호 확인란에 작성하여 확인해주시기 바랍니다.<br><br>";
		
		String random="";
		for(int i =0; i<5; i++) {
			random += (int)(Math.random()*10);
		}
		body += "<span style='font-size: 30px; text-decoration: underline;'><b>"
				+random+"</b></span><br></div>";
		
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		try {
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(body, true);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mailSender.send(mimeMessage);
		
		return random;
	}
	
	@PostMapping("/list")
	public int insertTodo(@ModelAttribute TodoList todo) {
		return mService.insertTodo(todo);
	}
	
	@PutMapping("/list")
	public int updateTodo(@ModelAttribute TodoList todo) {
		return mService.updateTodo(todo);
	}
	
	@DeleteMapping("/list")
	public int deleteTodo(@RequestParam("todoNum") int todoNum) {
		return mService.deleteTodo(todoNum);
	}
	
	@GetMapping("/top")
	public ArrayList<Board> selectTop(HttpServletResponse response){
		ArrayList<Board> list =bService.selectTop();
		return list;
	}
	
	@PostMapping("/reply")
	public ArrayList<Reply> insertReply(@ModelAttribute Reply r) {
		int result = bService.insertReply(r);
		ArrayList<Reply> rlist = bService.selectReplyList(r.getRefBoardId());
		
//		ObjectMapper om = new ObjectMapper();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		om.setDateFormat(sdf);
//		String str = null;
//		try {
//			str = om.writeValueAsString(rlist);
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//		return str;
		return rlist;
	}
	
	@DeleteMapping("/reply")
	public int deleteReply(@RequestParam("replyId") int replyId) {
		return bService.deleteReply(replyId);
	}
	
	@PutMapping("/reply")
	public int updateReply(Reply r) {
		return bService.updateReply(r);
	}
	
	@PutMapping("/members")
	public int updateMember(@RequestBody HashMap<String, String> map) {
		System.out.println("requestbody : "+map);
		
		if(map.get("col").equals("NICKNAME")) {
			int count = mService.checkValue(map);
			System.out.println("checkValue : "+count);
			if(count != 0) {
				return -1;
			}
		} else if(map.get("col").equals("STATUS")||map.get("col").equals("ADMIN")) {
			map.put("col", map.get("col").equals("STATUS")? "member_status": "is_admin");
		}
		return mService.updateMemberItem(map);
	}
	
	@PutMapping("/status")
	public int updateBoardStatus(@RequestBody HashMap<String, Object> map) {
		System.out.println("request body : "+map);
		int result = bService.updateBoardStatus(map);
		System.out.println("result : "+result);
		return result;
	}

	@GetMapping("/weather")
	public String getWeather() {
		StringBuilder sb = new StringBuilder();
		try {
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
	        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=zHANegkooKdHDCkm9tsH9DdjJoFFHnMByKMlFNG5AiI6ABG%2Bmlx3Pw7NUYw%2B2VeZ17HD5FMOCVMIyqTgrkaz1A%3D%3D"); /*Service Key*/
	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
	        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
	        
	        //날짜 
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmm");
	        String now = sdf.format(new Date());
	        System.out.println("now : "+now);
	        String[] dayTime = now.split(" ");
	        System.out.println("dayTime[0] : "+dayTime[0]);
	        System.out.println("dayTime[1] : "+dayTime[1]);
	        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(dayTime[0], "UTF-8")); /*‘21년 6월 28일 발표*/
	        
	        //시간
	        int[] baseTime = {200, 500, 800, 1100, 1400, 1700, 2000, 2300};
	        int index = 99;
	        for(int i=0; i<baseTime.length;i++) {
	        	if(Integer.parseInt(dayTime[1])<= baseTime[i]) {
	        		index = i -1;
	        		if (i==0) {
	        			index = i;
	        		}
	        		break;
	        	}
	        }
	        if(index==99) {
	        	dayTime[1]="2300";
	        }
	        //0800 02300
	        dayTime[1] = ("0" + baseTime[index]).substring(("0" + baseTime[index]).length()-4);
	        System.out.println("dayTime[1] : "+dayTime[1]);
	        
	        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(dayTime[1], "UTF-8")); /*06시 발표(정시단위) */
	        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("58", "UTF-8")); /*예보지점의 X 좌표값*/
	        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("127", "UTF-8")); /*예보지점의 Y 좌표값*/
	        
	        URL url = (new URI(urlBuilder.toString())).toURL();
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-type", "application/json");
	        //System.out.println("Response code: " + conn.getResponseCode());
	        
	        BufferedReader rd;
	        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
	            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }
	        
	        String line;
	        while ((line = rd.readLine()) != null) {
	            sb.append(line);
	        }
	        rd.close();
	        conn.disconnect();
	        System.out.println(sb.toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
