package com.shopme;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import com.shopme.security.oauth.CustomerOAuth2User;
import com.shopme.setting.CurrencySettingBag;
import com.shopme.setting.EmailSettingBag;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {

	public static String getSiteURL(HttpServletRequest request) {
		
		
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
		
	
		
	}
	
	public static JavaMailSenderImpl prepareJavaMailSender(EmailSettingBag settings) {
			
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
			mailSender.setHost(settings.getHost());
			mailSender.setPort(settings.getPort());
			mailSender.setUsername(settings.getUsername());
			mailSender.setPassword(settings.getPassword());
			
			Properties mailProperties = new Properties();
			mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
			mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());

			mailSender.setJavaMailProperties(mailProperties);
			
			return mailSender;
	}
	
	public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
			
			Object pricipal = request.getUserPrincipal();
			if(pricipal == null) {
				return null;
			}
			String customerEmail = null;
			
			if(pricipal instanceof UsernamePasswordAuthenticationToken || 
					pricipal instanceof RememberMeAuthenticationToken) {
				customerEmail = request.getUserPrincipal().getName();
			}else if(pricipal instanceof OAuth2AuthenticationToken){
				OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) pricipal;
				CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2AuthenticationToken.getPrincipal();
				customerEmail = oAuth2User.getEmail();
			}
			
			return customerEmail;
			
		}
	
	public static String formatCurrency(float amount,CurrencySettingBag settings) {
		
		String symbol = settings.getSymbol();
		String symbolPosition = settings.getSymbolPosition();
		String decimalPointType = settings.getDecimalPoinType();
		String thounsandPointType = settings.getThousandPoinType();
		int decimalDigits = settings.getDecimalDigits();
		
		String pattern = symbolPosition.equals("Before price") ? symbol : "";
		pattern += "###,###";
		
		if(decimalDigits > 0) {
			pattern += ".";
			for( int count = 1; count <= decimalDigits;count++) {
				pattern += "#";
			}
		}
		pattern += symbolPosition.equals("After price") ? symbol : "";
		
		char thounsandSeparator = thounsandPointType.equals("POINT") ? '.' : ',';
		char decimalSeparator = decimalPointType.equals("POINT") ? '.' : ',';
		
		DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
		decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
		decimalFormatSymbols.setGroupingSeparator(thounsandSeparator);
		
		DecimalFormat formatter = new DecimalFormat(pattern,decimalFormatSymbols);

		return formatter.format(amount);
	}
}
