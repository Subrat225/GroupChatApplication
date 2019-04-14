//Client program

import javax.swing.*;

import java.awt.FlowLayout;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.io.*;

import java.net.Socket;

public class ChatClient {


    //to create user interface
    static JFrame chatWindow = new JFrame("Chat Application");

    static JTextArea chatArea = new JTextArea(22, 40);

    static JTextField textField = new JTextField(40);

    static JLabel blankLabel = new JLabel("           ");

    static JButton sendButton = new JButton("Send");

    static BufferedReader in;

    static PrintWriter out;

    static JLabel nameLabel = new JLabel("         ");


    ChatClient()

    {

        chatWindow.setLayout(new FlowLayout());

        chatWindow.add(nameLabel);

        chatWindow.add(new JScrollPane(chatArea));

        chatWindow.add(blankLabel);

        chatWindow.add(textField);

        chatWindow.add(sendButton);

        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatWindow.setSize(475, 500);

        chatWindow.setVisible(true);


        //before connection we can't do anything
        textField.setEditable(false);

        chatArea.setEditable(false);


        //adding functionality to send button and text field
        sendButton.addActionListener(new Listener());

        textField.addActionListener(new Listener());

    }





    void startChat() throws Exception

    {
        //to connect to server we need to give server ip address
        String ipAddress = JOptionPane.showInputDialog(

                chatWindow,

                "Enter IP Address:",

                "IP Address Required!!",

                JOptionPane.PLAIN_MESSAGE);


        //client should connect to same port number where server is running
        Socket soc = new Socket(ipAddress, 9800);

        in = new BufferedReader(new InputStreamReader(soc.getInputStream()));

        out = new PrintWriter(soc.getOutputStream(), true);

        while (true)

        {

            String str = in.readLine();

            if (str.equals("NAMEREQUIRED"))

            {
                //pop up for enter unique username
                String name = JOptionPane.showInputDialog(

                        chatWindow,

                        "Enter a unique name:",

                        "Name Required!!",

                        JOptionPane.PLAIN_MESSAGE);



                out.println(name);



            }
            //if server send name already exist then another popup for reenter name
            else if(str.equals("NAMEALREADYEXISTS"))

            {

                String name = JOptionPane.showInputDialog(

                        chatWindow,

                        "Enter another name:",

                        "Name Already Exits!!",

                        JOptionPane.WARNING_MESSAGE);



                out.println(name);

            }

            //successfully accepted and name will be showed in top
            else if (str.startsWith("NAMEACCEPTED"))

            {

                textField.setEditable(true);

                nameLabel.setText("You are logged in as: "+str.substring(12));



            }
            //if someone send a message append to chat area(message area)
            else

            {

                chatArea.append(str + "\n");

            }

        }

    }



    public static void main(String[] args) throws Exception {

        ChatClient client = new ChatClient();

        client.startChat();

    }

}

class Listener implements ActionListener

{

    @Override

    public void actionPerformed(ActionEvent e) {

        ChatClient.out.println(ChatClient.textField.getText());

        //after sending message textfield will be empty
        ChatClient.textField.setText("");

    }

}

