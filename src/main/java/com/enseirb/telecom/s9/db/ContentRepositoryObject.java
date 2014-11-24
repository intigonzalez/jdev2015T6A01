package com.enseirb.telecom.s9.db;

import java.util.List;

import com.enseirb.telecom.s9.Authorization;
import com.enseirb.telecom.s9.Comment;

public class ContentRepositoryObject {
	String Id;
    String name;
    String userId;
    Long unixTime;
    String link;
    List<Comment> comment;
    List<Authorization> authorizations;
}
