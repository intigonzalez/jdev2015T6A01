package com.enseirb.telecom.s9.db;

import org.bson.types.ObjectId;

public class CommentRepositoryObject {

	
	String videoId;
	ObjectId commentId;
	String userId;
	double unix_time;
	String comment;
	
	
	public CommentRepositoryObject(){	
	}
	
	public CommentRepositoryObject(Object videoId, String commentId, String userId, double unix_time,
			String comment){
		
		//this.videoId = videoId;
		//this.commentId = commentId;
		this.userId = userId;
		this.unix_time = unix_time;
		this.comment = comment;
	}
	
	public void setVideoId(Object videoId){
		//this.videoId = videoId;
	}
	
	public void setCommentId(String commentId){
		//this.commentId = commentId;
	}
	
	public void setUserId(String userId){
		this.userId = userId;
	}
	
	public void setUnixTime(double unix_time){
		this.unix_time = unix_time;
	}
	
	public void setComment(String comment){
		this.comment = comment;
	}
	
	/*public Object getVideoId(){
		return videoId;
	}	
	public String getCommentId(){
		//return commentId;
	}	
	public String getUserId(){
		return userId;
	}
	public double getUnixTime(){
		return unix_time;
	}
	public String getComment(){
		return comment;
	}*/
}
