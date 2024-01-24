import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.StringTokenizer;
import java.util.Vector;

public class Block {
    private Vector<Block> items;
    //눌러져 있는 아이템인지 확인
    boolean isChoosed = false;
    //아이템 패널에서 좌표
    int blockX;
    int blockY;
    private JLabel blockLabel;
    //arrangePanel용 변수
    int x,y,w,h,randscope,life,speed;
    private ImageIcon mouseNormalBlock;
    private ItemPane itemPane;
    private ImageIcon mousePressedBlock;
    private boolean isSaved = false;
    private ArrangeTabPanel arrangeTabPanel;
    private ImageIcon userNewBlock;
    private ImageIcon userNewShotBlock;
    private int [] blockSource; //x,y,w,h,randscope,life,speed
    private MainAuthorFrame mainAuthorFrame;
    private PropertyPane propertyPane;
    private String imgFilePath;
    private String shotImgFilePath;
    //ItemPane 표기용 블록 생성자
    public Block(int blockX, int blockY, ItemPane itemPane) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.itemPane=itemPane;
        this.arrangeTabPanel=itemPane.getArrangeTabPanel();
        this.mousePressedBlock = itemPane.getMousePressedBlock();
        blockSource = new int[7];
        items=itemPane.getItems();
        this.mouseNormalBlock=itemPane.getMouseNormalBlock();
        blockLabel = new JLabel();
        blockLabel.setIcon(mouseNormalBlock);
        blockLabel.setSize(80, 80);
        BlockActionListener blc = new BlockActionListener();
        blockLabel.addMouseListener(blc);

    }
    //arrangeTabPanel에서 호출돼서 모든 속성을 가지는 블록
    public Block(int x, int y, int w, int h, int randscope, int life, int speed,
                 String imgFilePath, String shotImgFilePath,
                 MainAuthorFrame mainAuthorFrame, PropertyPane propertyPane){
        this.x=x;this.y=y;this.w=w;this.h=h; this.randscope=randscope;this.life=life;
        this.speed=speed; this.imgFilePath=imgFilePath;this.shotImgFilePath=shotImgFilePath;
        this.mainAuthorFrame=mainAuthorFrame; this.propertyPane=propertyPane;

        Image newBlockImg = new ImageIcon(imgFilePath).getImage();
        Image scaledBlockImg = newBlockImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        ImageIcon userNewBlock = new ImageIcon(scaledBlockImg);

        blockLabel = new JLabel();
        blockLabel.setIcon(userNewBlock);


        blockLabel.setSize(w,h);
        blockLabel.setLocation(x,y);
        BlockActionListener blc = new BlockActionListener();
        blc.isItemPane=false;
        blockLabel.addMouseListener(blc);
        blockLabel.addMouseMotionListener(blc);

    }
    public Block findBlockInItemPane(JLabel block) {
        for (Block b : items) {
            if (b.getBlockLabel() == block) {
                return b;
            }
        }
        return null;
    }

    public Block findBlockInArrangePane(JLabel block) {
        for (Block b : mainAuthorFrame.arrangeBlocks) {
            if (b.getBlockLabel() == block) {
                return b;
            }
        }
        return null;
    }
    //<Obj x="random" y="random"w="100" h="100" randscope="100" life="3"
    // speed="5" img="images/monster2.png" shotimg="images/shot.png"/>
    public String toXML() {
        return "<Obj x=\"" + x + "\" y=\"" + y + "\" w=\"" + w + "\" h=\"" + h +
                "\" randscope=\"" + randscope + "\" life=\"" + life +
                "\" speed=\"" + speed + "\" img=\"" + imgFilePath +
                "\" shotimg=\"" + shotImgFilePath + "\"/>\n";
    }
    public int getBlockX() {
        return blockX;
    }
    public String getImgFilePath(){return imgFilePath;}
    public String getShotImgFilePath(){return shotImgFilePath;}
    public int getRandscope(){return randscope;}
    public int getLife(){return life;}
    public int getSpeed(){return speed;}
    public ImageIcon getUserNewBlock(){
        return userNewBlock;
    }
    public ImageIcon getUserNewShotBlock(){
        return userNewShotBlock;
    }
    public int getBlockY() {
        return blockY;
    }

    public JLabel getBlockLabel() {
        return blockLabel;
    }
    public Block getBlock(){return this;}

    class BlockActionListener extends MouseAdapter {
        BlockDialog blockDialog;
        private Point initialClick; // 드래그 시작 위치
        private boolean isItemPane = true;
        @Override
        public void mouseClicked(MouseEvent e) {
            if(isItemPane) {
                if (e.getClickCount() == 2 && !isSaved) {
                    JLabel block = (JLabel) e.getSource();
                    Block b = findBlockInItemPane(block);
                    blockDialog = new BlockDialog();
                    blockDialog.setVisible(true);
                }
                if (e.getClickCount() == 2 && isSaved) {
                    JLabel block = (JLabel) e.getSource();
                    Block b = findBlockInItemPane(block);
                    blockDialog.setVisible(true);
                }
            }
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            if(!isItemPane) {
                JLabel blockLabel = (JLabel) e.getSource();
                Block b = findBlockInArrangePane(blockLabel);
                propertyPane.setProperties(b);
                Point currentLocation = blockLabel.getLocation(); // 현재 컴포넌트 위치
                int xMoved = e.getX() - initialClick.x; // X 이동 거리
                int yMoved = e.getY() - initialClick.y; // Y 이동 거리
                // 새 위치 계산
                int newX = currentLocation.x + xMoved;
                int newY = currentLocation.y + yMoved;
                // 컴포넌트 위치 업데이트
                blockLabel.setLocation(newX, newY);
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
            initialClick = e.getPoint(); // 초기 클릭 위치 저장
            if(isItemPane){
            JLabel block = (JLabel) e.getSource();
            Block b = findBlockInItemPane(block);
            for (int i = 0; i < items.size(); i++) {
                items.get(i).isChoosed = false;
            }
            if (b != null) {
                b.isChoosed = true;
                block.setIcon(mousePressedBlock);
                itemPane.repaint();
            }
            }
        }
}
        class BlockDialog extends JDialog {
            private JLabel x = new JLabel("시작 x 좌표 : ");
            private JLabel y = new JLabel("시작 y 좌표 : ");
            private JLabel w = new JLabel("너비 w : ");
            private JLabel h = new JLabel("높이 h : ");
            private JLabel randscope = new JLabel("랜덤 이동 범위 : ");
            private JLabel life = new JLabel("체력 life : ");
            private JLabel speed = new JLabel("이동 속도 speed : ");
            private JLabel img = new JLabel("기본 이미지 : ");
            private JLabel shotimg = new JLabel("소멸 시 이미지 : ");
            private JButton ok = new JButton("저장");
            private JLabel[] attrs;
            private JTextField[] inputFields = new JTextField[9];
            private JFileChooser chooser;

            private JTextField imgFileTextField;
            private JTextField shotImgFileTextField;

            public BlockDialog() {
                setLayout(null);
                setResizable(false);
                attrs = new JLabel[]{x, y, w, h, randscope, life, speed};
                for (int i = 0; i < attrs.length; i++) {
                    attrs[i].setSize(300, 20);
                    attrs[i].setFont(new Font("고딕체", Font.BOLD, 15));
                    attrs[i].setLocation(5, 5 + 35 * i);
                    add(attrs[i]);
                    inputFields[i] = new JTextField(30);
                    inputFields[i].setSize(200, 20);
                    inputFields[i].setLocation(200, 5 + 35 * i);
                    add(inputFields[i]);
                }
                img.setSize(300,20);
                img.setFont(new Font("고딕체",Font.BOLD,15));
                img.setLocation(5,250);
                shotimg.setSize(300,20);
                shotimg.setFont(new Font("고딕체",Font.BOLD,15));
                shotimg.setLocation(5,285);
                add(img);add(shotimg);
                imgFileTextField = new JTextField(30);
                imgFileTextField.setEnabled(false);
                imgFileTextField.setSize(200,20);
                imgFileTextField.setLocation(200,250);
                shotImgFileTextField = new JTextField(30);
                shotImgFileTextField.setEnabled(false);
                shotImgFileTextField.setSize(200,20);
                shotImgFileTextField.setLocation(200,285);
                add(imgFileTextField); add(shotImgFileTextField);
                setSize(500, 500);
                addImageButton();
            }
            private void addImageButton(){
                JButton imgButton = new JButton("기본 이미지 추가");
                imgButton.addActionListener(new imgButtonActionListener());
                JButton shotImgButton = new JButton("폭발 이미지 추가");
                shotImgButton.addActionListener(new imgButtonActionListener());
                JButton arrangeButton = new JButton("블록 배치하기");
                arrangeButton.setSize(150,30);
                arrangeButton.setLocation(150,400);
                imgButton.setSize(150,30);
                shotImgButton.setSize(150,30);
                imgButton.setLocation(30,320);
                shotImgButton.setLocation(270,320);
                ok.setSize(60, 30);
                ok.setLocation(190, 360);
                add(ok);
                ok.addActionListener(new okButtonActionListener());
                arrangeButton.addActionListener(new arrangeButtonActionListener());
                add(imgButton);
                add(shotImgButton);
                add(arrangeButton);
            }
            class okButtonActionListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    for(int i=0;i<blockSource.length;i++){
                        blockSource[i] = Integer.parseInt(inputFields[i].getText());
                        isSaved=true;
                    }
                }
            }
            class imgButtonActionListener implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e){
                    JButton button = (JButton)(e.getSource());
                    chooser = new JFileChooser("./images");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG & GIF Images"
                            , "jpg", "png", "gif");
                    chooser.setFileFilter(filter);
                    int result = chooser.showOpenDialog(null);
                    if (result != JFileChooser.APPROVE_OPTION) return;
                    if(button.getText().equals("기본 이미지 추가")){
                        imgFilePath = chooser.getSelectedFile().getPath();
                        Image newBlockImg = new ImageIcon(imgFilePath).getImage();
                        Image scaledBlockImg = newBlockImg.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        userNewBlock = new ImageIcon(scaledBlockImg);
                        blockLabel.setIcon(userNewBlock);
                        imgFileTextField.setText(imgFilePath);
                        repaint();
                    }
                    if(button.getText().equals("폭발 이미지 추가")){
                        shotImgFilePath = chooser.getSelectedFile().getPath();
                        Image newShotBlockImg = new ImageIcon(shotImgFilePath).getImage();
                        Image scaledImg = newShotBlockImg.getScaledInstance(80,80,Image.SCALE_SMOOTH);
                        userNewShotBlock = new ImageIcon(scaledImg);
                        shotImgFileTextField.setText(shotImgFilePath);
                    }
                }
            }
            class arrangeButtonActionListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent e) {
                    for(int i=0;i<blockSource.length;i++){
                        blockSource[i] = Integer.parseInt(inputFields[i].getText());
                        isSaved=true;
                    }

                    arrangeTabPanel.setBlock(blockSource,imgFilePath,shotImgFilePath);
                }
            }
        }
    }

