package com.cos.blog.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;

@RestController
public class ReplyControllerTest {
	private final BoardRepository boardRepository;

	private final ReplyRepository replyRepository;
	
	@Autowired
	public ReplyControllerTest(BoardRepository boardRepository, ReplyRepository replyRepository) {
		this.boardRepository = boardRepository;
		this.replyRepository = replyRepository;
	}
	
	@GetMapping("/test/board/{id}")
	public Board getBoard(@PathVariable int id) {
		return boardRepository.findById(id).get(); // Jackson 라이브러리(객체를 JSON으로 반환) -> 객체의 getter 호출
	}
	
	@GetMapping("/test/reply")
	public List<Reply> getBoard() {
		return replyRepository.findAll(); // Jackson 라이브러리(객체를 JSON으로 반환) -> 객체의 getter 호출
	}
}