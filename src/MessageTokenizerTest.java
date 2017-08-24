import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static junit.framework.Assert.assertTrue;

public class MessageTokenizerTest {

    @Test
    public void testSetMessage() throws Exception {
        MessageTokenizer tokenzer = new MessageTokenizer();
        tokenzer.setMessage("This is the message.");
        assertTrue(tokenzer.message.equals("This is the message."));
    }

    @Test
    public void testSetPassAndUser() throws Exception {
        MessageTokenizer tokenzer = new MessageTokenizer();
        tokenzer.setPassUserAndSubject("password", "Marios", "Subject");
        assertTrue(tokenzer.password.equals("password"));
        assertTrue(tokenzer.username.equals("Marios"));
        assertTrue(tokenzer.subject.equals("Subject"));


    }

    @Test
    public void testGetTokens() throws Exception {
        MessageTokenizer tokenzer = new MessageTokenizer();
        ArrayList<Token> tokens = new ArrayList<Token>();
        String message = "This email is sent to $$Title$$ $$Name$$";
        tokens = tokenzer.getTokens(message);

        ArrayList<Token> testlist = new ArrayList<Token>();
        testlist.add(new Token(22,"Title"));
        testlist.add(new Token(23,"Name"));
        Collections.reverse(testlist);

        assertTrue((tokens.get(0).getIndex())==(testlist.get(0).getIndex()));
        assertTrue(tokens.get(0).getTitle().equals(testlist.get(0).getTitle()));

        assertTrue((tokens.get(1).getIndex())==(testlist.get(1).getIndex()));
        assertTrue(tokens.get(1).getTitle().equals(testlist.get(1).getTitle()));


    }
}