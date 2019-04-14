//ChatServer.java

import java.io.*;

import java.net.*;

import java.util.ArrayList;
import java.util.HashSet;

public class ChatServer {


    //all users are unique by their name(primary key) so we will use hashset
    static HashSet<String> userNames = new HashSet<>();

    //for all users
    static ArrayList<PrintWriter> printWriters = new ArrayList<PrintWriter>();

    public static void main(String[] args) throws Exception{

        System.out.println("Waiting for clients...");

        //create server socket which will wait clients at the port no. 9800
        ServerSocket ss = new ServerSocket(9800);

        while (true) //waits unitl program stops

        {

            //accept the request of client and gets connected
            Socket soc = ss.accept();

            System.out.println("Connection established");

            ConversationHandler handler = new ConversationHandler(soc);

            //start a new thread
            handler.start();

        }

    }

}

class ConversationHandler extends Thread

{

    Socket socket;

    BufferedReader in;

    PrintWriter out;

    String name;

    //constructor
    public ConversationHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    //for every thread we need to override the run method which start the thread
    public void run()

    {

        try

        {

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream(), true);



            int count = 0;

            while (true)

            {
                //if it already present in the hashset count will be incremented
                if(count > 0)

                {

                    out.println("NAMEALREADYEXISTS");

                }

                else

                {

                    out.println("NAMEREQUIRED");

                }


                name = in.readLine();


                if (name == null)

                {

                    return;

                }


                //check the username is already present in the hashset or not
                if (!ChatServer.userNames.contains(name))

                {

                    ChatServer.userNames.add(name);

                    break;

                }

                count++;

            }


            out.println("NAMEACCEPTED"+name);

            ChatServer.printWriters.add(out);



            while (true)

            {

                String message = in.readLine();



                if (message == null)

                {

                    return;

                }

                //write to each client socket username and the message 
                for (PrintWriter writer : ChatServer.printWriters) {

                    writer.println(name + ": " + message);

                }

            }

        }

        catch (Exception e)

        {

            System.out.println(e);

        }

    }

}
