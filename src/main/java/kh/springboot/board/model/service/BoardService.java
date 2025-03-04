package kh.springboot.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import kh.springboot.board.model.mapper.BoardMapper;
import kh.springboot.board.model.vo.Attachment;
import kh.springboot.board.model.vo.Board;
import kh.springboot.board.model.vo.PageInfo;
import kh.springboot.board.model.vo.Reply;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	private final BoardMapper mapper;

	public int getListCount(int i) {
		return mapper.getListCount(i);
	}

	public ArrayList<Board> selectBoardList(PageInfo pi, int boardType) {
		int offset = (pi.getCurrentPage()-1)*pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return mapper.selectBoardList(boardType, rowBounds);
	}

	public int insertBoard(Board b) {
		return mapper.insertBoard(b);
	}

	public Board selectBoard(int bId, String login) {
		Board b = mapper.selectBoard(bId);
		if (b != null && !b.getBoardWriter().equals(login) && login!="") {
			int result = mapper.addCount(bId);
			if (result>0) {
				b.setBoardCount(b.getBoardCount()+1);
			}
		}
		return b;
	}

	public int updateBoard(Board b) {
		return mapper.updateBoard(b);
	}

	public int deleteBoard(int bid) {
		return mapper.deleteBoard(bid);
	}

	public ArrayList<Attachment> selectAttmBoardList(Integer bid) {
		return mapper.selectAttmBoardList(bid);
	}

	public int insertAttm(ArrayList<Attachment> list) {
		return mapper.insertAttm(list);
	}

	public int deleteAttm(ArrayList<String> delRename) {
		return mapper.deleteAttm(delRename);
	}

	public void updateAttmLevel(int boardId) {
		mapper.updateAttmLevel(boardId);
	}

	public ArrayList<Board> selectTop() {
		return mapper.selectTop();
	}

	public ArrayList<Reply> selectReplyList(int bId) {
		return mapper.selectReplyList(bId);
	}

	public int insertReply(Reply r) {
		return mapper.insertReply(r);
	}

	public int deleteReply(int replyId) {
		return mapper.deleteReply(replyId);
	}

	public int updateReply(Reply r) {
		return mapper.updateReply(r);
	}

	public ArrayList<Board> selectRecentBoards() {
		return mapper.selectRecentBoards();
	}

	public int updateBoardStatus(HashMap<String, Object> map) {
		return mapper.updateBoardStatus(map);
	}

//	public int statusNAttm(int bid) {
//		return mapper.statusNAttm(bid);
//	}
}
