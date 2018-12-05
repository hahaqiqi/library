package com.november.util;

import java.util.Random;

import com.november.exception.ParamException;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class Email {

	public static String GetCode(String Toemail) {
		String code="";
		HtmlEmail email=new HtmlEmail();
		email.setHostName("smtp.qq.com");
		email.setCharset("utf-8");//设置发送的字符类型
		Random r=new Random();
		String ryzm=r.nextInt(99999)+100000+"";
		try {
			email.addTo(Toemail);//设置收件人
			email.setFrom("2546082422@qq.com","[SYY图书馆]");//发送人的邮箱为自己的，用户名可以随便填
			email.setAuthentication("2546082422@qq.com","pzkktrkvmnbadjfd");
			email.setSubject("身份认证");//设置发送主题
			email.setMsg("您的验证码是："+ryzm);//设置发送内容
			email.send();//进行发送
			code=ryzm;
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			throw new ParamException("验证码发送失败");
		}
		return code;
	}

	public static void remindUser(String Toemail,String text) {
		HtmlEmail email=new HtmlEmail();
		email.setHostName("smtp.qq.com");
		email.setCharset("utf-8");//设置发送的字符类型
		try {
			email.addTo(Toemail);//设置收件人
			email.setFrom("2546082422@qq.com","[SYY图书馆]");//发送人的邮箱为自己的，用户名可以随便填
			email.setAuthentication("2546082422@qq.com","pzkktrkvmnbadjfd");
			email.setSubject("租借提醒");//设置发送主题
			email.setMsg(text);//设置发送内容
			email.send();//进行发送
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("发送失败");
		}
	}
}