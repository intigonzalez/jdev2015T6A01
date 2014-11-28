package com.enseirb.telecom.s9.db;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.enseirb.telecom.s9.Comment;

public class CommentRepositoryObject {

	@JsonProperty("_id")
	ObjectId commentId;
	String videoId;
	String login;
	double unix_time;
	String comment;
	
	
	public CommentRepositoryObject(){	
	}
	
	public CommentRepositoryObject(String videoId, ObjectId commentId, String login, double unix_time,
			String comment){
		
		this.videoId = videoId;
		this.commentId = commentId;
		this.login = login;
		this.comment = comment;
	}
	
	public CommentRepositoryObject(String videoId, Comment comment){
		
		commentId = new ObjectId(comment.getCommentID());
		this.videoId = videoId;
		login = comment.getLogin();
		unix_time = comment.getUnixTime();
		this.comment = comment.getComment();
	}
	
	public void setVideoId(String videoId){
		this.videoId = videoId;
	}
	
	public void setCommentId(ObjectId object){
		this.commentId = object;
	}
	
	public void setUserId(String login){
		this.login = login;
	}
	
	public void setUnixTime(double unix_time){
		this.unix_time = unix_time;
	}
	
	public void setComment(String comment){
		this.comment = comment;
	}
	
	public String getVideoId(){
		return videoId;
	}	
	public ObjectId getCommentId(){
		return commentId;
	}	
	public String getLogin(){
		return login;
	}
	public double getUnixTime(){
		return unix_time;
	}
	public String getComment(){
		return comment;
	}
	
	public Comment toComment(){
		
		Comment comment = new Comment();
		
		comment.setComment(this.getComment());
		comment.setCommentID(this.getCommentId().toString());
		comment.setLogin(this.getLogin());
		comment.setUnixTime(this.getUnixTime());		
		
		return comment;		
	}
	
}
