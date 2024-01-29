import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Vector;

public class ItemPanel extends JPanel {
    private ItemPane itemPane;
    private PropertyPane propertyPane;
    private ArrangePanel arrangePanel;
    private MainAuthorFrame mainAuthorFrame;
    private  JTabbedPane itemPanelPane;
    private SettingPane settingPane;
    private String bgImgFilePath;
    private ArrangeTabPanel arrangeTabPanel;
    public ItemPanel(MainAuthorFrame mainAuthorFrame) {
        setLayout(new BorderLayout());
        this.mainAuthorFrame=mainAuthorFrame;
        itemPanelPane = new JTabbedPane();
        propertyPane = new PropertyPane();
        settingPane = new SettingPane(mainAuthorFrame,this);

        add(itemPanelPane,BorderLayout.CENTER);

    }

    public void setBgImgFilePath(String bgImgFilePath) {
        this.bgImgFilePath = bgImgFilePath;
        arrangeTabPanel.setBgImgFilePath(bgImgFilePath);
    }

    public PropertyPane getPropertyPane() {
        return propertyPane;
    }
    public void setArrangePanel(ArrangePanel arrangePanel){
        this.arrangePanel=arrangePanel;
    }
    public void init(){
        itemPane = new ItemPane(mainAuthorFrame, arrangePanel);
        itemPanelPane.addTab("블록", itemPane);
        itemPanelPane.addTab("속성",propertyPane);
        itemPanelPane.addTab("설정",settingPane);
        this.arrangeTabPanel=arrangePanel.getArrangeTabPanel();

    }
}
    class ItemPane extends JPanel {
        private JTextField[] inputFields;
        private Vector<Block> normalBLocks;
        private Vector<Block> playerBlocks;
        int rows = 3;
        int columns = 3;
        int gapX = 150; // x 좌표 간격
        int gapY = 150; // y 좌표 간격
        int startX = 0; // 시작 x 좌표
        int startY = 10; // 시작 y 좌표
        private ImageIcon mouseNormalBlock;
        private ImageIcon playerBlock;
        JButton addButton;
        JButton deleteButton;
        int removedX, removedY;
        private MainAuthorFrame mainAuthorFrame;
        private ArrangePanel arrangePanel;
        private ArrangeTabPanel arrangeTabPanel;
        private String bgImg;

        public ItemPane(MainAuthorFrame mainAuthorFrame, ArrangePanel arrangePanel) {
            setLayout(null);
            this.arrangePanel = arrangePanel;
            this.mainAuthorFrame = mainAuthorFrame;
            this.arrangeTabPanel = arrangePanel.getArrangeTabPanel();
            firstSetting();
            drawBlocks();
        }

        public ImageIcon getPlayerBlock(){return playerBlock;}
        public Vector<Block> getNormalBLocks() {
            return normalBLocks;
        }
        public Vector<Block> getPlayerBlocks() {return playerBlocks;}
        public ArrangeTabPanel getArrangeTabPanel() {
            return arrangeTabPanel;
        }

        public ImageIcon getMouseNormalBlock() {
            return mouseNormalBlock;
        }



        public void firstSetting() {
            ImageIcon originalBlock = new ImageIcon("images/block.png");
            Image blockImg = originalBlock.getImage();
            Image scaledBlockImg = blockImg.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            mouseNormalBlock = new ImageIcon(scaledBlockImg);

            ImageIcon playerBlock1 = new ImageIcon("images/jet.png");
            Image playerBlockImg = playerBlock1.getImage();
            Image scaledPlayerBlockImg = playerBlockImg.getScaledInstance(80,80,Image.SCALE_SMOOTH);
            playerBlock = new ImageIcon(scaledPlayerBlockImg);

            normalBLocks = new Vector<>(9);
            playerBlocks = new Vector<>(3);

            for (int i = 0; i < rows; i++) {
                for (int n = 0; n < columns; n++) {
                    int x = startX + n * gapX;
                    int y = startY + i * gapY;
                    Block b = new Block(x, y, this,false);
                    normalBLocks.add(b);
                }
            }
            for(int i=0; i<rows; i++){
                int x= startX + i *gapX;
                int y = 600;
                Block b = new Block(x,y,this,true);
                playerBlocks.add(b);
            }
        }

        public void drawBlocks() {
            removeAll();
            for (int i = 0; i < normalBLocks.size(); i++) {
                Block b = normalBLocks.get(i);
                JLabel blockLabel = b.getBlockLabel();
                blockLabel.setLocation(b.getBlockX(), b.getBlockY());
                add(blockLabel);
            }
            for(int i=0; i<playerBlocks.size();i++){
                Block b= playerBlocks.get(i);
                JLabel playerBlockLabel = b.getBlockLabel();
                playerBlockLabel.setLocation(b.getBlockX(),b.getBlockY());
                add(playerBlockLabel);
            }
            repaint();
        }

        class ButtonActionListener extends MouseAdapter {
            private JFileChooser chooser;

            @Override
            public void mousePressed(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                if (button.getText().equals("삭제")) {
                    Iterator<Block> it = normalBLocks.iterator();
                    while (it.hasNext()) {
                        Block b = it.next();
                        if (b.isChoosed) {
                            removedX = b.blockX;
                            removedY = b.blockY;
                            it.remove();
                        }
                    }
                    drawBlocks();
                    repaint();
                }
            }
        }



        public String getInput() {
            String result = "";
            for (int i = 0; i < inputFields.length; i++) {
                result += inputFields[i].getText() + ",";
            }
            return result;
        }

    }
        class PropertyPane extends JPanel{
            private JLabel x = new JLabel("시작 x 좌표 : ");
            private JLabel y = new JLabel("시작 y 좌표 : ");
            private JLabel w = new JLabel("너비 w : ");
            private JLabel h = new JLabel("높이 h : ");
            private JLabel randscope = new JLabel("랜덤 이동 범위 : ");
            private JLabel life = new JLabel("체력 life : ");
            private JLabel speed = new JLabel("이동 속도 speed : ");
            private JLabel imgLabel = new JLabel("<기본 이미지>");
            private JLabel shotimgLabel = new JLabel("<소멸 시 이미지>");
            private JLabel img = new JLabel();
            private JLabel blockShotLabel = new JLabel();
            private JLabel [] attrs;
            private JLabel playerX = new JLabel("플레이어 x 좌표 : ");
            private JLabel playerY = new JLabel("플레이어 y 좌표 : ");
            private JLabel playerW = new JLabel("플레이어 너비 : ");
            private JLabel playerH = new JLabel("플레이어 높이 : ");
            private JLabel bulletW = new JLabel("총알 너비 : ");
            private JLabel bulletH = new JLabel("총알 높이 : ");
            private JLabel fireTimeMs = new JLabel("총알 발사 간격 : ");
            private JLabel bulletDistance = new JLabel("총알 이동 거리");
            private JLabel playerImgLabel = new JLabel("<플레이어 이미지>");
            private JLabel bulletImgLabel = new JLabel("<총알 이미지>");
            private JLabel playerImg = new JLabel();
            private JLabel bulletImg = new JLabel();
            private JLabel [] playerAttrs;
            public PropertyPane(){
                setLayout(null);
                attrs = new JLabel[]{x, y, w, h, randscope, life, speed};
                playerAttrs = new JLabel[]{playerX,playerY,playerW,playerH,bulletW,bulletH,fireTimeMs,bulletDistance};

            }
            public void makePane(boolean isPlayer){
                removeAll();
                if(!isPlayer) {
                    for (int i = 0; i < attrs.length; i++) {
                        attrs[i].setSize(300, 20);
                        attrs[i].setFont(new Font("고딕체", Font.BOLD, 15));
                        attrs[i].setLocation(5, 5 + 50 * i);
                        add(attrs[i]);
                    }
                    imgLabel.setSize(300, 20);
                    imgLabel.setFont(new Font("고딕체", Font.BOLD, 15));
                    imgLabel.setLocation(150, 380);
                    add(imgLabel);
                    shotimgLabel.setSize(300, 20);
                    shotimgLabel.setFont(new Font("고딕체", Font.BOLD, 15));
                    shotimgLabel.setLocation(140, 670);
                    add(shotimgLabel);
                    img.setSize(100, 100);
                    img.setLocation(150, 430);
                    add(img);
                    blockShotLabel.setSize(100, 100);
                    blockShotLabel.setLocation(150, 720);
                    add(blockShotLabel);
                }
                if(isPlayer){
                    for (int i = 0; i < playerAttrs.length; i++) {
                        playerAttrs[i].setSize(300, 20);
                        playerAttrs[i].setFont(new Font("고딕체", Font.BOLD, 15));
                        playerAttrs[i].setLocation(5, 5 + 50 * i);
                        add(playerAttrs[i]);
                    }
                    playerImgLabel.setSize(300, 20);
                    playerImgLabel.setFont(new Font("고딕체", Font.BOLD, 15));
                    playerImgLabel.setLocation(130, 400);
                    add(playerImgLabel);
                    bulletImgLabel.setSize(300, 20);
                    bulletImgLabel.setFont(new Font("고딕체", Font.BOLD, 15));
                    bulletImgLabel.setLocation(140, 690);
                    add(bulletImgLabel);
                    playerImg.setSize(100, 100);
                    playerImg.setLocation(150, 450);
                    add(playerImg);
                    bulletImg.setSize(100, 100);
                    bulletImg.setLocation(150, 740);
                    add(bulletImg);
                }
                repaint();
                }
                public ImageIcon makeImage(ImageIcon original,int w,int h){
                    Image newImg = original.getImage();
                    Image scaledNewImg = newImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                    ImageIcon result = new ImageIcon(scaledNewImg);
                    return result;
                }
            public void setProperties(Block b, boolean isPlayer){
                makePane(isPlayer);
                if(!isPlayer) {
                    x.setText("x 좌표 : " + b.getBlockLabel().getX());
                    y.setText("y 좌표 : " + b.getBlockLabel().getY());
                    w.setText("너비 w : " + b.getBlockLabel().getWidth());
                    h.setText("높이 h : " + b.getBlockLabel().getHeight());
                    randscope.setText("랜덤 이동 범위 : " + b.getRandscope());
                    life.setText("체력 life : " + b.getLife());
                    speed.setText("이동 속도 speed : " + b.getSpeed());
                    ImageIcon block = makeImage(b.getUserNewBlock(),100,100);
                    ImageIcon shot = makeImage(b.getUserNewShotBlock(),100,100);
                    img.setIcon(block);
                    blockShotLabel.setIcon(shot);
                }
                if(isPlayer){
                    playerX.setText("x 좌표 : " + b.getBlockLabel().getX());
                    playerY.setText("y 좌표 : " + b.getBlockLabel().getY());
                    playerW.setText("너비 w : " + b.getBlockLabel().getWidth());
                    playerH.setText("높이 h : " + b.getBlockLabel().getHeight());
                    bulletW.setText("총알 너비 : " + b.getBulletW());
                    bulletH.setText("총알 높이 : " + b.getBulletH());
                    fireTimeMs.setText("발사 속도 : " + b.getFireTimeMs());
                    bulletDistance.setText("총알 거리 : " + b.getBulletDistance());
                    ImageIcon player = makeImage(b.getUserNewPlayerBlock(),100,100);
                    ImageIcon bullet = makeImage(b.getUserNewBullet(),100,100);
                    playerImg.setIcon(player);
                    bulletImg.setIcon(bullet);
                }
                repaint();
                }

        }
        class SettingPane extends JPanel{
            private JLabel screenSize = new JLabel("화면 크기");
            private JLabel gameMode = new JLabel("게임 모드");
            private JLabel bgSetting = new JLabel("소리 & 배경");
            private JLabel blockMode = new JLabel("블록 모드");
            private JLabel stage = new JLabel("점수 & 라운드");
            private JLabel playerBlock = new JLabel("플레이어 설정");
            private JLabel [] attrs;
            private JButton button = new JButton("설정");
            private MainAuthorFrame  mainAuthorFrame;
            SettingDialog settingDialog;
            ItemPanel itemPanel;
            private String bgImgFilePath;
            private SettingPane settingPane;
            public SettingPane(MainAuthorFrame mainAuthorFrame, ItemPanel itemPanel){
                setLayout(null);
                this.itemPanel=itemPanel;
                settingPane = this;
                this.mainAuthorFrame=mainAuthorFrame;
                attrs = new JLabel[]{screenSize,bgSetting,gameMode,blockMode,stage,playerBlock};
                for (int i = 0; i < attrs.length; i++) {
                    attrs[i].setSize(200, 30);
                    attrs[i].setFont(new Font("고딕체", Font.BOLD, 25));
                    attrs[i].setLocation(100, 5 + 150 * i);
                    add(attrs[i]);
                }
                button.setSize(200,30);
                button.setLocation(100,900);
                add(button);
                button.addActionListener(new SettingButtonActionListener());
            }
            public void setBgImgFilePath(String bgImgFilePath) {
                this.bgImgFilePath = bgImgFilePath;
                itemPanel.setBgImgFilePath(bgImgFilePath);
            }

            class SettingButtonActionListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent e){
                    settingDialog= new SettingDialog(settingPane);
                    settingDialog.setVisible(true);
                }
            }
            class SettingDialog extends JDialog{
                private SettingPane settingPane;
                private JLabel screenSizeLabel = new JLabel("화면 크기");
                private JLabel wLabel = new JLabel("가로 : ");
                private JLabel hLabel = new JLabel("세로 : ");
                private JTextField wInput = new JTextField(10);
                private JTextField hInput = new JTextField(10);
                private JLabel bgSettingLabel = new JLabel("소리 & 배경");
                private JLabel blockModeLabel = new JLabel("블록 모드");
                private JLabel stageLabel = new JLabel("점수 & 라운드");
                private JLabel playerBlockLabel = new JLabel("플레이어 설정");
                private JLabel [] attrs;
                private JButton saveBtn = new JButton("저장");
                private String [] blockModes ={"limit","infinite"};
                private String [] shotModes = {"auto","manual"};
                private String [] blockDirections = {"cross","down","random","chase"};
                private String [] playerBlockDirections = {"cross","updown","fourway"};
                private JComboBox blockCombo = new JComboBox(blockModes);
                private JComboBox shotCombo = new JComboBox(shotModes);
                private JComboBox blockDirectionCombo = new JComboBox(blockDirections);
                private JComboBox playerBlockDirectionCombo = new JComboBox(playerBlockDirections);
                private ComboBoxActionListener comboBoxActionListener = new ComboBoxActionListener();
                private String screenW,screenH,blockMode, shotMode, blockDirection,
                        playerBlockDirection,bgImgFilePath,bgMusicFilePath,playerSoundFilePath,
                        scoreX, scoreY, scoreW, scoreH, scoreContent, scoreFontName, scoreFontStyle,
                        scoreFontSize, scoreFontColor, scoreRule, plusSpeed="";
                private JButton [] buttons;
                private JLabel bgImg = new JLabel("배경 이미지");
                private JLabel bgMusic = new JLabel("배경 음악");
                private JLabel playerSound = new JLabel("플레이어 소리");
                private JLabel scoreRuleLabel = new JLabel("스테이지당 점수");
                private JLabel plusSpeedLabel = new JLabel("스테이지당 추가 속도");
                private JLabel scoreXLabel = new JLabel("점수판 x  ");
                private JLabel scoreYLabel = new JLabel("점수판 y  ");
                private JLabel scoreWLabel= new JLabel("점수판 w  ");
                private JLabel scoreHLabel = new JLabel("점수판 h  ");
                private JLabel scoreContentLabel = new JLabel("점수판 단어  ");
                private JLabel scoreFontNameLabel = new JLabel("폰트 이름  ");
                private JLabel scoreFontStyleLabel = new JLabel("폰트 스타일  ");
                private JLabel scoreFontSizeLabel = new JLabel("폰트 크기  ");
                private JLabel scoreFontColorLabel = new JLabel("폰트 색깔  ");
                private JButton scoreBtn = new JButton("점수판 설정");
                private JTextField ruleInput = new JTextField(10);
                private JTextField speedInput = new JTextField(10);

                private JTextField [] scoreFields;
                public SettingDialog(SettingPane settingPane) {
                    setLayout(null);
                    this.settingPane=settingPane;
                    setSize(500,1000);
                    setResizable(false);
                    attrs = new JLabel[]{screenSizeLabel,bgSettingLabel,blockModeLabel,stageLabel,playerBlockLabel};
                    for(int i=0;i<attrs.length;i++){
                        attrs[i].setSize(200,20);
                        attrs[i].setFont(new Font("고딕체", Font.BOLD, 20));
                        attrs[i].setLocation(170, 5 + 170 * i);
                        add(attrs[i]);
                    }
                    //screen size
                    wLabel.setSize(100,20);
                    wLabel.setFont(new Font("고딕체", Font.PLAIN, 20));
                    wLabel.setLocation(5, 55);
                    wInput.setSize(100,20);
                    wInput.setLocation(50,55);
                    hLabel.setSize(100,20);
                    hLabel.setFont(new Font("고딕체", Font.PLAIN, 20));
                    hLabel.setLocation(270, 55);
                    hInput.setSize(100,20);
                    hInput.setLocation(320,55);
                    add(wInput); add(hInput);
                    add(wLabel); add(hLabel);
                    //block
                    blockCombo.setSize(100,30);
                    blockCombo.setLocation(30,410);
                    blockCombo.setSelectedIndex(0);
                    blockDirectionCombo.setSize(100,30);
                    blockDirectionCombo.setLocation(300,410);
                    //player
                    playerBlockDirectionCombo.setSize(100,30);
                    playerBlockDirectionCombo.setLocation(30,750);
                    playerBlockDirectionCombo.setSelectedIndex(0);
                    shotCombo.setSize(100,30);
                    shotCombo.setLocation(300,750);
                    add(playerBlockDirectionCombo);
                    add(shotCombo);
                    add(blockCombo);
                    add(blockDirectionCombo);
                    blockMode = blockModes[0]; // "limit"
                    shotMode = shotModes[0]; // "auto"
                    blockDirection = blockDirections[0]; // "cross"
                    playerBlockDirection = playerBlockDirections[0]; // "cross"
                    playerBlockDirectionCombo.addActionListener(comboBoxActionListener);
                    shotCombo.addActionListener(comboBoxActionListener);
                    blockCombo.addActionListener(comboBoxActionListener);
                    blockDirectionCombo.addActionListener(comboBoxActionListener);
                    saveBtn.setSize(200,20);
                    saveBtn.setLocation(150,900);
                    add(saveBtn);
                    saveBtn.addActionListener(new SaveBtnActionListener());
                    //bg
                    buttons=new JButton[3];
                    for(int i=0;i<2;i++){
                        buttons[i]= new JButton("파일 선택");
                        buttons[i].setSize(100,20);
                        buttons[i].setLocation(100,220+i*50);
                        add(buttons[i]);
                    }
                    buttons[2] = new JButton("파일 선택");
                    buttons[2].setSize(100,20);
                    buttons[2].setLocation(300,220);
                    add(buttons[2]);
                    bgBtnActionListener bgBtnActionListener = new bgBtnActionListener();
                    for(int i=0;i<buttons.length;i++){
                        buttons[i].addActionListener(bgBtnActionListener);
                    }
                    bgImg.setFont(new Font("고딕체", Font.PLAIN, 13));
                    bgMusic.setFont(new Font("고딕체", Font.PLAIN, 13));
                    playerSound.setFont(new Font("고딕체", Font.PLAIN, 13));
                    bgImg.setSize(100, 20);
                    bgImg.setLocation(5,220);
                    bgMusic.setSize(100,20);
                    bgMusic.setLocation(5,270);
                    playerSound.setSize(100,20);
                    playerSound.setLocation(210,220);
                    add(bgImg); add(bgMusic); add(playerSound);
                    //score
                    scoreBtn.setSize(100,40);
                    scoreBtn.setLocation(30,570);
                    scoreBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JDialog scoreDialog =new scoreDialog();
                            scoreDialog.setVisible(true);
                        }
                    });
                    add(scoreBtn);
                    scoreRuleLabel.setSize(200,20);
                    scoreRuleLabel.setLocation(150,550);
                    scoreRuleLabel.setFont(new Font("고딕체", Font.PLAIN, 13));
                    plusSpeedLabel.setFont(new Font("고딕체", Font.PLAIN, 13));
                    plusSpeedLabel.setSize(200,20);
                    plusSpeedLabel.setLocation(150,600);
                    ruleInput.setSize(100,20);
                    ruleInput.setLocation(300,550);
                    speedInput.setSize(100,20);
                    speedInput.setLocation(300,600);
                    add(scoreRuleLabel);
                    add(plusSpeedLabel);
                    add(ruleInput);
                    add(speedInput);
                    setLocationRelativeTo(null);

                }
                class ComboBoxActionListener implements ActionListener{
                    @Override
                    public void actionPerformed(ActionEvent e){
                        JComboBox comboBox= (JComboBox)e.getSource();
                        String selectedItem = (String) comboBox.getSelectedItem();
                        if (comboBox == blockCombo) {
                            blockMode=selectedItem;
                        } else if (comboBox == shotCombo) {
                            shotMode=selectedItem;
                        } else if (comboBox == blockDirectionCombo) {
                            blockDirection = selectedItem;
                        } else if (comboBox == playerBlockDirectionCombo) {
                            playerBlockDirection=selectedItem;
                        }
                    }
                }
                class SaveBtnActionListener implements ActionListener{
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        screenW=wInput.getText(); screenH=hInput.getText();
                        plusSpeed=speedInput.getText(); scoreRule = ruleInput.getText();
                        mainAuthorFrame.settings.add(screenW);
                        mainAuthorFrame.settings.add(screenH);
                        mainAuthorFrame.settings.add(blockMode);
                        mainAuthorFrame.settings.add(shotMode);
                        mainAuthorFrame.settings.add(blockDirection);
                        mainAuthorFrame.settings.add(playerBlockDirection);
                        mainAuthorFrame.settings.add(bgImgFilePath);
                        mainAuthorFrame.settings.add(bgMusicFilePath);
                        mainAuthorFrame.settings.add(playerSoundFilePath);
                        mainAuthorFrame.settings.add(scoreX);
                        //10
                        mainAuthorFrame.settings.add(scoreY);
                        mainAuthorFrame.settings.add(scoreW);
                        mainAuthorFrame.settings.add(scoreH);
                        mainAuthorFrame.settings.add(scoreContent);
                        mainAuthorFrame.settings.add(scoreFontName);
                        mainAuthorFrame.settings.add(scoreFontSize);
                        mainAuthorFrame.settings.add(scoreFontStyle);
                        mainAuthorFrame.settings.add(scoreFontColor);
                        mainAuthorFrame.settings.add(scoreRule);
                        mainAuthorFrame.settings.add(plusSpeed);
                        //                                scoreX=scoreFields[0].getText();
                        //                                scoreY=scoreFields[1].getText();
                        //                                scoreW=scoreFields[2].getText();
                        //                                scoreH=scoreFields[3].getText();
                        //                                scoreContent=scoreFields[4].getText();
                        //                                scoreFontSize=scoreFields[5].getText();
                        wInput.setText(screenW);
                        hInput.setText(screenH);
                        scoreFields[0].setText(scoreX);
                        scoreFields[1].setText(scoreY);
                        scoreFields[2].setText(scoreW);
                        scoreFields[3].setText(scoreH);
                        scoreFields[4].setText(scoreContent);
                        scoreFields[5].setText(scoreFontSize);
                        for(int i=0;i<mainAuthorFrame.settings.size();i++){
                            System.out.println(mainAuthorFrame.settings.get(i));
                        }
                        setVisible(false);
                    }
                }
                class bgBtnActionListener implements ActionListener{
                    JFileChooser chooseForImage;
                    JFileChooser chooserForSound;
                    @Override
                    public void actionPerformed(ActionEvent e){
                        JButton btn = (JButton)e.getSource();
                        chooseForImage = new JFileChooser("./images");
                        chooserForSound = new JFileChooser("./sounds");
                        FileNameExtensionFilter filterForImage = new FileNameExtensionFilter("JPG & PNG & GIF Images"
                                , "jpg", "png", "gif");
                        FileNameExtensionFilter filterForSound = new FileNameExtensionFilter("wav Files","wav");

                        //bgimg
                        if(btn==buttons[0]){
                            chooseForImage.setFileFilter(filterForImage);
                            int result = chooseForImage.showOpenDialog(null);
                            if (result != JFileChooser.APPROVE_OPTION) return;
                            bgImgFilePath = chooseForImage.getSelectedFile().getPath();
                            settingPane.setBgImgFilePath(bgImgFilePath);
                        }
                        //bgmusic
                        else if(btn ==buttons[1]){
                            chooserForSound.setFileFilter(filterForSound);
                            int result = chooserForSound.showOpenDialog(null);
                            if (result != JFileChooser.APPROVE_OPTION) return;
                            bgMusicFilePath = chooserForSound.getSelectedFile().getPath();
                        }
                        //playersound
                        else if (btn ==buttons[2]){
                            chooserForSound.setFileFilter(filterForSound);
                            int result = chooserForSound.showOpenDialog(null);
                            if (result != JFileChooser.APPROVE_OPTION) return;
                            playerSoundFilePath = chooserForSound.getSelectedFile().getPath();
                        }
                    }
                    public String getBgImgFilePath(){
                        return bgImgFilePath;
                    }
                }
                class scoreDialog extends JDialog{
                    private String [] scoreFontNames = {"궁서체","고딕체","명조체"};
                    private String [] scoreFontStyles = {"0","1","2"};
                    private String [] scoreFontColors = {"0","1","2","3","4","5","6","7","8","9","10","11","12"};
                    private JComboBox fontNameCombo= new JComboBox(scoreFontNames);
                    private JComboBox fontStyleCombo = new JComboBox(scoreFontStyles);
                    private JComboBox fontColorCombo = new JComboBox(scoreFontColors);
                    private JButton scoreDialogBtn=new JButton("저장");
                    public scoreDialog(){
                        setModal(true);
                        setLayout(null);
                        setSize(500,1000);
                        setResizable(false);
                        scoreFields = new JTextField[6];
                        //6,7,9
                        //6 : name
                        //7 : style
                        //9 : color
                        attrs = new JLabel[]{scoreXLabel,scoreYLabel,scoreWLabel,scoreHLabel,scoreContentLabel,scoreFontSizeLabel};
                        for(int i=0;i<attrs.length;i++) {
                            attrs[i].setSize(300, 30);
                            attrs[i].setFont(new Font("고딕체", Font.BOLD, 20));
                            attrs[i].setLocation(170, 5 + 100 * i);
                            add(attrs[i]);
                            scoreFields[i]=new JTextField(20);
                            scoreFields[i].setSize(300,30);
                            scoreFields[i].setLocation(80,55+100*i);
                            add(scoreFields[i]);
                        }
                        scoreFontNameLabel.setSize(300,30);
                        scoreFontNameLabel.setFont(new Font("고딕체",Font.BOLD,20));
                        scoreFontNameLabel.setLocation(170,605);
                        scoreFontStyleLabel.setSize(300,30);
                        scoreFontStyleLabel.setFont(new Font("고딕체",Font.BOLD,20));
                        scoreFontStyleLabel.setLocation(170,705);
                        scoreFontColorLabel.setSize(300,30);
                        scoreFontColorLabel.setFont(new Font("고딕체",Font.BOLD,20));
                        scoreFontColorLabel.setLocation(170,805);
                        add(scoreFontNameLabel); add(scoreFontStyleLabel); add(scoreFontColorLabel);
                        fontNameCombo.setSize(300,30);
                        fontNameCombo.setLocation(80,655);
                        fontNameCombo.setSelectedIndex(0);
                        fontStyleCombo.setSize(300,30);
                        fontStyleCombo.setLocation(80,755);
                        fontStyleCombo.setSelectedIndex(0);
                        fontColorCombo.setSize(300,30);
                        fontColorCombo.setLocation(80,855);
                        fontColorCombo.setSelectedIndex(10);
                        add(fontNameCombo); add(fontStyleCombo); add(fontColorCombo);
                        scoreDialogBtn.setSize(100,30);
                        scoreDialogBtn.setLocation(170,900);
                        scoreDialogBtn.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                scoreX=scoreFields[0].getText();
                                scoreY=scoreFields[1].getText();
                                scoreW=scoreFields[2].getText();
                                scoreH=scoreFields[3].getText();
                                scoreContent=scoreFields[4].getText();
                                scoreFontSize=scoreFields[5].getText();
                                scoreFontName= (String) fontNameCombo.getSelectedItem();
                                scoreFontStyle = (String)fontStyleCombo.getSelectedItem();
                                scoreFontColor = (String)fontColorCombo.getSelectedItem();
                                setVisible(false);
                            }
                        });
                        add(scoreDialogBtn);
                        setLocationRelativeTo(null);

                    }
                }
            }
        }


