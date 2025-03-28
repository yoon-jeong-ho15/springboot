package kh.springboot.member.model.mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import kh.springboot.member.model.vo.Member;
import kh.springboot.member.model.vo.TodoList;

@Mapper
public interface MemberMapper {

	Member login(Member m);

	int insert(Member m);

	ArrayList<HashMap<String, Object>> selectMyList(String id);

	int editMemberInfo(Member m);

	int updatePassword(HashMap<String, String> map);

	int deleteMember(String id);

	int checkValue(HashMap<String, String> map);

	int updateProfile(HashMap<String, String> map);

	Member findInfo(Member m);

	ArrayList<TodoList> selectTodoList(String id);

	int insertTodo(TodoList todo);

	int updateTodo(TodoList todo);

	int deleteTodo(int todoNum);

	ArrayList<Member> selectMembers();

	int updateMemberItem(HashMap<String, String> map);

}
