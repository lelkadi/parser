package parser.bean;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * The Bean representing Record objects
 * 
 * @author lelkadi
 *
 */
public class RecordBean {

	Long id;
	Date date;
	String ip;
	String request;
	Integer status;
	String userAgent;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
