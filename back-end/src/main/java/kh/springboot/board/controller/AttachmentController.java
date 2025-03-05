package kh.springboot.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kh.springboot.board.model.exception.BoardException;
import kh.springboot.board.model.service.BoardService;
import kh.springboot.board.model.vo.Attachment;
import kh.springboot.board.model.vo.Board;
import kh.springboot.board.model.vo.PageInfo;
import kh.springboot.common.Pagination;
import kh.springboot.member.model.vo.Member;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/attm")
@RequiredArgsConstructor
public class AttachmentController {
private final BoardService bService;
	
	@GetMapping("list")
	public String selectList(
			@RequestParam(value="page", defaultValue="1") int page,
			Model model, HttpServletRequest request) {
		int listCount = bService.getListCount(2);
		PageInfo pi = Pagination.getPageInfo(page, listCount, 9);
		ArrayList<Board> blist = bService.selectBoardList(pi, 2);
		ArrayList<Attachment> alist = bService.selectAttmBoardList(null);
		
		if(blist != null) {
			model.addAttribute("blist",blist).
			addAttribute("alist", alist).addAttribute("pi",pi)
			.addAttribute("loc", request.getRequestURI());
			return "views/attm/list";
		} else {
			throw new BoardException("failed (attm board)");
		}
	}
	
	@GetMapping("/write")
	public String writeAttm() {
		return "views/attm/write";
	}
	
	@PostMapping("/insert")
	@Transactional
	public String insertAttmBoard(@ModelAttribute Board b, 
			@RequestParam("file") ArrayList<MultipartFile> files,
			HttpSession session) {
		
		String id = ((Member)session.getAttribute("loginUser")).getId();
		b.setBoardWriter(id);
		System.out.println("before : "+b.getBoardId());
		
		ArrayList<Attachment> list = new ArrayList<Attachment>();
		for (int i=0; i<files.size();i++) {
			MultipartFile upload = files.get(i);
			if(!upload.getOriginalFilename().equals("")) {
				String[] returnArr = saveFile(upload);
				if(returnArr[1] != null) {
					Attachment a = new Attachment();
					a.setOriginalName(upload.getOriginalFilename());
					a.setRenameName(returnArr[1]);
					a.setAttmPath(returnArr[0]);
					list.add(a);
				}
			}
		}
		
		for(int i=0;i<list.size();i++) {
			Attachment a = list.get(i);
			if (i == 0) {
				a.setAttmLevel(0);
			}else {
				a.setAttmLevel(1);
			}
		}
		
		int result1=0;
		int result2=0;
		if(list.isEmpty()) {
			b.setBoardType(1);
			result1=bService.insertBoard(b);
		}else {
			b.setBoardType(2);
			result1 = bService.insertBoard(b);
			System.out.println("after : "+b.getBoardId());
			
			for(Attachment a : list) {
				a.setRefBoardId(b.getBoardId());
			}
			
			result2 = bService.insertAttm(list);
		}
		
		if(result1 + result2 == list.size()+1){
			if(result2 == 0) {
				return "redirect:/board/list";
			}else {
				return "redirect:/attm/list";
			}
		}else {
			for(Attachment a : list) {
				deleteFile(a.getRenameName());
			}
			throw new BoardException("failed (attm)");
		}
	}
	
	private void deleteFile(String renameName) {
		String savePath = "C:\\uploadFiles";
		
		File f = new File(savePath +"\\"+renameName);
		if(f.exists()) {
			f.delete();
		}
	}

	private String[] saveFile(MultipartFile upload) {
		String savePath = "C:\\uploadFiles";
		File folder = new File(savePath);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		int ranNum = (int)(Math.random()*100000);
		String originFileName = upload.getOriginalFilename();
		String renameFileName = sdf.format(new Date()) + ranNum +
				originFileName.substring(originFileName.lastIndexOf("."));
		
		String renamePath = folder +"\\"+renameFileName;
		try {
			upload.transferTo(new File(renamePath));
		} catch (IllegalStateException | IOException e) {
			System.out.println(e.getMessage());
		}
		
		String[] returnArr = new String[2];
		returnArr[0] = savePath;
		returnArr[1] = renameFileName;
		
		return returnArr;
	}

