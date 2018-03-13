package parser.bean;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * The Bean representing Blocked IP objects
 * 
 * @author lelkadi
 *
 */
public class BlockedIpBean {

	Long id;
	String ip;
	String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
