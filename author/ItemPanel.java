import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

public class ItemPanel extends JPanel {
    private ItemPane itemPane;
    private PropertyPane propertyPane;
    private ArrangePanel arrangePanel;
    private MainAuthorFrame mainAuthorFrame;
    private  JTabbedPane itemPanelPane;
    private SettingPane settingPane;
    public ItemPanel(MainAuthorFrame mainAuthorFrame) {
        setLayout(new BorderLayout());
        this.mainAuthorFrame=mainAuthorFrame;
        itemPanelPane = new JTabbedPane();
        propertyPane = new PropertyPane();
        settingPane = new SettingPane(mainAuthorFrame);

        add(itemPanelPane,BorderLayout.CENTER);

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
    }
}
    class ItemPane extends JPanel {
        ItemPanel itemPanel;
        private JTextField[] inputFields;
        private Vector<Block> items;
        int rows = 3;
        int columns = 3;
        int gapX = 150; // x 좌표 간격
        int gapY = 150; // y 좌표 간격
        int startX = 0; // 시작 x 좌표
        int startY = 10; // 시작 y 좌표
        private ImageIcon mouseNormalBlock;
        private ImageIcon mousePressedBlock;
        JButton addButton;
        JButton deleteButton;
        int removedX, removedY;
        private MainAuthorFrame mainAuthorFrame;
        private ArrangePanel arrangePanel;
        private ArrangeTabPanel arrangeTabPanel;

        public ItemPane(MainAuthorFrame mainAuthorFrame, ArrangePanel arrangePanel) {
            setLayout(null);
            this.arrangePanel = arrangePanel;
            this.mainAuthorFrame = mainAuthorFrame;
            this.arrangeTabPanel = arrangePanel.getArrangeTabPanel();
            firstSetting();
            addButtons();
            drawBlocks();
        }

        public Vector<Block> getItems() {
            return items;
        }

        public ArrangeTabPanel getArrangeTabPanel() {
            return arrangeTabPanel;
        }

        public ImageIcon getMouseNormalBlock() {
            return mouseNormalBlock;
        }

        public ImageIcon getMousePressedBlock() {
            return mousePressedBlock;
        }

        public void firstSetting() {
            ImageIcon originalBlock = new ImageIcon("images/block.png");
            Image blockImg = originalBlock.getImage();
            Image scaledBlockImg = blockImg.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            mouseNormalBlock = new ImageIcon(scaledBlockImg);

            ImageIcon originalBlock2 = new ImageIcon("images/bomb.png");
            Image blockImg2 = originalBlock2.getImage();
            Image scaledBlockImg2 = blockImg2.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            mousePressedBlock = new ImageIcon(scaledBlockImg2);

            items = new Vector<>(9);
            for (int i = 0; i < rows; i++) {
                for (int n = 0; n < columns; n++) {
                    int x = startX + n * gapX;
                    int y = startY + i * gapY;
                    Block b = new Block(x, y, this);
                    items.add(b);

                }
            }
        }

        public void drawBlocks() {
            removeAll();
            addButtons();
            for (int i = 0; i < items.size(); i++) {
                Block b = items.get(i);
                JLabel blockLabel = b.getBlockLabel();
                blockLabel.setLocation(b.getBlockX(), b.getBlockY());
                add(blockLabel);
            }
            repaint();
        }

        class ButtonActionListener extends MouseAdapter {
            private JFileChooser chooser;

            @Override
            public void mousePressed(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                if (button.getText().equals("배경 설정")) {
                    chooser = new JFileChooser("./images");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG & GIF Images"
                            , "jpg", "png", "gif");
                    chooser.setFileFilter(filter);
                    int result = chooser.showOpenDialog(null);
                    if (result != JFileChooser.APPROVE_OPTION) return;
                    String filePath = chooser.getSelectedFile().getPath();
                    ImageIcon userBg = new ImageIcon(filePath);
                    Image userBgImg = userBg.getImage();
                    arrangeTabPanel.setUserBgImage(userBgImg);
                    arrangeTabPanel.repaint();
                }
                if (button.getText().equals("삭제")) {
                    Iterator<Block> it = items.iterator();
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

        public void addButtons() {
            addButton = new JButton("배경 설정");
            deleteButton = new JButton("삭제");
            addButton.setSize(100, 30);
            deleteButton.setSize(100, 30);
            addButton.setLocation(70, 900);
            deleteButton.setLocation(230, 900);
            add(addButton);
            add(deleteButton);
            addButton.addMouseListener(new ButtonActionListener());
            deleteButton.addMouseListener(new ButtonActionListener());
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
            public PropertyPane(){
                setLayout(null);
                attrs = new JLabel[]{x, y, w, h, randscope, life, speed};
                for (int i = 0; i < attrs.length; i++) {
                    attrs[i].setSize(300, 20);
                    attrs[i].setFont(new Font("고딕체", Font.BOLD, 15));
                    attrs[i].setLocation(5, 5 + 50 * i);
                    add(attrs[i]);
                }
                imgLabel.setSize(300,20);
                imgLabel.setFont(new Font("고딕체",Font.BOLD,15));
                imgLabel.setLocation(150,380);
                add(imgLabel);
                shotimgLabel.setSize(300,20);
                shotimgLabel.setFont(new Font("고딕체",Font.BOLD,15));
                shotimgLabel.setLocation(140,670);
                add(shotimgLabel);
                img.setSize(100,100);
                img.setLocation(150,430);
                add(img);
                blockShotLabel.setSize(100,100);
                blockShotLabel.setLocation(150,720);
                add(blockShotLabel);
            }
            public void setProperties(Block b){
                x.setText("x 좌표 : "+ b.getBlockLabel().getX());
                y.setText("y 좌표 : "+ b.getBlockLabel().getY());
                w.setText("너비 w : "+ b.getBlockLabel().getWidth());
                h.setText("높이 h : "+ b.getBlockLabel().getHeight());
                randscope.setText("랜덤 이동 범위 : "+ b.getRandscope());
                life.setText("체력 life : "+ b.getLife());
                speed.setText("이동 속도 speed : "+b.getSpeed());
                img.setIcon(b.getUserNewBlock());
                blockShotLabel.setIcon(b.getUserNewShotBlock());
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
            public SettingPane(MainAuthorFrame mainAuthorFrame){
                setLayout(null);
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
            class SettingButtonActionListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent e){
                    JDialog se= new SettingDialog();
                    se.setVisible(true);
                }
            }
            class SettingDialog extends JDialog{
                private JLabel screenSizeLabel = new JLabel("화면 크기");
                private JLabel wLabel = new JLabel("w : ");
                private JLabel hLabel = new JLabel("h : ");
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
                String w,h,blockMode, shotMode, blockDirection, playerBlockDirection="";
                public SettingDialog() {
                    setLayout(null);
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
                    blockDirectionCombo.setSize(100,30);
                    blockDirectionCombo.setLocation(300,410);
                    //player
                    playerBlockDirectionCombo.setSize(100,30);
                    playerBlockDirectionCombo.setLocation(30,750);
                    shotCombo.setSize(100,30);
                    shotCombo.setLocation(300,750);
                    add(playerBlockDirectionCombo);
                    add(shotCombo);
                    add(blockCombo);
                    add(blockDirectionCombo);
                    blockCombo.setSelectedIndex(0);  // 첫 번째 항목을 디폴트로 설정
                    shotCombo.setSelectedIndex(0);
                    blockDirectionCombo.setSelectedIndex(0);
                    playerBlockDirectionCombo.setSelectedIndex(0);
                    playerBlockDirectionCombo.addActionListener(comboBoxActionListener);
                    shotCombo.addActionListener(comboBoxActionListener);
                    blockCombo.addActionListener(comboBoxActionListener);
                    blockDirectionCombo.addActionListener(comboBoxActionListener);
                    saveBtn.setSize(200,20);
                    saveBtn.setLocation(150,900);
                    add(saveBtn);
                    saveBtn.addActionListener(new SaveBtnActionListener());
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
                        mainAuthorFrame.settings.add(w);
                        mainAuthorFrame.settings.add(h);
                        mainAuthorFrame.settings.add(blockMode);
                        mainAuthorFrame.settings.add(shotMode);
                        mainAuthorFrame.settings.add(blockDirection);
                        mainAuthorFrame.settings.add(playerBlockDirection);

                        for(int i=0;i<mainAuthorFrame.settings.size();i++){
                            System.out.println(mainAuthorFrame.settings.get(i));
                        }
                    }
                }
            }
        }


