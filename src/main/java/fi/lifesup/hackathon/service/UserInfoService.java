package fi.lifesup.hackathon.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.lifesup.hackathon.domain.UserInfo;
import fi.lifesup.hackathon.repository.UserInfoRepository;
import fi.lifesup.hackathon.service.dto.UserInfoImageDTO;

@Service
public class UserInfoService {
	
	@Inject
	private UserInfoRepository userInfoRepository;
	
	@Value("${attach.path}")
	private String attachPath;

	@Transactional
	public UserInfo updateUserInfoBanner(UserInfoImageDTO dto) {
		UserInfo userInfo = userInfoRepository.findOne(dto.getUserInfoId());
		String filePath = null;
		try {
			// tao thu muc
			String dirPath = attachPath + "/userInfo/";
			File dir = new File(dirPath);
			if (!dir.exists()) {
				if (dir.mkdirs()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}

			// lay thong tin file
			String[] fileTypeArray = dto.getFiletype().split("/");
			String extention = fileTypeArray[1];
			filePath = dirPath + "/" + dto.getUserInfoId() + "." + extention;
			// tao file
			File file = new File(filePath);
			file.createNewFile();

			byte[] bytes = DatatypeConverter.parseBase64Binary(dto.getBase64());

			InputStream in = new ByteArrayInputStream(bytes);
			BufferedImage bImageFromConvert = ImageIO.read(in);
			ImageIO.write(bImageFromConvert, extention, file);

			String displayPath = filePath.replace("src/main/webapp/", "");
			userInfo.setLogoUrl(displayPath);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return userInfoRepository.save(userInfo);
	}

}
