package com.facevisitor.api.service.aws;//package com.maison.api.service.aws;
//
//import com.amazonaws.AmazonClientException;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
//import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
//import com.fooddeuk.api.domain.user.User;
//import com.fooddeuk.api.service.user.UserService;
//import com.fooddeuk.api.vo.email.SESSender;
//import java.util.List;
//import java.util.stream.Collectors;
//import javax.mail.internet.MimeUtility;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Slf4j
//@Service
//@Transactional
//public class AWSSESServiceImpl implements AWSSESService {
//
//    @Autowired
//    private AWSCredentialsService awsCredentialsService;
//
//    @Autowired
//    private UserService userService;
//
//    @Value("${aws.ses.sender.email}")
//    private String from;
//
//    @Value("${aws.ses.sender.name}")
//    private String name;
//
//    private AmazonSimpleEmailService client() {
//        return AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(awsCredentialsService.credentials()).withRegion(Regions.US_EAST_1).build();
//    }
//
//    @Override
//    public void send(SESSender sesSender) {
//        log.debug("sesSender ::: {}", sesSender);
//
//        try {
//            final String fromName = MimeUtility.encodeText(name); // RFC 2047 - UTF-8 인코딩 방식
//            sesSender.setFrom(String.format("\"%s\" <%s>", fromName, from));
//            this.client().sendEmail(sesSender.toSendRequestDto());
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            throw new AmazonClientException(ex.getMessage(), ex);
//        }
//    }
//
//    @Override
//    public void sendToAdmin(SESSender sesSender) {
//
//        List<User> admins = userService.userByAdmin();
//        if (admins != null) {
//            List<String> emails = admins.stream().map(User::getEmail).collect(Collectors.toList());
//            sesSender.setTo(emails);
//            this.send(sesSender);
//        }
//    }
//}
