import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class connection
{
    public static String to, from, pass,username,subject,message;

    // there are two states for an attempted email send. it either failed or was successful
    // this array keeps count of the failures and the successful sends.
    public static int[] emailStatus = new int[2];
   public static final int success = 0;
   public static final int failed = 1;

    /**
     * method to set the recipient email
     */
    public void setRecipient(String Re)
    {
        to = Re;
    }

    /**
     * method to set the sender email
     */
    public void setSender(String Se)
    {
        from = Se;
    }

    /**
     * method to set the Sender password
     */
    public void setPassword(String Ps)
    {
        pass = Ps;
    }

    /**
     * method to set the email subject
     */
    public void setSubject(String sub)
    {
        subject = sub;
    }

    /**
     * method to set the message that is going to be send
     */
    public void setMessage(String Me)
    {
        message = Me;
    }

    /**
     * method which returns the recipient
     */
    public static String recipient()
    {
        return to;
    }

    /**
     *  method which returns the sender username
     */
    public static String SenderUsername()
    {
        username = from.substring(0,from.indexOf("@"));
        return username;
    }

    /**
     * method which returns the recipient
     */
    public static String SenderEmail()
    {
        return from;
    }

    /**
     * method which returns the password
     */
    public static String pass()
    {
        return pass;
    }


    /**
     * method which creates the sending session
     */
    public static Session properties()
    {
        final String senderEml = SenderEmail();
        final String senderUsrname = SenderUsername();
        final String password = pass();

        String conn = "true";
        Properties properties = System.getProperties();
        properties.put("mail.smtps.host", "smtp.essex.ac.uk");
        properties.put("mail.smtps.port", "465");
        properties.put("mail.smtps.auth", conn);
        properties.put("mail.smtps.starttls.enable", conn);
        properties.put("mail.smtps.ssl.enable", conn);
        properties.setProperty("mail.user", senderUsrname);
        properties.setProperty("mail.password", password);

        Session session = Session.getInstance(properties);
        return(session);
    }

    /**
     * method responsible for the email transportation
     */
    public static void send(Session sesh){

        // an email either failed to send, or was successful

        final int success = 0;
        final int failed = 1;

        try
        {
            Session session = sesh;


            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(SenderEmail()));
            msg.addRecipient(MimeMessage.RecipientType.TO,
                    new InternetAddress(recipient()));
            msg.setSubject(subject);

            BodyPart msgBodyPart = new MimeBodyPart();

            msgBodyPart.setContent(message, "text/html; charset=utf-8");
            //msg.setText(message);
            msg.setContent(message, "text/html; charset=utf-8");
            MimeMultipart multipart=new MimeMultipart();
            multipart.addBodyPart(msgBodyPart);

            if(MailOMatic.select==true)
            {
                // attachment part
                for(int i = 0;i < MailOMatic.AttachFiles.size();i++)
                {
                    msgBodyPart = new MimeBodyPart();
                    String FilePath = "" + MailOMatic.AttachFiles.get(i);
                    String FileName = MailOMatic.AttachFiles.get(i).getName();
                    DataSource Dsr = new FileDataSource(FilePath);
                    msgBodyPart.setDataHandler(new DataHandler(Dsr));
                    msgBodyPart.setFileName(FileName);
                    multipart.addBodyPart(msgBodyPart);
                    msg.setContent(multipart);

                }
            }

            Transport trns = session.getTransport("smtps");
            trns.connect(null, sesh.getProperty("mail.password"));
            msg.saveChanges();
            trns.sendMessage(msg, msg.getAllRecipients());
            trns.close();
            emailStatus[success] += 1;


        }
        catch (MessagingException e)
        {
            emailStatus[failed] += 1;
        }
    }
}
