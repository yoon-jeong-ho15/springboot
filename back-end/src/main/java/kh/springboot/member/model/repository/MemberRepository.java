package kh.springboot.member.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kh.springboot.board.model.entity.Board;

@Repository
public interface MemberRepository extends JpaRepository<Board, Integer>{

}
