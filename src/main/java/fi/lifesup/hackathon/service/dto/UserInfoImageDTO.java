package fi.lifesup.hackathon.service.dto;

public class UserInfoImageDTO {
private long userInfoId;
private String filetype;

private String filename;

private String filesize;

private String base64;

public long getUserInfoId() {
	return userInfoId;
}

public void setUserInfoId(long userInfoId) {
	this.userInfoId = userInfoId;
}

public String getFiletype() {
	return filetype;
}

public void setFiletype(String filetype) {
	this.filetype = filetype;
}

public String getFilename() {
	return filename;
}

public void setFilename(String filename) {
	this.filename = filename;
}

public String getFilesize() {
	return filesize;
}

public void setFilesize(String filesize) {
	this.filesize = filesize;
}

public String getBase64() {
	return base64;
}

public void setBase64(String base64) {
	this.base64 = base64;
} 

}
