import org.junit.Test;

import javax.mail.Session;

import static org.junit.Assert.assertTrue;

public class connectionTest {

    @Test
    public void testRecipient() throws Exception
    {
        connection test = new connection();
        test.setRecipient("ctsits@essex.ac.uk");
        assertTrue(test.recipient().equals("ctsits@essex.ac.uk"));
    }

    @Test
    public void testSenderUsername() throws Exception
    {
        connection test = new connection();
        test.setSender("mpante@essex.ac.uk");
        assertTrue(test.SenderUsername().equals("mpante"));
    }

    @Test
         public void TestProperties()
    {
        connection connTest = new connection();

        Session test;

        connTest.setSender("mpante@essex.ac.uk");
        connTest.setPassword("1234");
        test = connTest.properties();
        assertTrue(test.getProperty("mail.smtps.port").equals("465"));
        assertTrue(test.getProperty("mail.smtps.auth").equals("true"));
        assertTrue(test.getProperty("mail.smtps.host").equals("smtp.essex.ac.uk"));
        assertTrue(test.getProperty("mail.smtps.starttls.enable").equals("true"));
        assertTrue(test.getProperty("mail.smtps.ssl.enable").equals("true"));
        assertTrue(test.getProperty("mail.user").equals("mpante"));
        assertTrue(test.getProperty("mail.password").equals("1234"));
    }

    @Test
    public void testSenderEmail() throws Exception
    {
        connection test = new connection();
        test.setSender("mpante@essex.ac.uk");
        assertTrue(test.SenderEmail().equals("mpante@essex.ac.uk"));
    }

    @Test
    public void testPass() throws Exception
    {
        connection test = new connection();
        test.setPassword("1234");
        assertTrue(test.pass().equals("1234"));
    }


    @Test
    public void testSetSubject() throws Exception {
        connection test = new connection();
        test.setSubject("me");
        String testString = test.subject;
        assertTrue(testString.equals("me"));

    }

    @Test
    public void testSetMessage() throws Exception {
        connection test = new connection();
        test.setMessage("me");
        String testString = test.message;
        assertTrue(testString.equals("me"));

    }

}