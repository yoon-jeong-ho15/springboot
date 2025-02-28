package kh.springboot.admin.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kh.springboot.board.model.service.BoardService;
import kh.springboot.board.model.vo.Board;
import kh.springboot.member.model.service.MemberService;
import kh.springboot.member.model.vo.Member;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	private final BoardService bService;
	private final MemberService mService;
	
	@GetMapping("/home")
	public String moveToMainAdmin(Model model) {
		File f = new File("D:/logs/member/");
		File[] files = f.listFiles();
		TreeMap<String, Integer> dateCount = new TreeMap<>();
		BufferedReader br = null;
		
		try {
			for(File file:files) {
				System.out.println("log file : "+file);
				br = new BufferedReader(new FileReader(file));
				String data;
					while((data = br.readLine())!=null) {
						System.out.println("log lines : "+data);
						String date = data.split(" ")[0];
						//중복 검사 기능은 어떻게?
						//String user = data.split(" ")[마지막];
						//<날짜,유저>로 만들고
						//날짜마다 유저가 겹치면 +1 해서 위의 <날짜,횟수+1>
						//그러려면 밑의 if를 감싸는 조건문이 또 있어야함.
						
						if(dateCount.containsKey(date)) {
							dateCount.put(date,(dateCount.get(date) + 1));
						}else{
							dateCount.put(date, 1);
						}
					}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		ArrayList<Board> list = bService.selectRecentBoards();
		
		model.addAttribute("list", list);
		model.addAttribute("dateCount", dateCount);
		
		return "/admin";
	}
	
	@GetMapping("/members")
	   public String selectMembers(Model model) {
	      ArrayList<Member> list = mService.selectMembers();
	      model.addAttribute("list",list);
	      return "/members";
	   }
}
