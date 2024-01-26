import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        xmlTabPanel = new XMLTabPanel(mainAuthorFrame);
        arrangePanelPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane tabPane = (JTabbedPane) e.getSource();
                int selectedIndex = tabPane.getSelectedIndex();
                if (selectedIndex == 1) { // XML 탭이 선택되었을 때
                    System.out.println("XML 탭 선택됨");
                    xmlTabPanel.writeXml(); // XML 내용 작성 메소드 호출
                }
            }
        });
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
        private String bgImgFilePath;
        private Image userBgImage;
        JLabel arrangeBlockLabel;
        JLabel playerBlockLabel;
        MainAuthorFrame mainAuthorFrame;
        PropertyPane propertyPane;
        public ArrangeTabPanel(MainAuthorFrame mainAuthorFrame,PropertyPane propertyPane){
            setLayout(null);
            this.propertyPane=propertyPane;
            this.mainAuthorFrame=mainAuthorFrame;
        }
        public void setBgImgFilePath(String bgImgFilePath){
            this.bgImgFilePath=bgImgFilePath;
            ImageIcon bgSrc = new ImageIcon(bgImgFilePath);
            this.userBgImage = bgSrc.getImage();
            repaint();
        }
        public void setBlockForBlock(int [] blockSource, String imgFilePath, String shotImgFilePath) {
            int x = blockSource[0]; int y = blockSource[1]; int w= blockSource[2]; int h =blockSource[3];
            int randscope = blockSource[4]; int life = blockSource[5]; int speed = blockSource[6];
            Block arrangeBlock = new Block(x,y,w,h,randscope,life,speed,imgFilePath,shotImgFilePath,mainAuthorFrame,propertyPane);
            arrangeBlockLabel = arrangeBlock.getBlockLabel();
            add(arrangeBlockLabel);
            repaint();
        }
        public void setBlockForPlayer(int [] playerSource, String playerImgFilePath, String bulletImgFilePath) {
            int playerW = playerSource[0]; int playerH = playerSource[1]; int playerX= playerSource[2]; int playerY =playerSource[3];
            int bulletW = playerSource[4]; int bulletH = playerSource[5]; int fireTimeMs = playerSource[6];
            int bulletDistance = playerSource[7];
            Block playerBlock = new Block(playerW,playerH,playerX,playerY,bulletW,bulletH,
                                        fireTimeMs,bulletDistance,playerImgFilePath,bulletImgFilePath,
                                    mainAuthorFrame,propertyPane);

            playerBlockLabel = playerBlock.getBlockLabel();

            add(playerBlockLabel);
            repaint();
        }
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(userBgImage,0,0,this.getWidth(),this.getHeight(),null);
        }
}


    class XMLTabPanel extends JPanel{
        private MainAuthorFrame mainAuthorFrame;
        private String content;
        private JTextArea xmlContent;
        public XMLTabPanel(MainAuthorFrame mainAuthorFrame){
            this.mainAuthorFrame=mainAuthorFrame;
            setLayout(new BorderLayout());
            xmlContent = new JTextArea();
            xmlContent.setFont(new Font("고딕체", Font.BOLD, 20));
            JScrollPane scrollPane = new JScrollPane(xmlContent);
            add(scrollPane, BorderLayout.CENTER);
        }
        public void writeXml(){
            try {
                content = mainAuthorFrame.makeString();
                xmlContent.setText(content);
            }
            catch(ArrayIndexOutOfBoundsException e){
                JOptionPane.showMessageDialog(this, "내용이 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }

}

