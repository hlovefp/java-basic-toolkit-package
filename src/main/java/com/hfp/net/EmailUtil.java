package com.hfp.net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;
//import java.util.Scanner;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class EmailUtil {
	
	/**
	 * JavaMail发送邮件
	 * @return
	 */
	public static boolean sendEmail(String smtp, String port, 
			String from, String passwd, String[] to, 
			String subject, String mess){
		
		if( to == null || to.length == 0 ){
			return false;
		}

        Properties props = System.getProperties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", smtp);
        props.setProperty("mail.smtp.auth", "true");

        // ssl
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", "465");

        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);
        Transport tr = null;
        try{
        	message.setFrom(new InternetAddress(from));
			message.setRecipient(RecipientType.TO, new InternetAddress(to[0])); // 收件人
	        for(int i=1;i<to.length;i++){
	        	message.addRecipient(RecipientType.TO, new InternetAddress(to[i]));
	        }
        	//message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(cc)); // 抄送人
            //message.addRecipient(MimeMessage.RecipientType.CC, new InternetAddress(cc));
	        
            //message.setRecipient(MimeMessage.RecipientType.BCC, new InternetAddress(bcc)); // 密送人
	        
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setContent(mess,"text/html;charset=utf-8");
            message.saveChanges();

            tr = session.getTransport();
            tr.connect(from,passwd);
            tr.sendMessage(message,message.getAllRecipients());
        }catch(Exception e){
        	e.printStackTrace();
        	return false;
        }finally{
			try {
				if( tr != null ) tr.close();
			} catch (MessagingException e) {
				e.printStackTrace();
				return false;
			}
        }

		return true;
	}
	
	public static void sendMailDemo() throws IOException{
		
		String host = "smtp.163.com";
		int port = 465;
		String from = "hefeiping.love@163.com";
		String to = "876687377@qq.com";
		//String cc = "708831172@qq.com";
		String subject = "测试";
		String mailMessage = "这是一封测试邮件.......";
				
		/*
		过去SMTP(简单邮件传输协议)专用端口25发送邮件
		SMTP规范 RFC 821
		SMTP发送邮件
		*/
		Socket s = new Socket(host,port); // 阻塞于connect
		PrintWriter out = new PrintWriter(s.getOutputStream(),true/*autoFlush*/);
		//Scanner scan = new Scanner(s.getInputStream());

		// 可以省略
		out.print("HELO "+host+"\r\n");
		System.out.println("HELO "+host);
		//while( scan.hasNextLine() ) System.out.println("HELO: "+scan.nextLine());  // 收到"250 OK"
		
		// 发送方
		out.print("MAIL FROM: "+from+"\r\n");
		System.out.println("MAIL FROM: "+from);
		//while( scan.hasNextLine() ) System.out.println("HELO: "+scan.nextLine());  // 收到"250 OK"
		
		// 接收方有多个，则执行命令RCPT TO多次
		out.print("RCPT TO: "+to+"\r\n");
		System.out.println("RCPT TO: "+to);
		//while( scan.hasNextLine() ) System.out.println("HELO: "+scan.nextLine());  // 收到"250 OK"
		
		
		//out.print("RCPT TO: "+to1+"\r\n");
		//if( scan.hasNextLine() ) scan.nextLine();
		
		// 收到"250 OK" "550 No such user here"
		// 251：用户不在本地；将向前发送到
		// 551 ：用户非本地，请尝试
		

		out.print("DATA"+"\r\n");  
		System.out.println("DATA");
		// "354 Start mail input; end with ."应答
		
		// 以下各行都是信件内容
		out.print("\r\n");
		// Date, Subject, To, Cc, From
		out.print("Subject: "+subject+"\r\n");
		System.out.println("Subject: "+subject);
		//while( scan.hasNextLine() ) System.out.println("HELO: "+scan.nextLine());  // 收到"250 OK"

		out.print(mailMessage+"\r\n");
		System.out.println(mailMessage);
		//while( scan.hasNextLine() ) System.out.println("HELO: "+scan.nextLine());  // 收到"250 OK"
		
		out.print("."+"\r\n");
		out.print("QUIT"+"\r\n");
		System.out.println("QUIT");
		//while( scan.hasNextLine() ) System.out.println("HELO: "+scan.nextLine());  // 收到"250 OK"

		/*
		SMTP确认地址
		  "VRFY "+地址+"\r\n"
		  应答：
		    250 Fred Smith
		    251 User not local; will forward to 
		    550 String does not match anything.
		    551 User not local; please try
		    553 User ambiguous

		SMTP扩展邮件列表
		   "EXPN "+"Example-People"+"\r\n"
		   或者  "EXPN "+"Executive-Washroom-List"+"\r\n"
		   多个响应：
		*/
		s.close();

	}
	
	public static void main(String[] args) throws IOException{
		/*
		sendEmail("smtp.163.com", 
				"465", 
				"hefeiping.love@163.com", 
				"....", 
				new String[]{"876687377@qq.com","708831172@qq.com"}, 
				"测试", 
				"这是一封测试邮件");
				*/
	}
}
