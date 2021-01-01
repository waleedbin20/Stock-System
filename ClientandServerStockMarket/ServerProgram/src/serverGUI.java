/*1805967*/
import javax.swing.*;
import java.awt.*;

public class serverGUI {

    /*  Initialising the GUI variables*/
    public JFrame frame; public JPanel panel, panel1, panel2,panel3; public JLabel printinglabel;
    public JList l1; public JList l2;
    public JLabel[] strings;
    public JLabel heading1, heading2;
    public JScrollPane scroll_view1,  scroll_view2;


    /****************************************************************************
     ****       On execution this will execute the Server GUI
     ****       It will also start the thread
     ****************************************************************************/

    public void start(){
        runServerGUI();
        // new server thread object created
        ServerThread m_Server = new ServerThread(this);
        m_Server.start();
    }

    /****************************************************************************
     ****       This is function which creates the Server GUI
     ****************************************************************************/
    public void runServerGUI(){
        /* This panel is for positioning the objects inside the frame */
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 400));
        panel.setBackground(Color.lightGray);

        /* These two panels are for positioning the lists created and a scrollable view will be added */
        panel1 = new JPanel();
        panel2 = new JPanel();

        /* assigning the background color for panels */
        panel1.setBackground(Color.getHSBColor(110,170,60));
        panel2.setBackground(Color.getHSBColor(110,170,60));

        /* this is the position of the label that will be printed on the GUI */
        panel3 = new JPanel();
        printinglabel = new JLabel("Waiting for traders to join the market server...");
        panel3.add(printinglabel);
        panel3.setBackground(Color.lightGray);

        /* Heading created for the first column*/
        heading1 = new JLabel("TRADERS-AVAILABLE");
        panel1.add(heading1);
        /* Heading created for the second column*/
        heading2 = new JLabel("TRADER HOLDING STOCK IN MARKET ");
        panel2.add(heading2);

        /* This is creating two Lists */
        strings = new JLabel[1];
        l1 = new JList(strings);
        l2 = new JList(strings);

        /* Scrollable view created for the first column */
        scroll_view1 = new JScrollPane(l1);
        scroll_view1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_view1.setPreferredSize(new Dimension(200, 250));
        panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));

        /* Scrollable view created for the second column */
        scroll_view2 = new JScrollPane(l2);
        scroll_view2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_view2.setPreferredSize(new Dimension(200, 250));
        scroll_view1.getHorizontalScrollBar().setMaximum(100);

        /* adding scrollable view in panel*/
        panel1.add(scroll_view1);
        panel2.setLayout(new BoxLayout(panel2,BoxLayout.Y_AXIS));

        /* displaying a heading for the column 2 -- list 2*/
        scroll_view2.getHorizontalScrollBar().setMinimum(0);
        panel2.add(scroll_view2);

        /* This is creating fame size and adding some specifications to it */
        frame = new JFrame("Market Server");
        frame.setLocation(new Point(500,250));
        frame.setPreferredSize(new Dimension(650, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        /* Adding all the sub panels inside the main panel*/
        panel.add(panel1); panel.add(panel2); panel.add(panel3);
        frame.add(panel);
        frame.pack();
    }
}
