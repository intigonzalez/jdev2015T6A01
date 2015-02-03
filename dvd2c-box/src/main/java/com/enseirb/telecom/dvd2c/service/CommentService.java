package com.enseirb.telecom.dvd2c.service;

import com.enseirb.telecom.s9.Comment;

public interface CommentService {
	
	public abstract boolean commentExit(String email);
	
	public abstract Comment getComment(String email);
	
	public abstract Comment createComment(Comment comment);
	
	public abstract void saveComment(Comment comment);
	
	public abstract void deleteComment(String email);

}
