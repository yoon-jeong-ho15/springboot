package kh.springboot.member.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kh.springboot.board.model.entity.Board;
import kh.springboot.member.model.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Board, Integer>{

	Optional<Member> findByUsernameAndPwd(String username, String pwd)
}
