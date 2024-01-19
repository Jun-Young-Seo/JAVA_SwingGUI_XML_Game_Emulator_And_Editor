import javax.swing.*;
import java.awt.*;

public class ArrangePanel extends JPanel {
    private ArrangeTabPanel arrangeTabPanel;
    private XMLTabPanel xmlTabPanel;
    public ArrangePanel(){
        setLayout(new BorderLayout());
        JTabbedPane arrangePanelPane = new JTabbedPane();
        arrangeTabPanel = new ArrangeTabPanel();
        xmlTabPanel = new XMLTabPanel();

        arrangePanelPane.addTab("작업 중",arrangeTabPanel);
        arrangePanelPane.addTab("XML",xmlTabPanel);

        add(arrangePanelPane, BorderLayout.CENTER);

    }

    //사용자가 아이템을 붙이는 탭팬
    class ArrangeTabPanel extends JPanel{
        public ArrangeTabPanel(){
            setBackground(Color.BLUE);
        }
    }

    class XMLTabPanel extends JPanel{
        public XMLTabPanel(){
            setBackground(Color.YELLOW);
        }
    }
}
