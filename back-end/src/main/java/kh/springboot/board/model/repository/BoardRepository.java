package kh.springboot.board.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kh.springboot.board.model.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer>{

}
