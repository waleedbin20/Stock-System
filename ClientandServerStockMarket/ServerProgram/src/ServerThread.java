/*1805967*/
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerThread extends Thread {

    /* Creating Variables which will be used later*/
     public serverGUI cl_ob; public ServerSocket S_socket; public final int PORT = 12000;
     public int trader_Id; public ArrayList<Trader> List_Cl;


    /***********************************************
     **** constructor for the class ServerThread
     **** traderID will start from 0
     ***********************************************/

    public ServerThread(serverGUI object)
    {
        // creating a new array list to store values of trader
        List_Cl = new ArrayList<Trader>();
        cl_ob = object;
        trader_Id = 0; // initiating the start value of trader id
        try {
            /* new instance of server socket created to establish connection with the trader*/
            S_socket = new ServerSocket(PORT);
            System.out.println("Waiting for incoming clients...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /********************************************************************
     **** this run program allows the server to accept clients
     **** It assigns and ID and gives stock to first trader
     ********************************************************************/

    public void run() {
        while(true) {
            try{
                /* establishes connection with the client and prints the message on terminal*/
                Socket socket = S_socket.accept();
                // prints on terminal
                System.out.print(" *** Connection Successful with Trader *** \n" +
                                   "\n --- Server will store all the information on it's gui ---");

                // creating an object and passing the values inside it
                Trader thread = new Trader(socket,trader_Id,this);

                // when a trader joins ID keeps on incrementing +1
                trader_Id++;

                // checking list size
                if (List_Cl.size() == 0)
                    // when empty list, stock given as 1 to the trader who joined
                    thread.Stock_Number = 1;
                List_Cl.add(thread);
                thread.start();

                // calling two functions
                C_stockInMarket();
                trader_WithStock();

                // printing label on the server whenever a new trader joins the market
                cl_ob.printinglabel.setText("A New Trader with ID:" + (trader_Id - 1) + " has joined the market");
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    /********************************************************************
     **** this function is printing text on the gui for server
     ********************************************************************/
    public void C_stockInMarket(){
        StringBuilder printCommand = new StringBuilder();
        System.out.print(List_Cl.size() + "\n");
        // printing on terminal window, when trader joins
        for (Trader trader : List_Cl) printCommand.append("InMarketID:").append(trader.Client_ID).append("!");

        StringBuilder printCommand2 = new StringBuilder(" ");
        // checking list size and printing the label
        //printing the stock of the trader with its ID which will be shown to the traders gui
        for (Trader trader : List_Cl)
            printCommand2.append("InMarketID:").append(trader.Client_ID).append(" Stocks:").append(trader.Stock_Number).append("!");

        for (Trader trader : List_Cl) trader.printingOnTerminal(printCommand2.toString());

        // in the list the text will be separated using the "!" symbol.
        String[] dLabel = printCommand2.toString().split("!");
        cl_ob.l1.setListData(dLabel);

        // start value
        int x_id = 0;
        for(int i = 0;i < List_Cl.size();i++)
            // this will set the traderId
            if (List_Cl.get(i).Client_ID == trader_Id-1) {
                x_id = i;
                break;
            }
        cl_ob.l1.setSelectedIndex(x_id);
    }

    /********************************************************************
     **** This will print the stocks with the trader on the GUI Second
     **** column list in server GUI
     **** it calls the Ids and matches them
     ********************************************************************/

    public void trader_WithStock(){
        StringBuilder commds = new StringBuilder(" ");
        //looping through list and verifying the stocknumber wether greater than 0
        //trader cannot have a negative so it must be greater than that
        for (Trader interim : List_Cl) {
            // gets the stock from the array trader
            if (interim.Stock_Number > 0)
                // printing that on the server GUI
                commds.append("Trader ID:").append(interim.Client_ID).append("  Has Stock: ").append(interim.Stock_Number).append("!");
        }
        // in the list the text will be separated using the "!" symbol.
        String[] sString = commds.toString().split("!");
        cl_ob.l2.setListData(sString);

        // looping through the list
        int id_x = 0;
        for(int i = 0;i < List_Cl.size();i++)
            // this will set the traderId
            if (List_Cl.get(i).Client_ID == trader_Id-1) {
                id_x  = i;
                break;
            }
        // allowed selecting elements in the J List
        cl_ob.l2.setSelectedIndex(id_x);
    }
}
