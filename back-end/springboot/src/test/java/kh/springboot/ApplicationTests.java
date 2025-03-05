package kh.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kh.springboot.board.controller.BoardController;
import kh.springboot.board.model.vo.Board;

@SpringBootTest
class ApplicationTests {
	@Autowired
	private BoardController controller;
	
	@BeforeAll
	public static void startTest() {
		System.out.println("start test");
	}
	
	@Test
	void contextLoads() {
		Board b = new Board();
		b.setBoardContent("hello world");
		b.setBoardCount(0);
		b.setBoardId(1);
		b.setBoardTitle("hello");
		b.setBoardType(0);
		b.setBoardWriter("yoon");
		b.setCreateDate(new Date(new java.util.Date().getTime()));
		b.setModifyDate(new Date(new java.util.Date().getTime()));
		b.setNickName("yoon");
		b.setStatus("Y");
		
		assertEquals("redirect:/board/1/1", controller.updateBoard(b,1));
	}
	
	@AfterAll
	public static void endTest() {
		System.out.println("end test");
	}
}
