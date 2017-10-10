package fi.lifesup.hackathon.domain;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "application_invite_mail")
public class ApplicationInviteEmail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "send", nullable = false)
	private Boolean send;


	@Column(name = "send_time", nullable = false)
	private ZonedDateTime sendTime;

	
	@Column(name = "accept_key", nullable = false)
	private String acceptKey;

	@ManyToOne
    private Application application;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getSend() {
		return send;
	}

	public void setSend(Boolean send) {
		this.send = send;
	}

	public ZonedDateTime getSendTime() {
		return sendTime;
	}

	public void setSendTime(ZonedDateTime sendTime) {
		this.sendTime = sendTime;
	}

	public String getAcceptKey() {
		return acceptKey;
	}

	public void setAcceptKey(String acceptKey) {
		this.acceptKey = acceptKey;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acceptKey == null) ? 0 : acceptKey.hashCode());
		result = prime * result + ((application == null) ? 0 : application.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((send == null) ? 0 : send.hashCode());
		result = prime * result + ((sendTime == null) ? 0 : sendTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplicationInviteEmail other = (ApplicationInviteEmail) obj;
		if (acceptKey == null) {
			if (other.acceptKey != null)
				return false;
		} else if (!acceptKey.equals(other.acceptKey))
			return false;
		if (application == null) {
			if (other.application != null)
				return false;
		} else if (!application.equals(other.application))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (send == null) {
			if (other.send != null)
				return false;
		} else if (!send.equals(other.send))
			return false;
		if (sendTime == null) {
			if (other.sendTime != null)
				return false;
		} else if (!sendTime.equals(other.sendTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ApplicationInviteEmail [id=" + id + ", email=" + email + ", send=" + send + ", sendTime=" + sendTime
				+ ", acceptKey=" + acceptKey + ", application=" + application + "]";
	}
	
	
	
}
