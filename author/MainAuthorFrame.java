import javax.swing.*;
import java.awt.*;

public class MainAuthorFrame extends JFrame {
    private ArrangePanel arrangePanel;
    private Container contentPane;
    private ItemPanel itemPanel;
    public MainAuthorFrame(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920,1080);
        contentPane = getContentPane();
        splitPanel();
        setVisible(true);
    }
    private void splitPanel(){
        setLayout(new BorderLayout());
        JSplitPane hSplitPane = new JSplitPane();
        hSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        hSplitPane.setDividerLocation(1500); // Divider 위치 설정
        contentPane.add(hSplitPane);
        arrangePanel = new ArrangePanel(itemPanel);
        itemPanel = new ItemPanel(arrangePanel);
        hSplitPane.setLeftComponent(arrangePanel);
        hSplitPane.setRightComponent(itemPanel);
    }

    public static void main (String [] args){
        new MainAuthorFrame();
    }
}
