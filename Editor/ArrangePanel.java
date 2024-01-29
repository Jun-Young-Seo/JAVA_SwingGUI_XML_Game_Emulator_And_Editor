import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

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
        private ImageIcon bgImg;
        private String bgMusicFilePath,playerSoundFilePath;
        private String shotMode;
        private String blockMode;
        private Node gamePanelNode;
        private String blockDirection;
        private String bulletBlockDirection;
        private int blockW,blockH;
        private String bgImgFilePath;
        private Image userBgImage;
        JLabel arrangeBlockLabel;
        JLabel playerBlockLabel;
        MainAuthorFrame mainAuthorFrame;
        PropertyPane propertyPane;

        Color[] colors = {
                Color.BLACK,   // 검은색
                Color.BLUE,    // 파란색
                Color.CYAN,    // 청록색
                Color.DARK_GRAY, // 진회색
                Color.GRAY,    // 회색
                Color.GREEN,   // 녹색
                Color.LIGHT_GRAY, // 밝은 회색
                Color.MAGENTA, // 자홍색
                Color.ORANGE,  // 주황색
                Color.PINK,    // 분홍색
                Color.RED,     // 빨간색
                Color.WHITE,   // 하얀색
                Color.YELLOW   // 노란색
        };
        int[] fontStyles = {
                Font.PLAIN,  // 기본 스타일
                Font.BOLD,   // 굵은 스타일
                Font.ITALIC  // 이탤릭 스타일
        };
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
        public void setBlocksByXml(File file) {
            //미리 settings 벡터 크기 늘리기 --> set 메서드 사용을 위해
            for (int i = mainAuthorFrame.settings.size(); i < 20; i++) {
                mainAuthorFrame.settings.add(null);
            }
            System.out.println("호출");
            String XMLFile = file.getAbsolutePath();
            XMLReader xml = new XMLReader(XMLFile);
            Node settingNode = xml.getSettingElement();
            Node bgNode = XMLReader.getNode(settingNode, XMLReader.E_BG);
            Node sizeNode = XMLReader.getNode(settingNode, XMLReader.E_SIZE);
            Node gameMode = XMLReader.getNode(settingNode, XMLReader.E_GAMEMODE);

            NodeList bgNodeList = bgNode.getChildNodes();
            for (int i = 0; i < bgNodeList.getLength(); i++) {
                Node node = bgNodeList.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                if (node.getNodeName().equals(XMLReader.E_BGIMG)) {
                    bgImgFilePath = XMLReader.getAttr(node, "bgImg");
                    bgImg = new ImageIcon(bgImgFilePath);
                    userBgImage = bgImg.getImage();
                }
                if(node.getNodeName().equals(XMLReader.E_BGMUSIC)){
                    bgMusicFilePath = XMLReader.getAttr(node,"bgMusic");
                }
                if(node.getNodeName().equals(XMLReader.E_FIRESOUND)){
                    playerSoundFilePath = XMLReader.getAttr(node,"fireSound");
                }
            }
            NodeList gameModeList = gameMode.getChildNodes();
            for (int i = 0; i < gameModeList.getLength(); i++) {
                Node node = gameModeList.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                if (node.getNodeName().equals(XMLReader.E_SHOTMODE)) {
                    shotMode = XMLReader.getAttr(node, "shotMode");
                }
                if (node.getNodeName().equals(XMLReader.E_BLOCKDIRECTION)) {
                    blockDirection = XMLReader.getAttr(node, "blockDirection");
                }
                if (node.getNodeName().equals(XMLReader.E_BULLETBLOCKDIRECTION)) {
                    bulletBlockDirection = XMLReader.getAttr(node, "bulletBlockDirection");
                }
                if (node.getNodeName().equals(XMLReader.E_BLOCKMODE)) {
                    blockMode = XMLReader.getAttr(node, "blockMode");
                }
            }
            String screenW = XMLReader.getAttr(sizeNode, "w");
            String screenH = XMLReader.getAttr(sizeNode, "h");
            mainAuthorFrame.settings.set(0,screenW);
            mainAuthorFrame.settings.set(1,screenH);
            mainAuthorFrame.settings.set(2,blockMode);
            mainAuthorFrame.settings.set(3,shotMode);
            mainAuthorFrame.settings.set(4,blockDirection);
            mainAuthorFrame.settings.set(5,bulletBlockDirection);
            mainAuthorFrame.settings.set(6,bgImgFilePath);
            mainAuthorFrame.settings.set(7,bgMusicFilePath);
            mainAuthorFrame.settings.set(8,playerSoundFilePath);
            gamePanelNode = xml.getGamePanelElement();//게임패널부분 - 플레이어, 블록 object
            Node playerNode = XMLReader.getNode(gamePanelNode, XMLReader.E_PLAYER);
            Node scoreNode = XMLReader.getNode(gamePanelNode, XMLReader.E_SCORE);
            Node stageNode = XMLReader.getNode(gamePanelNode, XMLReader.E_STAGE);
            NodeList playerNodeList = playerNode.getChildNodes();
            NodeList scoreNodeList = scoreNode.getChildNodes();
            NodeList stageNodeList = stageNode.getChildNodes();
            for (int i = 0; i < scoreNodeList.getLength(); i++) {
                Node node = scoreNodeList.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                if (node.getNodeName().equals(XMLReader.E_SCOREBLOCK)) {
                    String scoreBlockX = XMLReader.getAttr(node, "scoreblockx");
                    String scoreBlockY = XMLReader.getAttr(node, "scoreblocky");
                    String scoreBlockW = XMLReader.getAttr(node, "scoreblockw");
                    String scoreBlockH = XMLReader.getAttr(node, "scoreblockh");
                    String scoreContent = XMLReader.getAttr(node, "scorecontent");
                    String scoreFontName = XMLReader.getAttr(node, "scorefontname");
                    String scoreFontStyle = XMLReader.getAttr(node, "scorefontstyle");
                    String scoreFontSize = XMLReader.getAttr(node, "scorefontsize");
                    String scoreFontColor = XMLReader.getAttr(node, "scorefontcolor");
                    Color fontColor = colors[Integer.parseInt(scoreFontColor)];
                    JLabel scoreLabel = new JLabel();
                    scoreLabel.setSize(Integer.parseInt(scoreBlockW), Integer.parseInt(scoreBlockH));
                    scoreLabel.setLocation(Integer.parseInt(scoreBlockX), Integer.parseInt(scoreBlockY));
                    scoreLabel.setFont(new Font(scoreFontName, fontStyles[Integer.parseInt(scoreFontStyle)], Integer.parseInt(scoreFontSize)));
                    scoreLabel.setForeground(fontColor);
                    add(scoreLabel);
                    mainAuthorFrame.settings.set(9,scoreBlockX);
                    mainAuthorFrame.settings.set(10,scoreBlockY);
                    mainAuthorFrame.settings.set(11,scoreBlockW);
                    mainAuthorFrame.settings.set(12,scoreBlockH);
                    mainAuthorFrame.settings.set(13,scoreContent);
                    mainAuthorFrame.settings.set(14,scoreFontName);
                    mainAuthorFrame.settings.set(15,scoreFontSize);
                    mainAuthorFrame.settings.set(16,scoreFontStyle);
                    mainAuthorFrame.settings.set(17,scoreFontColor);

                }
                if (node.getNodeName().equals(XMLReader.E_RULE)) {
                    String scoreGap = XMLReader.getAttr(node, "rule");
                    System.out.println("scoreGap : " + scoreGap);
                    System.out.println("vector size : " +mainAuthorFrame.settings.size());
                    System.out.println("Before: " + mainAuthorFrame.settings.get(18));

                    mainAuthorFrame.settings.set(18,scoreGap);

                    System.out.println("After: " + mainAuthorFrame.settings.get(18));

                }
            }
            for (int i = 0; i < stageNodeList.getLength(); i++) {
                Node node = stageNodeList.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                if (node.getNodeName().equals(XMLReader.E_STAGEBLOCK)) {
                    int stageBlockX = Integer.parseInt(XMLReader.getAttr(node, "stageblockx"));
                    int stageBlockY = Integer.parseInt(XMLReader.getAttr(node, "stageblocky"));
                    int stageBlockW = Integer.parseInt(XMLReader.getAttr(node, "stageblockw"));
                    int stageBlockH = Integer.parseInt(XMLReader.getAttr(node, "stageblockh"));
                    String stageContent = XMLReader.getAttr(node, "stagecontent");
                    String stageFontName = XMLReader.getAttr(node, "stagefontname");
                    int stageFontStyle = Integer.parseInt(XMLReader.getAttr(node, "stagefontstyle"));
                    int stageFontSize = Integer.parseInt(XMLReader.getAttr(node, "stagefontsize"));
                    int stageFontColor = Integer.parseInt(XMLReader.getAttr(node, "stagefontcolor"));
                    Color fontColor = colors[stageFontColor];
                    JLabel stageLabel = new JLabel();
                    stageLabel.setSize(stageBlockW, stageBlockH);
                    stageLabel.setLocation(stageBlockX, stageBlockY);
                    stageLabel.setFont(new Font(stageFontName, fontStyles[stageFontStyle], stageFontSize));
                    stageLabel.setForeground(fontColor);
                    add(stageLabel);
                }
                if (node.getNodeName().equals(XMLReader.E_PLUSSPEED)) {
                    String stagePlusSpeed = XMLReader.getAttr(node, "plusSpeed");
                    mainAuthorFrame.settings.set(19,stagePlusSpeed);
                }
            }
            for (int i = 0; i < playerNodeList.getLength(); i++) {
                Node node = playerNodeList.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                if (node.getNodeName().equals(XMLReader.E_BULLETBLOCK)) {
                    String playerImgFIlePath =XMLReader.getAttr(node, "blockimg");
                    int blockW = Integer.parseInt(XMLReader.getAttr(node, "blockw"));
                    int blockH = Integer.parseInt(XMLReader.getAttr(node, "blockh"));
                    int blockX = Integer.parseInt(XMLReader.getAttr(node, "blockx"));
                    int blockY = Integer.parseInt(XMLReader.getAttr(node, "blocky"));
                    int bulletW = Integer.parseInt(XMLReader.getAttr(node, "bulletw"));
                    int bulletH = Integer.parseInt(XMLReader.getAttr(node, "bulleth"));
                    int bulletDistance = Integer.parseInt(XMLReader.getAttr(node, "bulletdistance"));
                    int fireTimeMs = Integer.parseInt(XMLReader.getAttr(node, "firetimems"));
                    String bulletImgFilePath = XMLReader.getAttr(node, "bulletimg");
                    Block block = new Block(blockW,blockH,blockX,blockY,bulletW,bulletH,fireTimeMs,bulletDistance
                                            ,playerImgFIlePath,bulletImgFilePath,mainAuthorFrame,propertyPane);
                    add(block.getBlockLabel());
                    repaint();
                }
            }
            Node blockNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BLOCK);
            NodeList blockNodeList = blockNode.getChildNodes();
            for (int i = 0; i < blockNodeList.getLength(); i++) {
                Node node = blockNodeList.item(i);
                Block block;
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                if (node.getNodeName().equals(XMLReader.E_OBJ)) {
                    int blockX = Integer.parseInt(XMLReader.getAttr(node, "x"));
                    int blockY = Integer.parseInt(XMLReader.getAttr(node, "y"));
                    blockW = Integer.parseInt(XMLReader.getAttr(node, "w"));
                    blockH = Integer.parseInt(XMLReader.getAttr(node, "h"));
                    int randScope = Integer.parseInt(XMLReader.getAttr(node, "randscope"));
                    int life = Integer.parseInt(XMLReader.getAttr(node, "life"));
                    int speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
                    String imgFilePath = XMLReader.getAttr(node, "img");
                    String shotImgFilePath = XMLReader.getAttr(node,"shotimg");
                    block = new Block(blockX,blockY,blockW,blockH,randScope, life,speed,imgFilePath,shotImgFilePath,mainAuthorFrame,propertyPane);
                    add(block.getBlockLabel());
                    repaint();
                }
            }
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