	@GetMapping("/{id}/{page}")
	public ModelAndView selectAttm(@PathVariable("id") int refBoardId,
			@PathVariable("page") int page,
			HttpSession session, ModelAndView mv) {
		Member loginUser = (Member) session.getAttribute("loginUser");
		String login = "";
		if(loginUser != null) {
			login = loginUser.getId();
		}
		
		Board b = bService.selectBoard(refBoardId, login);
		ArrayList<Attachment> list = bService.selectAttmBoardList(refBoardId);
		
		if (b != null) {
			mv.addObject("b",b).addObject("page",page)
				.addObject("list",list).setViewName("views/attm/detail");
			return mv;
		}else {
			throw new BoardException("failed (attm detail)");
		}
	}
	
	@PostMapping("updForm")
	public String updateForm(@RequestParam("boardId") int bid,
			@RequestParam("page") int page, Model model) {
		Board b = bService.selectBoard(bid, null);
		ArrayList<Attachment> list = bService.selectAttmBoardList(bid);
		
		model.addAttribute("b", b).addAttribute("list", list).addAttribute("page", page);
		return "views/attm/edit";
	}
	
	@PostMapping("update")
	public String updateBoard(@ModelAttribute Board b,
			@RequestParam("page") int page,
			@RequestParam("deleteAttm") String[] deleteAttm,
			@RequestParam("file") ArrayList<MultipartFile> files) {
		
		b.setBoardType(2);
		
		ArrayList<Attachment> list = new ArrayList<Attachment>();
		for(int i=0;i<files.size();i++) {
			MultipartFile upload = files.get(i);
			if(!upload.getOriginalFilename().equals("")) {
				String[] returnArr = saveFile(upload);
				if(returnArr[1] != null) {
					Attachment a = new Attachment();
					a.setOriginalName(upload.getOriginalFilename());
					a.setRenameName(returnArr[1]);
					a.setAttmPath(returnArr[0]);
					a.setRefBoardId(b.getBoardId());
					
					list.add(a);
				}
			}
		}
		
		ArrayList<String> delRename = new ArrayList<String>();
		ArrayList<Integer> delLevel = new ArrayList<Integer>();
		for(String rename : deleteAttm) {
			if(!rename.equals("")) {
				String[] split = rename.split("/");
				delRename.add(split[0]);
				delLevel.add(Integer.parseInt(split[1]));
			}
		}
		
		int deleteAttmResult = 0;
		int updateBoardResult = 0;
		boolean existBeforeAttm = true;
		
		if(!delRename.isEmpty()) {
			deleteAttmResult = bService.deleteAttm(delRename);
			if (deleteAttmResult > 0) {
				for(String rename : delRename) {
					deleteFile(rename);
				}
			}
			
			if(deleteAttm.length != 0 && delRename.size() == deleteAttm.length) {
				existBeforeAttm = false;
				if(list.isEmpty()) {
					b.setBoardType(1);
				}
			}else {
				for(int level : delLevel) {
					if(level == 0) {
						System.out.println("들어옴");
						bService.updateAttmLevel(b.getBoardId());
						break;
					}
				}
			}
		}
		
		for(int i=0;i<list.size();i++) {
			Attachment a = list.get(i);
			if(existBeforeAttm) {
				a.setAttmLevel(1);
			}else {
				if(i==0) {
					a.setAttmLevel(0);
				}else {
					a.setAttmLevel(1);
				}
			}
		}
		
		updateBoardResult = bService.updateBoard(b);
		int updateAttmResult = 0;		
		if(!list.isEmpty()) {
			updateAttmResult = bService.insertAttm(list);
		}
		
		if(updateBoardResult + updateAttmResult == list.size() + 1) {
	         if(!existBeforeAttm && list.size() == 0) {
	            return "redirect:/board/list";
	         }else {
	            return String.format("redirect:/attm/%d/%d", b.getBoardId(), page) ;
	         }
	      }else {
	         throw new BoardException("첨부파일 게시글 수정을 실패하였습니다.");
	      }
	}
	
//	@PostMapping("delete")
//	public String delete(@RequestParam("boardId") int bid) {
//		int result1 = bService.deleteBoard(bid);
//		int result2 = bService.statusNAttm(bid);
//		
//		return "redirect:/attm/list";
//	}
	
	
}
