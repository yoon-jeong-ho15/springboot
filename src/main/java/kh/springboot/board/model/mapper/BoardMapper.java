package kh.springboot.board.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import kh.springboot.board.model.vo.Attachment;
import kh.springboot.board.model.vo.Board;
import kh.springboot.board.model.vo.Reply;

@Mapper
public interface BoardMapper {

	int getListCount(int i);

	ArrayList<Board> selectBoardList(int boardType, RowBounds rowBounds);

	int insertBoard(Board b);

	Board selectBoard(int bId);

	int addCount(int bId);

	int updateBoard(Board b);

	int deleteBoard(int bid);

	ArrayList<Attachment> selectAttmBoardList(Integer bid);

	int insertAttm(ArrayList<Attachment> list);

	int deleteAttm(ArrayList<String> delRename);

	void updateAttmLevel(int boardId);

	ArrayList<Board> selectTop();

	ArrayList<Reply> selectReplyList(int bId);

	int insertReply(Reply r);

	int deleteReply(int replyId);

	int updateReply(Reply r);

	ArrayList<Board> selectRecentBoards();

	int updateBoardStatus(HashMap<String, Object> map);

//	int statusNAttm(int bid);

}
