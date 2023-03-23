package HelloMyTeam.Hellomyteam.service;

import HelloMyTeam.Hellomyteam.dto.AuthNumDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
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

    @Autowired
    private TokenProvider tokenProvider;
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

        System.out.println("시크릿 : " + secretKey);
        System.out.println(secretKey.getBytes());
        String token = Jwts.builder()
                        .setSubject(mailId)
                        .claim("authNumber", authNumber)
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                        .compact();

        System.out.println(authNumDto.getAuthCode());
        System.out.println(authNumDto.getAuthNumber());
        System.out.println(token);

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
            System.out.println("에러가 발생했습니다1.");
            System.out.println(e);
            e.printStackTrace();
        }catch (Exception e){
            System.out.println("에러가 발생했습니다2.");
            System.out.println(e);
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

        String title = "테스트 입니다.";
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
            System.out.println("올바른 이메일 주소 형식이 아닙니다.");
            ex.printStackTrace();
        }catch (MessagingException ex){
            System.out.println("이메일 전송 실패!");
            ex.printStackTrace();
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

    public AuthNumDto getAuthNumDto(){
        System.out.println("getAuthNumDto 실행");
        System.out.println(authNumDto.getAuthCode());
        return authNumDto;
    }
}
