import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by jadamsc,tcotti and rframp on 18/11/2014.
 */
// $$EXAMPLE$$ is a token
public class MessageTokenizer {


    //    String message = "Thank you $$name$$ for submitting a file using the FASER service. This email is your receipt of submission, you should keep this email.\n" +
//            "\n" +
//            "It is advisable to double check that $$name$$ submitted file is in a format that can be accessed by your department.\n" +
//            "Files submitted in incorrect file formats, password protected files, etc. may not be accepted.";
    String subject,tempmessage, message, templateemail, password, username = null;

    public void setMessage(String m) {
        message = m;


    }

    public String getPreview() {
        return tempmessage;
    }

    public void setPassUserAndSubject(String pass, String user, String sub) {
        password = pass;
        username = user;
        subject = sub;
    }

    public void SendEmailsToEveryone(ArrayList<CsvRecord> csv, Boolean maybeSend) {
        CsvFileReader fileReader = new CsvFileReader();


        ArrayList<CsvField> fields;
        ArrayList<Token> indexes; //tokenizer method call 2

        indexes = getTokens(message);
        int indx = 0;
        String token = null;
        String RecipientEmail = null;


        connection conn = new connection();
        conn.setSender(username);
        conn.setPassword(password);
        conn.setSubject(subject);


//        for (CsvRecord record : csv) //LOOP Through array containing records
//        {
//            templateemail = message;
//            fields = record.getList();
//            for (CsvField field : fields) //LOOP Through fields of Record
//            {
//                if (field.getName().equalsIgnoreCase("email"))
//                    RecipientEmail = field.getValue();
//                break;
//            }

            try {


                for (CsvRecord record : csv) //LOOP Through array containing records
                {
                    templateemail = message;
                    fields = record.getList();
                    for (CsvField field : fields) //LOOP Through fields of Record
                    {
                        if (field.getName().equalsIgnoreCase("email"))
                            RecipientEmail = field.getValue();
                        break;
                    }

                    for (Token obj : indexes) //LOOP Through list of index numbers + Titles
                    {

                        indx = obj.getIndex();

                        for (CsvField field : fields) //LOOP Through fields of Record
                        {

                            if ((obj.getTitle()).equals(field.getName())) {
                                token = field.getValue();
                                message = message.substring(0, indx) + token
                                        + message.substring(indx);
                                tempmessage=message;
                            }
                        }


                    }

                    if (maybeSend) {
                        conn.setRecipient(RecipientEmail);
                        conn.setMessage(message);
                        conn.send(conn.properties());
                    }else break;
                    message = templateemail;
                }


             // reset the successful and unsuccessful emails sent, for if the user composes a new email


                if (maybeSend) {

                    // All emails have been attempted to be sent when the loop above has finished. Now we tell the user
                    // whether the emails were successful or not!

                    // some were successfully sent, some were unsuccessfully sent
                    if (conn.emailStatus[conn.success] > 0 && conn.emailStatus[conn.failed] > 0) {
                        JOptionPane.showMessageDialog(null, conn.emailStatus[conn.success] +
                                " emails successfully sent" + ", " + conn.emailStatus[conn.failed] +
                                " emails failed to send. Please check the details in the CSV file are correct.");
                    }

                    // none were successful
                    else if (conn.emailStatus[conn.success] == 0 && conn.emailStatus[conn.failed] > 0) {
                        JOptionPane.showMessageDialog(null, conn.emailStatus[conn.success] +
                                " emails successfully sent" + ", " + conn.emailStatus[conn.failed] +
                                " emails failed to send. Are you logged in correctly?");
                    }

                    // all were successful
                    else {
                        JOptionPane.showMessageDialog(null, conn.emailStatus[conn.success] +
                                " emails successfully sent");
                    }
                }

                // reset the successful and unsuccessful emails sent, for if the user composes a new email
                conn.emailStatus[conn.success] = 0;
                conn.emailStatus[conn.failed] = 0;

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "No CSV file present. Please select a file.");
            }


        }
  //  }
        public ArrayList<Token> getTokens (String msg)
        {
            Pattern p = Pattern.compile("\\$\\$[A-Za-z0-9_]+\\$\\$");
            Matcher m = p.matcher(msg);

            ArrayList<Token> tokens = new ArrayList<Token>();


            while (m.find()) {

                int newIndex = m.start();
                String newName = m.group().substring(2, m.group().length() - 2);
                Token tok = new Token(newIndex, newName);
                tokens.add(tok);
                msg = m.replaceFirst("");
                m = p.matcher(msg);
            }

            message = msg;


            Collections.reverse(tokens);
            return tokens;
        }

//fourth time trying to send this to $$Title$$ $$Surname$$ $$Email$$

}

