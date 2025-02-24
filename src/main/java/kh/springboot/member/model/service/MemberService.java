package kh.springboot.member.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import kh.springboot.member.model.mapper.MemberMapper;
import kh.springboot.member.model.vo.Member;
import kh.springboot.member.model.vo.TodoList;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberMapper mapper;
	
	public Member login(Member m) {
		return mapper.login(m);
	}

	public int insertMember(Member m) {
		return mapper.insert(m);
	}

	public ArrayList<HashMap<String, Object>> selectMyList(String id) {
		return mapper.selectMyList(id);
	}

	public int editMemberInfo(Member m) {
		return mapper.editMemberInfo(m);
	}

	public int updatePassword(HashMap<String, String> map) {
		return mapper.updatePassword(map);
	}

	public int deleteMember(String id) {
		return mapper.deleteMember(id);
	}

	public int checkValue(HashMap<String, String> map) {
		return mapper.checkValue(map);
	}

	public int updateProfile(HashMap<String, String> map) {
		return mapper.updateProfile(map);
	}

	public Member findInfo(Member m) {
		return mapper.findInfo(m);
	}

	public ArrayList<TodoList> selectTodoList(String id) {
		return mapper.selectTodoList(id);
	}

	public int insertTodo(TodoList todo) {
		return mapper.insertTodo(todo);
	}

	public int updateTodo(TodoList todo) {
		return mapper.updateTodo(todo);
	}

	public int deleteTodo(int todoNum) {
		return mapper.deleteTodo(todoNum);
	}
	
}
