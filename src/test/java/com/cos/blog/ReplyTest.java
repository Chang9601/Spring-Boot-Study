package com.cos.blog;

import org.junit.Test;

import com.cos.blog.model.Reply;

public class ReplyTest {

	@Test
	public void testToString() {
		Reply reply = Reply.builder()
				.id(1)
				.user(null)
				.board(null)
				.content("Hello")
				.build();
		
		System.out.println(reply);
	}
}
