import javax.swing.*;
import java.awt.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class ArrangePanel extends JPanel {
    private ArrangeTabPanel arrangeTabPanel;
    private XMLTabPanel xmlTabPanel;
    private ItemPanel itemPanel;
    Vector<ItemPanel.Block> items = new Vector<>(10);
    public ArrangePanel(ItemPanel itemPanel) {
        this.itemPanel = itemPanel;
        setLayout(new BorderLayout());
        JTabbedPane arrangePanelPane = new JTabbedPane();
        arrangeTabPanel = new ArrangeTabPanel(itemPanel);
        xmlTabPanel = new XMLTabPanel();

        arrangePanelPane.addTab("작업 중", arrangeTabPanel);
        arrangePanelPane.addTab("XML", xmlTabPanel);

        add(arrangePanelPane, BorderLayout.CENTER);

    }
    public ArrangeTabPanel getArrangeTabPanel(){
        return arrangeTabPanel;
    }
}

    //사용자가 아이템을 붙이는 탭팬
    class ArrangeTabPanel extends JPanel{
        ItemPanel itemPanel;
        Vector<ItemPanel.Block> items;

        public ArrangeTabPanel(ItemPanel itemPanel) {
            this.itemPanel = itemPanel;
            items = new Vector<>(10);
        }
        public void setBlock(String input){
            StringTokenizer stk = new StringTokenizer(input,",");
            while(stk.hasMoreTokens()){
                int x = Integer.parseInt(stk.nextToken());
                int y = Integer.parseInt(stk.nextToken());
                int w = Integer.parseInt(stk.nextToken());
                int h = Integer.parseInt(stk.nextToken());
                System.out.println(stk.nextToken());
            }
        }
    }

    class XMLTabPanel extends JPanel{
        public XMLTabPanel(){
            setBackground(Color.YELLOW);
        }
    }

