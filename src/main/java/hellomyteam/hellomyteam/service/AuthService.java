package hellomyteam.hellomyteam.service;

import hellomyteam.hellomyteam.dto.AuthNumDto;
import hellomyteam.hellomyteam.dto.CommonResponse;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    
    //private final JwtTokenProvider jwtTokenProvider;
    private int authNumber;
    private String uniqueAuthNumber;
    private static final long EXPIRATION_TIME = 10 * 60 * 1000;

    private AuthNumDto authNumDto;
    private final TokenProvider tokenProvider;

    @Value("${jwt.secret}")
    private String secretKey;
    public String sendMail(String mailId){

        Random random = new Random();
        authNumber = random.nextInt(888888)+111111;

        uniqueAuthNumber = mailId + "_" + authNumber;
        authNumDto = AuthNumDto.builder()
                .authCode(uniqueAuthNumber)
                .authNumber(authNumber)
                .build();

        System.out.println(mailId+"/"+ authNumber+"/"+ EXPIRATION_TIME);
        String token = tokenProvider.mailToken(mailId, authNumber, EXPIRATION_TIME);
        /*String token = Jwts.builder()
                        .setSubject(mailId)
                        .claim("authNumber", authNumber)
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                        .compact();*/

        log.debug(authNumDto.getAuthCode());
        log.debug(String.format("%d",authNumDto.getAuthNumber()));
        log.debug(token);

        Properties authProperties = new Properties();
        try{
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("authProperties.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("property");
            for(int temp = 0; temp < nodeList.getLength(); temp++){
                Element element = (Element) nodeList.item(temp);
                String propertyName = element.getAttribute("name");
                String propertyValue = element.getTextContent();
                authProperties.setProperty(propertyName, propertyValue);
            }
            inputStream.close();
        }catch (IOException e){
            log.debug("IOException 발생");
            log.error("An error occurred ",e);
        }catch (Exception e){
            log.debug("Exception 발생");
            log.error("An error occurred ",e);
        }

        Session session = Session.getInstance(authProperties, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                String username = authProperties.getProperty("mail.username");
                String password = authProperties.getProperty("mail.password");
                String cryptorPw = authProperties.getProperty("mail.encryptor.pw");
                String encryptedUsername = decrypt(username,cryptorPw);
                String encryptedPassword = decrypt(password,cryptorPw);
                return new PasswordAuthentication(encryptedUsername, encryptedPassword);
            }
        });

        String title = "[헬로마이팀] 회원가입을 위한 인증번호입니다.";
        String body =
                "안녕하세요 가입을 위해 아래 인증번호를 입력해주세요." +
                "<br><br>" +
                "인증 번호는 <b>" + authNumber + "</b>입니다." +
                "<br>" +
                "해당 인증번호를 인증번호 확인란에 기입하여 주세요.";

        String myMail = "wonerwoner@naver.com";
        Message message = new MimeMessage(session);
        try {
            MimeMessageHelper mmh = new MimeMessageHelper((MimeMessage) message,true,"utf-8");
            mmh.setFrom(new InternetAddress(myMail));
            mmh.setSubject(title);
            mmh.setTo(mailId);
            mmh.setText(body,true);
            Transport.send(message);
        }catch (AddressException ex){
            log.debug("올바른 이메일 주소 형식이 아닙니다.");
            log.error("An error occurred ",ex);
        }catch (MessagingException ex){
            log.debug("이메일 전송 실패!");
            log.error("An error occurred ",ex);
        }

        return token;
    }

    private String encrypt (String str, String pw){
        //암호화
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        encryptor.setPassword(pw);

        return encryptor.encrypt(str);
    }

    private String decrypt (String str, String pw){
        //복호화
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

        encryptor.setPassword(pw);

        return encryptor.decrypt(str);
    }

    public CommonResponse<?> chkJWT(String token, int authNumber){
        if(StringUtils.hasText(token)&&tokenProvider.verifyToken(token)){
            int AuthNum = tokenProvider.getAuthNumber(token);
            if(authNumber == AuthNum){
                return CommonResponse.createSuccess("인증 성공");
            }else{
                return CommonResponse.createError("인증 실패");
            }
        }else{
            return CommonResponse.createError("토큰 만료");
        }
    }
}
