import javax.swing.*;
import java.awt.*;

public class ArrangePanel extends JPanel {
    private ArrangeTabPanel arrangeTabPanel;
    private XMLTabPanel xmlTabPanel;
    private MainAuthorFrame mainAuthorFrame;
    private PropertyPane propertyPane;
    private ItemPanel itemPanel;
    JTabbedPane arrangePanelPane;
    public ArrangePanel(MainAuthorFrame mainAuthorFrame) {
        this.mainAuthorFrame=mainAuthorFrame;
        setLayout(new BorderLayout());
        arrangePanelPane = new JTabbedPane();
        xmlTabPanel = new XMLTabPanel();

        add(arrangePanelPane, BorderLayout.CENTER);
    }
    public void init(){
        propertyPane=itemPanel.getPropertyPane();
        arrangeTabPanel = new ArrangeTabPanel(mainAuthorFrame,propertyPane);
        arrangePanelPane.addTab("작업 중", arrangeTabPanel);
        arrangePanelPane.addTab("XML",xmlTabPanel);
    }
    public void setItemPanel(ItemPanel itemPanel){
        this.itemPanel=itemPanel;
    }
    public ArrangeTabPanel getArrangeTabPanel(){
        return arrangeTabPanel;
    }
}

    //사용자가 아이템을 붙이는 탭팬
    class ArrangeTabPanel extends JPanel{
        private Image userBgImage;
        JLabel arrangeBlockLabel;
        MainAuthorFrame mainAuthorFrame;
        PropertyPane propertyPane;
        public ArrangeTabPanel(MainAuthorFrame mainAuthorFrame,PropertyPane propertyPane){
            setLayout(null);
            this.propertyPane=propertyPane;
            this.mainAuthorFrame=mainAuthorFrame;
            userBgImage = null;
        }
        public void setUserBgImage(Image userBgImage){
            this.userBgImage=userBgImage;
        }
        public void setBlock(int [] blockSource, String imgFilePath, String shotImgFilePath) {

            int x = blockSource[0]; int y = blockSource[1]; int w= blockSource[2]; int h =blockSource[3];
            int randscope = blockSource[4]; int life = blockSource[5]; int speed = blockSource[6];

            Block arrangeBlock = new Block(x,y,w,h,randscope,life,speed,imgFilePath,shotImgFilePath,mainAuthorFrame,propertyPane);

            mainAuthorFrame.arrangeBlocks.add(arrangeBlock);
            arrangeBlockLabel = arrangeBlock.getBlockLabel();
            add(arrangeBlockLabel);
            repaint();
        }
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(userBgImage,0,0,this.getWidth(),this.getHeight(),null);
        }
}


    class XMLTabPanel extends JPanel{
        public XMLTabPanel(){
            setBackground(Color.YELLOW);
        }
    }

