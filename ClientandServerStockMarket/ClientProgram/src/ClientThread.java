/* 1805967*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientThread extends Thread{

    /* Initialising variables */
    // Buffer Reader --> input = Commands will be coming in from clients
    // Print Reader --> output = Commands will be going out to clients

    public Socket Socket_cl; public int Stock_Number;
    private PrintWriter out; private BufferedReader in;  private boolean Socket_State;

    public ClientProgram cl_tra;

    // assigning a port number and IP address
    public final int PORT = 12000;
    public String localhost = "127.0.0.1";

    /***********************************************
     **** constructor for the class ClientThread
     **********************************************/

    public ClientThread(ClientProgram object){
        cl_tra = object;
        Stock_Number = 0;
    }

    /****************************************************************
     ****  function which checks socket state if it is connected
     ****  It displays appropriate messages on the terminal
     ****  Calls the function which will only show the ID of trader
     ****  Once a connection to server is setup successfully
     ***************************************************************/
   @Override
    public void run(){
        while(Socket_State)
        {
            try{
                if (!Socket_cl.isConnected())
                    return;
                String str = in.readLine();
                // calling function to print string messages for the trader
                TraderStingsToBePrinted(str);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        // otherwise socket close if something went wrong with the connection
        try{
            Socket_cl.close();
        }
        catch (Exception e){
            System.out.println(e);

        }
    }

    /*************************************************
     **** Allowing clients to disconnect from server
     *************************************************/
    public void DCn_Client(){
        Socket_State = false;
        try{
            // when the trader presses disconnect them this function is executed
            out.println("Closing Socket:");
            Socket_cl.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /***************************************************************************
     ****  Method for implementing two way connecting i.e Client with server
     ****  Connecting to the server and creating objects for communications
     ***************************************************************************/
    public boolean Cn_Client(){

        boolean connectionState;
        try{

            // new socket being created with the ip address and the assigned port number
            Socket_cl = new Socket(localhost,PORT);
            in = new BufferedReader(new InputStreamReader(Socket_cl.getInputStream()));
            out = new PrintWriter(Socket_cl.getOutputStream(), true);
            Socket_State= true;
            connectionState = true;
        }

        // throws error if the connection fails
        catch (Exception e){
            e.printStackTrace();
            connectionState = false;
        }
        return connectionState;
    }

    /***********************************************
     **** Printing messages on terminal window
     **** whatever the user does on GUI on the
     **** backend it gets updated on terminal
     ***********************************************/
    public boolean outputToTerminal(String command){
        if (Socket_cl.isConnected()) {
            out.println(command);
        }
        else
            return false;
        return true;
    }

    /***********************************************
     **** Printing messages for the client
     ***********************************************/

    public void TraderStingsToBePrinted(String sendmessage){
        if (sendmessage.contains("YourID:")){
            int ID = 0;
            for(int i = 0;i < sendmessage.length() - 7;i++)
                // ID is being assigned to the trader
                ID = ID * 10 + Character.getNumericValue(sendmessage.charAt(i + 7));

            /* Assigning the ID's on the GUI labels of the client program
             * Using "!" to split string
             * Calling object and it variables */
            cl_tra.Client_MyId.setText("TraderID: " + ID);
            cl_tra.Client_ID = ID;
            cl_tra.detail_m =  cl_tra.detail_m + "MYID: " + ID + "!";
            cl_tra.l1.setListData( cl_tra.detail_m.split("!"));

            System.out.print("\n*** You're connection has been Successful with Server *** " +
                            "\n\n --- Now you can start using the GUI ---");
            return;
        }

        // it will show message on column 2 for the client GUI
        if (sendmessage.contains("InMarketID:")){
            String[] stringsMessagess = sendmessage.split("!");
            cl_tra.l2.setListData(stringsMessagess);
            cl_tra.l2.setSelectedIndex(stringsMessagess.length - 1);

            // this returns the unicode value of the specified string
            for(int i = 0;i < stringsMessagess.length;i++)
            {
                String[] sri = stringsMessagess[i].split(" ");
                int id = 0;
                //getting id
                for(int p = 0;p < sri[0].length() - 11;p++)
                    id = id * 10 + Character.getNumericValue(sri[0].charAt(p + 11));
                Character.getNumericValue(stringsMessagess[i].charAt(12));

                // selecting which trader to give stock to from the list 2
                if (id ==  cl_tra.Client_ID)
                    Stock_Number = Character.getNumericValue(sri[1].charAt(7));
            }
            return;
        }
        /* This condition will print details of the stock when it is being received --
         * For example: When a trader is about to receive a stock from another trader,
         * which is verified by the server then
         * this condition will hereby allow that to be printed on the Jlist of the Target Trader ID*/
        if (sendmessage.contains("Receive")){
            cl_tra.detail_m =  cl_tra.detail_m + sendmessage;
            cl_tra.l1.setListData( cl_tra.detail_m.split("!"));
        }
    }
}
