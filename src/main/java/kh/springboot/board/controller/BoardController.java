package kh.springboot.board.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kh.springboot.board.model.exception.BoardException;
import kh.springboot.board.model.service.BoardService;
import kh.springboot.board.model.vo.Board;
import kh.springboot.board.model.vo.PageInfo;
import kh.springboot.board.model.vo.Reply;
import kh.springboot.common.Pagination;
import kh.springboot.member.model.vo.Member;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService bService;
	
	@GetMapping("/list")
	public String selectList(@RequestParam(value="page",
			defaultValue="1") int currentPage
			, Model model, HttpServletRequest request) {
		int listCount = bService.getListCount(1);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5);
		
		ArrayList<Board> list = bService.selectBoardList(pi, 1);
		
		model.addAttribute("list", list).addAttribute("pi", pi);
		model.addAttribute("loc", request.getRequestURI());
		return "/list";
	}
	
	@GetMapping("/write")
	public String toWrite() {
		return "/write(board)";
	}
	
	@PostMapping("/insert")
	public String insert(@ModelAttribute("Board") Board b, HttpSession session
			) {
		int result = 0;
		Member loginUser = (Member) session.getAttribute("loginUser");
		b.setBoardType(1);
		b.setBoardWriter(loginUser.getId());
		result = bService.insertBoard(b);
		if(result >0 ) {
			return "redirect:/board/list";
		}else {
			throw new BoardException("failed");
		}
	}
	
	@GetMapping("/{id}/{page}")
	public String selectBoard(@PathVariable("id") int bId,
			@PathVariable("page") int page,
			HttpSession session, Model model) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		String login = "";
		if (loginUser != null) {
			login = loginUser.getId();
		}
		Board b = bService.selectBoard(bId, login);
		ArrayList<Reply> rlist = bService.selectReplyList(bId);
		
		if (b != null) {
			model.addAttribute("b",b);
			model.addAttribute("page", page);
			model.addAttribute("rlist", rlist);
			return "/detail";
		}else {
			throw new BoardException("not found");
		}
	}
	
	@PostMapping("/updForm")
	public String toEdit(@RequestParam("boardId") int bId,
			@RequestParam("page") int page,
			Model model) {
		Board b = bService.selectBoard(bId, null);
		model.addAttribute("b", b);
		model.addAttribute("page", page);
		return "views/board/edit";
	}
	
	@PostMapping("/update")
	public String updateBoard(@ModelAttribute Board b,
			@RequestParam("page") int page) {
		b.setBoardType(1);
		int result = bService.updateBoard(b);
		if (result>0) {
			//return "redirect:"+b.getBoardId()+"/"+page;
			return String.format("redirect:/board/%d/%d",
					b.getBoardId(), page);
		}else {
			throw new BoardException("update failed");
		}
	}
	
	@PostMapping("/delete")
	public String deleteBoard(@ModelAttribute Board b,
			@RequestParam("page") int page,
			HttpServletRequest request) {
		int result = bService.deleteBoard(b.getBoardId());
		if (result>0) {
			//"redirect:/list?page="+page; 는 왜 안되고
			//return "redirect:/"+(request.getRequestURI().contains("board")?"board":"attm")+"/list";
			return "redirect:/"+(request.getHeader("referer").contains("board")?
					"board":"attm")+"/list";
		}else {
			throw new BoardException("delete failed");
		}
	}
	
//	@GetMapping(value = "/top", produces="application/json; charset=UTF-8")
//	@ResponseBody
//	public String selectTop(HttpServletResponse response) {
//		ArrayList<Board> list = bService.selectTop();
//		System.out.println(list);
//		
//		JSONArray array = new JSONArray();
//		for(Board b : list) {
//			JSONObject json = new JSONObject();
//			json.put("boardId", b.getBoardId());
//			json.put("boardTitle", b.getBoardTitle());
//			json.put("nickName", b.getNickName());
//			json.put("modifyDate", b.getModifyDate());
//			json.put("boardCount", b.getBoardCount());
//			
//			array.put(json);
//		}
////		response.setContentType("application/json; charset=UTF-8");
//		return array.toString();
//	}
	
	@GetMapping("/top")
	public void selectTop(HttpServletResponse response){
		ArrayList<Board> list =bService.selectTop();
		response.setContentType("application/json; charset=UTF-8");

		GsonBuilder gb = new GsonBuilder();
		GsonBuilder dfgb = gb.setDateFormat("yyyy-MM-dd");
		Gson gson = dfgb.create();
		try {
			gson.toJson(list, response.getWriter());
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
	}
	
//	@GetMapping("rinsert")
//	@ResponseBody
//	public String insertReply(@ModelAttribute Reply r,
//			HttpServletResponse response) {
//		int result = bService.insertReply(r);
//		ArrayList<Reply> rlist = bService.selectReplyList(r.getRefBoardId());
//		
//		response.setContentType("application/json; charset=UTF-8");
//		
//		JSONArray jArr = new JSONArray();
//		for(Reply re : rlist) {
//			JSONObject json = new JSONObject();
//			json.put("replyContent", re.getReplyContent());
//			json.put("nickName", re.getNickName());
//			json.put("replyModifyDate", re.getReplyModifyDate());
//			jArr.put(json);
//		}
//		return jArr.toString();
//	}
	
//	@GetMapping("/rinsert")
//	public void insertReply(@ModelAttribute Reply r,
//			HttpServletResponse response) {
//		response.setContentType("application/json; charset=UTF-8");
//		
//		int result = bService.insertReply(r);
//		ArrayList<Reply> rlist = bService.selectReplyList(r.getRefBoardId());
//		
//		GsonBuilder gb = new GsonBuilder().setDateFormat("yyyy-MM-dd");
//		Gson gson = gb.create();
//		try {
//			gson.toJson(rlist, response.getWriter());
//		} catch (JsonIOException | IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	@GetMapping("/rinsert")
	@ResponseBody
	public String insertReply(@ModelAttribute Reply r) {
		int result = bService.insertReply(r);
		ArrayList<Reply> rlist = bService.selectReplyList(r.getRefBoardId());
		
		ObjectMapper om = new ObjectMapper();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		om.setDateFormat(sdf);
		String str = null;
		try {
			str = om.writeValueAsString(rlist);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	@GetMapping("/rdelete")
	@ResponseBody
	public int deleteReply(@RequestParam("replyId") int replyId) {
		return bService.deleteReply(replyId);
	}
	
	@PostMapping("/rupdate")
	@ResponseBody
	public int updateReply(Reply r) {
		return bService.updateReply(r);
	}
}
