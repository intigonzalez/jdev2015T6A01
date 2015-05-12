package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: DBObject
 *
 */


public interface DBObject extends Serializable {


 
	public Date getCreatedAt();

	public void setCreatedAt(Date createdAt);
	public Date getUpdatedAt() ;

	public void setUpdatedAt(Date updatedAt);
   
}
