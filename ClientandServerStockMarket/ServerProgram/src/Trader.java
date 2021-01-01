/*1805967*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Trader extends Thread{

    // Output = Commands going out to the clients
    private BufferedReader in; private PrintWriter out;
    ServerThread server_ob; Socket market_Trader;
    int Client_ID; int Stock_Number;
    boolean State_market;


    /******************************************************************************************
     ****         This is a constructor created for the Trader with three parameters
     ******************************************************************************************/

    public Trader(Socket socket,int id,ServerThread object){
        this.Client_ID = id;
        this.server_ob = object;
        // assigning stock
        Stock_Number = 0;
        // initiating markets state at beginning
        State_market = true;
        try {
            market_Trader = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String PrintingTraderID = "YourID:" + Client_ID;
            printingOnTerminal(PrintingTraderID);
            System.out.println("\n Trader with ID(" + Client_ID + ") has connected");
            
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /******************************************************************************************
     ****      This checks if the value is true then it will process a string on terminal
     ******************************************************************************************/

    public void run(){
        while(State_market){
            try{
                //read line from console
                String mob = in.readLine();
                System.out.print(mob + "\n");
                messageToBePrinted(mob);

            }catch (Exception e){
                CS_state();
                System.out.println("TraderID" + Client_ID + "has left the market(i.e Quit GUI)");
                e.printStackTrace();
                return;
            }
        }
    }

    /******************************************************************************************
     ****      This function keeps updating whenever trader leaves the market,
     ****      It checks socket state
     ****      Then it waits for them to connect
     ******************************************************************************************/

    public void CS_state(){
        // when trader has quit or got disconnected then this will be printed on the server gui
        server_ob.cl_ob.printinglabel.setText("Trader ID("+ Client_ID + ") has left the market");

        int ind_id = -1;
        // looping through object.method and checking the list size
        for(int i = 0;i < server_ob.List_Cl.size();i++) {
            // declaring the trader ID
            if (Client_ID == server_ob.List_Cl.get(i).Client_ID)
                ind_id = i;
        }
        if (ind_id >= 0)
        {
            try{
                /* Calling server thread object*/
                int newind_id = 0;
                if (ind_id != server_ob.List_Cl.size() - 1)
                    newind_id  = server_ob.List_Cl.size() - 1;

                // removing the trader id
                server_ob.List_Cl.get(newind_id ).Stock_Number += server_ob.List_Cl.get(ind_id).Stock_Number ;
                server_ob.List_Cl.remove(ind_id);

                /* Server thread methods called*/
                server_ob.C_stockInMarket();
                server_ob.trader_WithStock();
                market_Trader.close();

                /* when market connection lost then it will wait for traders to rejoin*/
                State_market = false;
                if (server_ob.List_Cl.size() == 0)
                    server_ob.cl_ob.printinglabel.setText("Waiting for traders to join the market... ");
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }
    }

    /******************************************************************************************
     ****     if trader is connected then the server will send a message in terminal
     ****     it will assign the ID of the trader who joined
     ******************************************************************************************/

    public void printingOnTerminal(String commands){
        if (market_Trader.isConnected()) {
            // outputting message
            out.println(commands);
        }
    }

    /**************************************************************************************************
     ****     This function is for processing messages on the GUI for the Trader and Server
     ****     When a trader trades a stock the server will update by displaying a label for that
     ****     It will also show in the GUI for trader in First column -- showing him market details
     ****     on what he was doing in the market, receiving and trading stocks.
     **************************************************************************************************/

    public void messageToBePrinted(String comm){
        // initialising to integer variable source and target
        int sd,td;

        // checking wether the two words are there
        if (comm.contains("YourID:") && comm.contains("TargetID:")){
            int source_ID = 0;
            int target_ID = 0;
            // split then them with a space
            String[] cod = comm.split(" ");

            //this loop will convert the unicode to int value
            for(int i = 0;i < cod[0].length() - 7;i++)
                // assigns source
                source_ID = source_ID * 10 + Character.getNumericValue(cod[0].charAt(i + 7));

            //this loop assign the target id -- then server will know which traders are trading stock
            for(int i = 0;i < cod[1].length() - 9;i++)
                target_ID = target_ID * 10 + Character.getNumericValue(cod[1].charAt(i + 9));

            // initiating value
            sd= -1;
            td = -1;
            /* calling and assigning the target id and source ID*/
            for(int i = 0;i < server_ob.List_Cl.size();i++)
            {
                // source
                if (server_ob.List_Cl.get(i).Client_ID == source_ID)
                    sd = i;
                // target
                if (server_ob.List_Cl.get(i).Client_ID == target_ID)
                    td = i;
            }
            try{
                // when the client is connected
                // so in short scenario this condition is for displaying text on server when a trader gives stock to other trader
                // targetID increment positive while source ID will decrement since it is giving its stock
                if (server_ob.List_Cl.get(td).market_Trader.isConnected()){
                    server_ob.List_Cl.get(td).Stock_Number++;
                    server_ob.List_Cl.get(sd).Stock_Number--;

                    //checking weather two ID's are equal if its equal meaning he keeps stock with himself otherwise has given stock
                    if (sd == td)
                        server_ob.List_Cl.get(sd).Stock_Number++;
                    server_ob.cl_ob.printinglabel.setText("Trader ("+ source_ID + ") has traded stock with Trader (" + target_ID +")");

                    //checking weather two ID's are not equal and then when a trader sends stock to other trader it will display
                    //on the receiving traders GUI a text message
                    if (sd != td)
                    {
                        String history = "Received stock from Trader(" + source_ID +")"+ "!";
                        server_ob.List_Cl.get(td).out.println(history);
                    }
                }
                // calling methods from the server thread
                server_ob.C_stockInMarket();
                server_ob.trader_WithStock();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        // when the trader has disconnected it calls the function Client socket state
        // and displays the message trader has left the market
        if (comm.contains("Lost Connection with Client")){
            CS_state();
        }
    }
}
