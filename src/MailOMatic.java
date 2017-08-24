/**
 * Created by jlawso on 06/11/2014.
 */

import net.atlanticbb.tantlinger.shef.HTMLEditorPane;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class MailOMatic
{
    //### variables ###
    //public static JEditorPane emailTextEditor;
    public static HTMLEditorPane emailTextEditor;
    public static ArrayList<String> columnTitles;
    public static ArrayList<CsvRecord> CsvList;
    public static String emailUsername;
    public static String emailPassword;
    public static String emailSubject;
    public static JFileChooser chooserAttach;
    public static ArrayList<File> AttachFiles = new ArrayList<File>();
    public static boolean select;
    public int[] emailStatus = new int[2];
    public final int success = 0;
    public final int failed = 1;
    public int test;


    private static JFrame window;
    private static JPanel tagMenu;
    private static JPanel SubjectMenu;
    private static JComboBox tagDropDown;
    private static JTextField addTag;
    private static JTextField SubjectTextBox;

    public static void main(String[] args) {
        //### main initializes program on startup ###
        makeGUI();
    }

    public MailOMatic(){
        //### needs to be here for jUnit tests to work ###
        makeGUI();
    }

    private static void makeGUI(){
        //### set up GUI window ###
        window = new JFrame("Mail-O-Matic");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(650,800));

        //### file menu ###
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem saveEmail = new JMenuItem("Save Email");
        JMenuItem openEmail = new JMenuItem("Open Email");
        JMenuItem openFile = new JMenuItem("Open Emailing List");
        JMenuItem closeWindow = new JMenuItem("Exit");

        //action listeners for each file menu item
        openFile.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){openCSVFile(); }});
        saveEmail.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){SaveEmail(); }});
        openEmail.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){OpenEmail(); }});
        closeWindow.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e)
        {window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING)); }});

        //add menu options to file menu
        fileMenu.add(saveEmail);
        fileMenu.add(openEmail);
        fileMenu.add(openFile);
        fileMenu.add(closeWindow);

        //create config file menu
        JMenu configMenu = new JMenu("Configure");
        JMenuItem email = new JMenuItem("Email Settings");

        //add action listener to config email settings
        email.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddEmailSettings();
            }
        });

        //add to config menu
        configMenu.add(email);
        //add fle menu and config menu
        menuBar.add(fileMenu);
        menuBar.add(configMenu);

        //### set up text area ###
        emailTextEditor = new HTMLEditorPane();


        //un-highlights text on focus
        emailTextEditor.addFocusListener(new FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        emailTextEditor.setCaretPosition(emailTextEditor.getCaretPosition());
                    }
                });
            }
        });

        //### window layout ###
        JPanel menus = new JPanel(new BorderLayout());//new GridLayout(2,1));
        //
        tagMenu = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton previewButton = new JButton("Send/Preview Email");
        previewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                genEmailPreview();
            }
        });

        JButton attachButton = new JButton("Attach File");
        attachButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                chooserAttach = new JFileChooser();
                chooserAttach.setMultiSelectionEnabled(true);
                int option = chooserAttach.showOpenDialog(MailOMatic.addTag);
                if (option == JFileChooser.APPROVE_OPTION)
                {
                   File[] temp;
                   temp=(chooserAttach.getSelectedFiles());
                   for(File tp : temp)AttachFiles.add(tp);

                   select = true;
                }
            }
        });

        tagDropDown = new JComboBox();
        tagDropDown.addItem("");
        JLabel selectTagLabel = new JLabel("Select a token:");
        JLabel addTagLabel = new JLabel("Drag token:");
        addTag = new JTextField();
        addTag.setDragEnabled(true);
        addTag.setEditable(false);

        //when drop down changes update tag textbox
        tagDropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getUserDataTag();
            }
        });

        //auto selects all text on focus
        addTag.addFocusListener(new FocusAdapter(){
            public void focusGained(java.awt.event.FocusEvent evt) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        addTag.selectAll();
                    }
                });
            }
        });

        tagMenu.add(previewButton);
        tagMenu.add(attachButton);
        tagMenu.add(selectTagLabel);
        tagMenu.add(tagDropDown);
        tagMenu.add(addTagLabel);


        //add to GUI
        tagMenu.add(addTag);

        //add subject to email
        SubjectMenu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel SubjectText = new JLabel("Subject:");
        SubjectTextBox = new JTextField(35);
        SubjectMenu.add(SubjectText);
        SubjectMenu.add(SubjectTextBox);

        //add to menus panel
        menus.add(menuBar, BorderLayout.NORTH);
        menus.add(tagMenu, BorderLayout.CENTER);
        menus.add(SubjectMenu, BorderLayout.SOUTH);

        //### add component containers to window ###
        window.getContentPane().add(menus, BorderLayout.NORTH);
        window.getContentPane().add(emailTextEditor, BorderLayout.CENTER);

        //### show window ###
        window.pack();
        window.setVisible(true);
    }

    private static void openCSVFile(){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        chooser.setMultiSelectionEnabled(false);

        File tempCSVFile = null;

        int returnVal = chooser.showOpenDialog(window);
        if(returnVal == JFileChooser.APPROVE_OPTION)
        {
            //choose selected file
            tempCSVFile = chooser.getSelectedFile();
            //create csv file reader
            CsvFileReader fileReader = new CsvFileReader();
            //load contents in to list
            CsvList = fileReader.generateList(tempCSVFile);
            //fetch column titles
            columnTitles = fileReader.getTitles();
            //generate buttons to add to text area
            genDropDownList(columnTitles);
        }
    }

    private static void SaveEmail(){

        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            chooser.setDialogTitle("Save File");
            //chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = chooser.showSaveDialog(window);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String directory = chooser.getCurrentDirectory().getAbsolutePath();
                String filename = chooser.getSelectedFile().getName();
                PrintWriter writter = new PrintWriter(new File( directory + filename));
                String messageText = emailTextEditor.getText();

                writter.write(messageText);
                writter.close();
            }
        }
        catch (IOException e)
        {
            System.out.println("Unable to save the file. Try a different directory.");
        }


    }
    private static void OpenEmail(){

    try {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        chooser.setFileFilter(new FileNameExtensionFilter("HTML Files", "html"));
        chooser.setDialogTitle("Open File");
        //chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(window);
        File tempHTMLfile = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            tempHTMLfile = chooser.getSelectedFile();
            BufferedReader reader = new BufferedReader(new FileReader(tempHTMLfile));
            String line;
            String outline="";
            while((line = reader.readLine())!=null) outline+=line;
            emailTextEditor.setText(outline);
        }
    }
    catch (IOException e)
    {
        System.out.println("Unable to save the file. Try a different directory.");
    }


}
    public static String getEmailText(){
        return emailTextEditor.getText();
    }

    public static void setEmailText(String text){
        emailTextEditor.setText(text);
    }

    private static void genDropDownList(ArrayList<String> columnTitles){
        //### populate drop down box ###

        //gen drop down list
        tagDropDown.removeAllItems();

        for(int i=0, l=columnTitles.size(); i<l; i++){
            tagDropDown.addItem(columnTitles.get(i));
        }

        //initialize first tag
        getUserDataTag();

        RepaintWindow();
    }

    private static void AddEmailSettings(){
        //### popup window to input email settings ###
        final JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        username.setText(emailUsername);
        final Object[] settingsMessage = {
                "Essex Username:", username,
                "Password:", password
            };

        JOptionPane pane = new JOptionPane(settingsMessage, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = pane.createDialog("Email Login Details");

        dialog.addComponentListener(new ComponentListener(){
            @Override
            public void componentShown(ComponentEvent e) {
                username.requestFocusInWindow();
            }
            @Override public void componentHidden(ComponentEvent e) {}
            @Override public void componentResized(ComponentEvent e) {}
            @Override public void componentMoved(ComponentEvent e) {}
        });
        dialog.setVisible(true);

        if ((Integer)pane.getValue() == JOptionPane.YES_OPTION) {
            emailUsername = username.getText();
            char[] temp = password.getPassword();
            emailPassword = "";

            for (int i = 0; i < temp.length; i++) {
                emailPassword += temp[i];
            }
        }
    }

    private static void getUserDataTag(){
        //### update text box on tag drop down box change ###
        addTag.setText("$$" + tagDropDown.getSelectedItem() + "$$");
        addTag.selectAll();
        RepaintWindow();
    }

    private static void RepaintWindow(){
        //### repaint to show the buttons ###
        window.validate();
        window.repaint();
    }

    public static String getEmailSubject(){
        return SubjectTextBox.getText();
    }

    private static void genEmailPreview(){
        //### set up GUI window ###
        JFrame previewWindow = new JFrame("Email Preview");
        previewWindow.setPreferredSize(new Dimension(650,800));


        MessageTokenizer Send = new MessageTokenizer();

        HTMLEditorPane previewEmailTextEditor = new HTMLEditorPane();
        previewEmailTextEditor.setEditable(false);
        previewEmailTextEditor.removebar(previewEmailTextEditor);
        emailSubject = getEmailSubject();
        Send.setPassUserAndSubject(emailPassword,emailUsername+"@essex.ac.uk", emailSubject);
        Send.setMessage(getEmailText()+" ");
        Send.SendEmailsToEveryone(CsvList, false);
        previewEmailTextEditor.setText(Send.getPreview());

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                MessageTokenizer Send1 = new MessageTokenizer();
                emailSubject = getEmailSubject();
                Send1.setPassUserAndSubject(emailPassword,emailUsername+"@essex.ac.uk", emailSubject);
                Send1.setMessage(getEmailText()+" ");
                Send1.SendEmailsToEveryone(CsvList, true);
            }
        });

        JLabel SubjectText = new JLabel("Subject:");
        JTextField previewSubjectTextBox = new JTextField(35);
        previewSubjectTextBox.setEditable(false);
        previewSubjectTextBox.setText(SubjectTextBox.getText());

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        menu.add(sendButton);
        menu.add(SubjectText);
        menu.add(previewSubjectTextBox);

        previewWindow.getContentPane().add(menu, BorderLayout.NORTH);
        previewWindow.getContentPane().add(previewEmailTextEditor, BorderLayout.CENTER);

        previewWindow.pack();
        previewWindow.setVisible(true);
    }
}
