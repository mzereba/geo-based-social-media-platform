package com.streamingvideoservice.ws;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendGMail {

    private static String USER_NAME = "geobased.dashboard";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "dsqcri2014"; // GMail password
    private static String RECIPIENT = "mzereba@qf.org.qa";

    public static void main(String[] args) {
        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = { RECIPIENT }; // list of recipient email addresses
        String subject = "Hotspots Area Detected";
        String body = "Geo-based Social Media Dashboard Administrator(s),\n\n";
        body += "A new search in the Geo-based Social Media Dashboard has been performed, more than 50 videos in the area have been detected.\n\n";
        body += "Location and details of the search is described below.\n\n";
        body +=  "Latitude: " + "30.0444° N" + "\n";
        body +=  "Longitude: " + "31.2357° E" + "\n\n";
        body +=  "URL: " + "http://maps.google.com/?q=Tahrir%20Square@30.0444,31.2357\n\n";

        sendFromGMail(from, pass, to, subject, body);
    }

    private static void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}