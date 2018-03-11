package parser.bean;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * The Bean representing Record objects
 * 
 * @author lelkadi
 *
 */
public class RecordBean {

	String date;
	String ip;
	String request;
	String status;
	String userAgent;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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
