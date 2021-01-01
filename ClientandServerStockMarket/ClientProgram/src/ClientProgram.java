/* 1805967*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ClientProgram implements ActionListener {

    // Assigning variable to thread
    public ClientThread market_Trader;
    public String detail_m; public int Client_ID;


    /* Initialising Variables for the ClientGUI*/
    public JFrame frame; public JPanel panel, pl0, pl1,pl2,pl3,pl4; public JLabel label_executeCommand;
    public JLabel Client_MyId, he_1,he_2;
    public BoxLayout design1;
    public JButton c_N, g_S; public JScrollPane sc_bar1,sc_bar2;
    public JList<String> l1; public JList<String> l2;


    /****************************************************************************
     ****       On execution this will execute the client GUI
     ****************************************************************************/

    public void start(){
        // initially the ID is set to -1 becasue we will use it for if conditions. Client ID will start from 0 ownwards
        Client_ID= -1;
        detail_m = new String("");
        ConTo_Server();
        runClientGUI();

    }

    /****************************************************************************
     ****       This function designs the client GUI
     ****       Frame has one main panel and that panel has sub panels for
     ****       locating objects on the frame
     ****       Buttons Created
     ****       Two list Columns Created
     ****************************************************************************/

    public void runClientGUI(){
        /* main panel which is added to frame -- this panel will have multiple panels */
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(700, 500));
        panel.setBackground(Color.getHSBColor(231,33,47));

        /* Designing panel which will locate the object on the main panel
         * setting the background color for the panel 0 */
        pl0 = new JPanel();
        pl0.setBackground(Color.getHSBColor(231,33,47));

        /* panel for locating the first column and used a list- PANEL 1 */
        pl1 = new JPanel();
        pl1.setBackground(Color.getHSBColor(160,160,160));

        Client_MyId = new JLabel("Empty");
        pl0 = new JPanel();
        pl0.add(Client_MyId);

        /* panel for locating the execute label command - PANEL 2 */
        pl2 = new JPanel();
        pl2.setBackground(Color.getHSBColor(160,160,160));

        /* JButtons will be added inside this panel */
        pl3 = new JPanel();

        /* panel for locating the second column and used a list- PANEL 4 */
        pl4 = new JPanel();
        pl4.setBackground(Color.getHSBColor(160,160,160));

        /* two lists created which store the Data Strings */
        String[] str = {"",""};
        l1 = new JList<>(str);
        l2 = new JList<>(str);

        /* designing scrollable view of the first columns and allocating location and size*/
        sc_bar1 = new JScrollPane(l1,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        sc_bar1.setPreferredSize(new Dimension(250, 300));
        sc_bar1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sc_bar1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        sc_bar1.getVerticalScrollBar().setMaximum(1000);
        sc_bar1.getVerticalScrollBar().setMinimum(10);

        // setting layout of the panel 1 in the frame
        pl1.setLayout(new BoxLayout(pl1,BoxLayout.Y_AXIS));

        /* Adding a heading for the first column */
        he_1 = new JLabel("YOUR DETAILS");
        pl1.add(he_1);
        pl1.add(sc_bar1);

        /* designing scrollable view of the Second columns and allocating location and size*/
        sc_bar2 = new JScrollPane(l2,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        sc_bar2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sc_bar2.setPreferredSize(new Dimension(250, 300));
        sc_bar2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sc_bar2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pl4.setLayout(new BoxLayout(pl4,BoxLayout.Y_AXIS));

        /* Adding a heading for the second column */
        he_2 = new JLabel("WHO HAS STOCK IN MARKET");
        pl4.add(he_2);
        pl4.add(sc_bar2);

        /* This label command will be updated when the user presses Give Stock button
         * String messages will be printed inside this*/
        label_executeCommand = new JLabel(" ");
        pl2.add(label_executeCommand);
        pl2.setAlignmentX(SwingConstants.CENTER);
        pl2.setLayout(new BoxLayout(pl2,BoxLayout.Y_AXIS));

         /* ---give stock to other trader
               OnCLick Events are added for the buttons
           --- This panel 3 will be added inside panel 2
          */
        g_S = new JButton("Trade Stock");
        g_S.addActionListener(this);
        pl3.add(g_S);
        pl2.setBounds(150,350, 450,70 );

        // setting the layout for panel 2 and setting up the position
        design1 = new BoxLayout(pl2,BoxLayout.Y_AXIS);
        pl2.setLayout(design1);
        pl2.add(pl3);

        /* Creating GUI frame */
        frame = new JFrame("Trader");
        frame.setLocation(new Point(500,250));
        frame.setPreferredSize(new Dimension(700, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        // adding all the panels inside the main panel
        panel.add(pl0); panel.add(pl1); panel.add(pl4); frame.add(pl2);
        frame.add(panel); frame.pack();
    }

    /******************************************************************************************
     ****      This function is an action listener for the button trading stock
     ****      On click of this trader will be able to trade a stock
     ******************************************************************************************/
    public void actionPerformed(ActionEvent event){
        if (event.getSource() == g_S){
            GStockTo_T();
        }
    }

    /******************************************************************************************
     ****      This function is used for sending stock to another trader
     ****      We need to select a trader first from the second column
     ****      Onclick of Give stock button it will check weather you have a stock to trade
     ****      Otherwise it will give an error message
     ******************************************************************************************/
    public void GStockTo_T(){
        // selecting trader from (i.e column 2)
        String str = l2.getSelectedValue();
        System.out.print(str + "\n");


        // telling trader to select another trader, if he hasnt selected one
        if (str == ""){
            label_executeCommand.setText("Select Trader");
            return;
        }
        // checking if trader eligible to trade
        if (Client_ID < 0){
            label_executeCommand.setText("SORRY! You are out of Stocks");
            return;
        }
        String[] youTrade = str.split(" ");
        int S_ID = 0;
        for( int i = 0;i<youTrade [0].length() - 11;i++)
            // target Id
            S_ID = S_ID * 10 + Character.getNumericValue(youTrade [0].charAt(i + 11));

        //display trader detail on terminal
        //System.out.print("selected " + S_ID + "\n");
        // showing traders stock number
        //System.out.print("selected " + market_Trader.Stock_Number + "\n");

        // checking weather the object.method is less than 0
        if (Client_ID != S_ID && market_Trader.Stock_Number <= 0)
            return;
        // sending text to be printed on terminal
        String cmd = "YourID:" + Client_ID + " TargetID:" + S_ID;

        //ID trader one not equal to ID trade two -- then he is able to give stock
        if (Client_ID != S_ID)
        {
            // stock number is reduced by 1
            market_Trader.Stock_Number--;
            // "!" using this symbol to split a string onto new line
            detail_m = detail_m + "Give stock to Trader " + S_ID + "!";
        }
        else
            // allowing the object.method and checking if he has the present stock than can keep with himself
            if (market_Trader.Stock_Number > 0){
                detail_m = detail_m + "Keep stock to myself!";
                // when it reads the symbol "!" the string will split and the next string will be on a new line
                l1.setListData(detail_m.split("!"));
                return;
            }
            else{
                label_executeCommand.setText("SORRY! You do not have enough stocks to Trade");
                return;
            }
            //printing on terminal as well as the list
        market_Trader.outputToTerminal(cmd);
        //System.out.print(cmd + "\n");
        l1.setListData(detail_m.split("!"));
    }

    /******************************************************************************************
     ****      This function calls the object.connectclient method
     ****      If it is connected -- then button changes to disconnect
     ****      Appropriate message thrown if the client connection to server failed
     ******************************************************************************************/
    public void ConTo_Server(){
        //creating and new object client thread
        market_Trader = new ClientThread(this);
        //when thread runs client automatically connected
        market_Trader.Cn_Client();
        //starting the thread
        market_Trader.start();
    }


}
