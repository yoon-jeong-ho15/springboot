package kh.springboot.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodoList {
	private int todoNum;
	private String todo;
	private String writer;
	private String status;
	private String important;
}
