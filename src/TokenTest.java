import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testGetIndex() throws Exception {
        Token tok = new Token(20,"title");
        assertTrue(tok.getIndex()==20);
    }

    @Test
    public void testGetTitle() throws Exception {
        Token tok = new Token(20,"title");
        assertTrue(tok.getTitle().equals("title"));
    }

    @Test
    public void testToString() throws Exception {
        Token tok = new Token(20,"title");
        assertTrue(tok.toString().equals("[20, title]"));
    }
}