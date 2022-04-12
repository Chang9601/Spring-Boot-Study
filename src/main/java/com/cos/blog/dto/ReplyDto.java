package com.cos.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ReplyDto {
	private int userId;
	private int boardId;
	private String content;
}