import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import static javax.swing.JOptionPane.showMessageDialog;

public class Block {
    private Vector<Block> normalBlocks;
    //눌러져 있는 아이템인지 확인
    boolean isChoosed = false;
    //아이템 패널에서 좌표
    int blockX;
    int blockY;
    private JLabel blockLabel;
    //arrangePanel용 변수
    int x,y,w,h,randscope,life,speed;
    int playerX,playerY,playerW,playerH,bulletW,bulletH,fireTimeMs,bulletDistance;
    private ImageIcon mouseNormalBlock;
    private ImageIcon mouseNormalPlayerBlock;
    private ItemPane itemPane;
    private boolean isSaved = false;
    private ArrangeTabPanel arrangeTabPanel;
    //속성팬에 보일 이미지
    private ImageIcon userNewBlock;
    private ImageIcon userNewShotBlock;
    private ImageIcon userNewPlayerBlock;
    private ImageIcon userNewBullet;
    private int [] blockSource; //x,y,w,h,randscope,life,speed
    private int [] defaultBlockSource = {100,100,100,100,10,10,10};
    private String defaultImg = "C:\\Users\\서준영\\Desktop\\HSU\\과제\\3-1\\project_java\\mycode\\Editor\\images\\block.png";
    private String defaultPlayerImg = "C:\\Users\\서준영\\Desktop\\HSU\\과제\\3-1\\project_java\\mycode\\Editor\\images\\jet.png";
    private int [] playerSource; //w,h,x,y,Bw,Bh,fms,Bd
    private int [] defaultPlayerSource = {100,100,500,500,20,50,10,10};
    private MainAuthorFrame mainAuthorFrame;
    private PropertyPane propertyPane;
    private String imgFilePath;
    private String shotImgFilePath;
    private String playerImgFilePath;
    private String bulletImgFilePath;
    private ImageIcon icon,shotIcon;
    private boolean isPlayer;
    //일단 생성됐다면, 속성 변경 다이얼로그를 띄운다
    //처음 아이템팬에서 누르는 경우에는 false일 것.
    private boolean forChange=true;
    private JLabel handlerLabel;

    //ItemPane 표기용 블록 생성자
    public Block(int blockX, int blockY, ItemPane itemPane,boolean isPlayer) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.itemPane=itemPane;
        this.isPlayer=isPlayer;
        this.arrangeTabPanel=itemPane.getArrangeTabPanel();
        this.forChange=false;
        normalBlocks=itemPane.getNormalBLocks();
        this.mouseNormalBlock=itemPane.getMouseNormalBlock();
        this.mouseNormalPlayerBlock = itemPane.getPlayerBlock();
        blockLabel = new JLabel();
        blockSource = new int[7];
        playerSource = new int[8];
        if(!isPlayer) {
            blockLabel.setIcon(mouseNormalBlock);
        }
        if(isPlayer){
            blockLabel.setIcon(mouseNormalPlayerBlock);
        }
        blockLabel.setSize(80, 80);
        BlockMouseListener blc = new BlockMouseListener();
        blockLabel.addMouseListener(blc);
    }
    //arrangeTabPanel에서 호출돼서 모든 속성을 가지는 블록
    public Block(int x, int y, int w, int h, int randscope, int life, int speed,
                 String imgFilePath, String shotImgFilePath,
                 MainAuthorFrame mainAuthorFrame, PropertyPane propertyPane){
        isPlayer=false;

        this.x=x;this.y=y;this.w=w;this.h=h; this.randscope=randscope;this.life=life;
        this.speed=speed; this.imgFilePath=imgFilePath;this.shotImgFilePath=shotImgFilePath;
        this.mainAuthorFrame=mainAuthorFrame; this.propertyPane=propertyPane;
        this.arrangeTabPanel=mainAuthorFrame.getArrangePanel().getArrangeTabPanel();
        blockSource= new int[]{x, y, w, h, randscope, life, speed};
        userNewBlock = makeImage(new ImageIcon(imgFilePath),w,h);
        blockLabel = new JLabel();
        blockLabel.setIcon(userNewBlock);

        userNewShotBlock = makeImage(new ImageIcon(shotImgFilePath),w,h);

        blockLabel.setSize(w,h);
        blockLabel.setLocation(x,y);
        BlockMouseListener blc = new BlockMouseListener();
        blc.isItemPane=false;
        blockLabel.addMouseListener(blc);
        blockLabel.addMouseMotionListener(blc);
        blockLabel.addMouseWheelListener(blc);
        mainAuthorFrame.arrangeBlocks.add(this);

    }

    //플레이어용 블록
    public Block(int playerW, int playerH, int playerX, int playerY, int bulletW, int bulletH, int fireTimeMs,
                 int bulletDistance, String playerImgFilePath, String bulletImgFilePath,
                 MainAuthorFrame mainAuthorFrame, PropertyPane propertyPane){
        isPlayer=true;
        this.playerW=playerW; this.playerH=playerH; this.playerX=playerX; this.playerY=playerY;
        this.bulletW=bulletW; this.bulletH=bulletH; this.fireTimeMs=fireTimeMs; this.bulletDistance=bulletDistance;
        this.playerImgFilePath=playerImgFilePath; this.bulletImgFilePath=bulletImgFilePath;
        this.mainAuthorFrame=mainAuthorFrame; this.propertyPane=propertyPane;
        this.arrangeTabPanel=mainAuthorFrame.getArrangePanel().getArrangeTabPanel();
        playerSource= new int[]{playerX,playerY,playerW,playerH,bulletW,bulletH,fireTimeMs,bulletDistance};
        userNewPlayerBlock = makeImage(new ImageIcon(playerImgFilePath),playerW,playerH);
        blockLabel = new JLabel();
        blockLabel.setIcon(userNewPlayerBlock);
        userNewBullet = makeImage(new ImageIcon(bulletImgFilePath),bulletW,bulletH);

        blockLabel.setSize(playerW,playerH);
        blockLabel.setLocation(playerX,playerY);
        BlockMouseListener blc = new BlockMouseListener();
        blc.isItemPane=false;
        blockLabel.addMouseListener(blc);
        blockLabel.addMouseMotionListener(blc);
        blockLabel.addMouseWheelListener(blc);
        mainAuthorFrame.playerBlocks.add(this);
    }
    public void selectBlock(Block block) {
        if (block.isChoosed) {
            block.getBlockLabel().setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            arrangeTabPanel.repaint();
        }
        else {
            block.getBlockLabel().setBorder(null);
        }
    }

    public void changeBlock(int [] blockSource,String imgFilePath,String shotImgFilePath){
       this.x = blockSource[0]; this.y = blockSource[1]; this.w= blockSource[2]; this.h =blockSource[3];
        this.randscope = blockSource[4]; this.life = blockSource[5]; this.speed = blockSource[6];
        this.imgFilePath=imgFilePath; this.shotImgFilePath=shotImgFilePath;
        userNewBlock = makeImage(new ImageIcon(imgFilePath),w,h);
        userNewShotBlock = makeImage(new ImageIcon(shotImgFilePath),w,h);
        blockLabel.setSize(w,h);
        blockLabel.setLocation(x,y);
        blockLabel.setIcon(userNewBlock);
        arrangeTabPanel.repaint();
    }
    //w,h,x,y,Bw,Bh,fms,Bd
    public void changePlayer(int [] playerSource,String playerImgFilePath,String bulletImgFilePath){
        this.playerW = playerSource[0]; this.playerH = playerSource[1]; this.playerX= playerSource[2]; this.playerY =playerSource[3];
        this.bulletW = playerSource[4]; this.bulletH = playerSource[5]; this.bulletDistance = playerSource[6];
        this.playerImgFilePath=playerImgFilePath; this.bulletImgFilePath=bulletImgFilePath;
        userNewPlayerBlock = makeImage(new ImageIcon(imgFilePath),playerW,playerH);
        userNewBullet = makeImage(new ImageIcon(bulletImgFilePath),bulletW,bulletH);
        blockLabel.setSize(playerW,playerH);
        blockLabel.setLocation(playerX,playerY);
        blockLabel.setIcon(userNewPlayerBlock);
        arrangeTabPanel.repaint();
    }

    public void setPointForBlock(Point p){
        this.x=p.x; this.y=p.y;
    }
    public void setPointForPlayer(Point p){
        this.playerX=p.x; this.playerY=p.y;
    }
    public Block findBlockInItemPane(JLabel block) {
        for (Block b : normalBlocks) {
            if (b.getBlockLabel() == block) {
                return b;
            }
        }
        return null;
    }
    public ImageIcon makeImage(ImageIcon original,int w,int h){
        Image newImg = original.getImage();
        Image scaledNewImg = newImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        ImageIcon result = new ImageIcon(scaledNewImg);
        return result;
    }
    public Block findBlockInArrangePane(JLabel block) {
        for (Block b : mainAuthorFrame.arrangeBlocks) {
            if (b.getBlockLabel() == block) {
                return b;
            }
        }
        for(Block b : mainAuthorFrame.playerBlocks){
            if(b.getBlockLabel() == block){
                return b;
            }
        }
        return null;
    }
    //<Obj x="random" y="random"w="100" h="100" randscope="100" life="3"
    // speed="5" img="images/monster2.png" shotimg="images/shot.png"/>
    public String blockToXML() {
        return "<Obj x=\"" + x + "\" y=\"" + y + "\" w=\"" + w + "\" h=\"" + h +
                "\" randscope=\"" + randscope + "\" life=\"" + life +
                "\" speed=\"" + speed + "\" img=\"" + imgFilePath +
                "\" shotimg=\"" + shotImgFilePath + "\"/>\n";
    }
    public String playerToXML(){
        return "<BulletBlock blockw=\"" + playerW + "\" blockh=\"" + playerH +
                "\" blockx=\"" + playerX + "\" blocky=\"" + playerY +
                "\" bulletw=\"" + bulletW + "\" bulleth=\"" + bulletH +
                "\" firetimems=\"" + fireTimeMs + "\" bulletdistance=\"" + bulletDistance +
                "\" blockimg=\"" + playerImgFilePath + "\" bulletimg=\"" + bulletImgFilePath + "\"/>\n";
    }

    public int getBlockX() {
        return blockX;
    }
    public ImageIcon getUserNewPlayerBlock(){
        return userNewPlayerBlock;
    }
    public ImageIcon getUserNewBullet(){
        return userNewBullet;
    }
    public String getImgFilePath(){return imgFilePath;}
    public String getShotImgFilePath(){return shotImgFilePath;}
    public int getRandscope(){return randscope;}
    public int getLife(){return life;}
    public int getSpeed(){return speed;}
    public int getBulletW(){return bulletW;}
    public int getBulletH(){return bulletH;}
    public int getFireTimeMs(){return fireTimeMs;}
    public int getBulletDistance(){return bulletDistance;}
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

    class BlockMouseListener extends MouseAdapter {
        BlockDialogForBlock blockDialogForBlock =new BlockDialogForBlock();
        BlockDialogForPlayer blockDialogForPlayer = new BlockDialogForPlayer();
        private Point initialClick; // 드래그 시작 위치
        private boolean isItemPane = true;
        @Override
        public void mouseWheelMoved(MouseWheelEvent e){
            int wheelMove = e.getWheelRotation();
            if(wheelMove<0 && isChoosed){ //up
                int newWidth = blockLabel.getWidth()+10;
                int newHeight = blockLabel.getHeight()+10;
                blockLabel.setSize(newWidth,newHeight);
                if(!isPlayer){
                    userNewBlock = makeImage(userNewBlock,newWidth,newHeight);
                    blockLabel.setIcon(userNewBlock);
                }
                else{
                    userNewPlayerBlock = makeImage(userNewPlayerBlock,newWidth,newHeight);
                    blockLabel.setIcon(userNewPlayerBlock);
                }
            }
            if(wheelMove>0 && isChoosed){//down
                int newWidth = blockLabel.getWidth()-10;
                int newHeight = blockLabel.getHeight()-10;
                blockLabel.setSize(newWidth,newHeight);
                blockLabel.setSize(newWidth,newHeight);
                if(!isPlayer){
                    userNewBlock = makeImage(userNewBlock,newWidth,newHeight);
                    blockLabel.setIcon(userNewBlock);
                }
                else{
                    userNewPlayerBlock = makeImage(userNewPlayerBlock,newWidth,newHeight);
                    blockLabel.setIcon(userNewPlayerBlock);
                }
            }
            arrangeTabPanel.repaint();
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if(isItemPane) {//오른쪽 아이템 팬의 블록
                if(e.getButton()==MouseEvent.BUTTON3 &&!isPlayer && !isSaved){
                    arrangeTabPanel.setBlockForBlock(defaultBlockSource,defaultImg,defaultImg);
                }
                if(e.getButton()==MouseEvent.BUTTON3 && isPlayer&& !isSaved){
                    arrangeTabPanel.setBlockForPlayer(defaultPlayerSource,defaultPlayerImg,defaultPlayerImg);
                }
                if(e.getButton()==MouseEvent.BUTTON3 && !isPlayer && isSaved){
                    arrangeTabPanel.setBlockForBlock(blockSource,imgFilePath,shotImgFilePath);
                }
                if(e.getButton()==MouseEvent.BUTTON3&&isPlayer &&isSaved){
                    arrangeTabPanel.setBlockForPlayer(playerSource,playerImgFilePath,bulletImgFilePath);
                }
                if (e.getClickCount() == 2 && !isSaved && !isPlayer) {
                    JLabel block = (JLabel) e.getSource();
                    Block b = findBlockInItemPane(block);
                    blockDialogForBlock.setVisible(true);
                }
                if (e.getClickCount() == 2 && isSaved && !isPlayer) {
                    JLabel block = (JLabel) e.getSource();
                    Block b = findBlockInItemPane(block);
                    blockDialogForBlock.setVisible(true);
                }
                if(e.getClickCount()==2 && !isSaved && isPlayer){
                    JLabel block = (JLabel) e.getSource();
                    Block b = findBlockInItemPane(block);
                    blockDialogForPlayer.setVisible(true);
                }
                if(e.getClickCount()==2 && isSaved && isPlayer){
                    JLabel block = (JLabel) e.getSource();
                    Block b = findBlockInItemPane(block);
                    blockDialogForPlayer.setVisible(true);
                }
            }
            if(!isItemPane){
                if(e.getButton()==3){
                    JLabel block = (JLabel) e.getSource();
                    Block b = findBlockInArrangePane(block);
                    arrangeTabPanel.remove(b.getBlockLabel());
                    arrangeTabPanel.repaint();
                    if(b.isPlayer) mainAuthorFrame.playerBlocks.remove(b);
                    if(!b.isPlayer) mainAuthorFrame.arrangeBlocks.remove(b);
                }
                if(e.getClickCount()==2 && !isPlayer){
                    JLabel block = (JLabel) e.getSource();
                    Block b = findBlockInArrangePane(block);
                    b.blockSource[0] = b.getBlockLabel().getX();
                    b.blockSource[1] = b.getBlockLabel().getY();
                    blockDialogForBlock.setBlockAttributes(b.blockSource, b.imgFilePath, b.shotImgFilePath);
                    blockDialogForBlock.setVisible(true);
                }
                if(e.getClickCount()==2 && isPlayer){
                    JLabel block = (JLabel) e.getSource();
                    Block b = findBlockInArrangePane(block);
                    b.playerSource[0] = b.getBlockLabel().getX();
                    b.playerSource[1] = b.getBlockLabel().getY();
                    blockDialogForPlayer.setBlockAttributes(b.playerSource, b.playerImgFilePath, b.bulletImgFilePath);
                    blockDialogForPlayer.setVisible(true);
                }
            }
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            if(!isItemPane) {
                JLabel blockLabel = (JLabel) e.getSource();
                Block b = findBlockInArrangePane(blockLabel);
                propertyPane.setProperties(b,isPlayer);
                Point currentLocation = blockLabel.getLocation();
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                int newX = currentLocation.x + xMoved;
                int newY = currentLocation.y + yMoved;
                blockLabel.setLocation(newX, newY);
                if(!isPlayer)b.setPointForBlock(new Point(newX,newY));
                if(isPlayer)b.setPointForPlayer(new Point(newX,newY));
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
            initialClick = e.getPoint(); // 초기 클릭 위치 저장
            if(isItemPane){
            JLabel block = (JLabel) e.getSource();
            Block b = findBlockInItemPane(block);
            for (int i = 0; i < normalBlocks.size(); i++) {
                normalBlocks.get(i).isChoosed = false;
            }
            if (b != null) {
                b.isChoosed = true;
                itemPane.repaint();
            }
            }
            if(!isItemPane){
                JLabel block = (JLabel) e.getSource();
                Block b = findBlockInArrangePane(block);
                for(int i=0;i<mainAuthorFrame.arrangeBlocks.size();i++){
                    mainAuthorFrame.arrangeBlocks.get(i).isChoosed=false;
                    selectBlock(mainAuthorFrame.arrangeBlocks.get(i));
                }
                for(int i=0;i<mainAuthorFrame.playerBlocks.size();i++){
                    mainAuthorFrame.playerBlocks.get(i).isChoosed=false;
                    selectBlock(mainAuthorFrame.playerBlocks.get(i));
                }
                if(b!=null){
                    b.isChoosed=true;
                    selectBlock(b);
                    arrangeTabPanel.repaint();
                }
            }
        }
}
        class BlockDialogForBlock extends JDialog {
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
            private JTextField[] inputFields = new JTextField[7];
            private JFileChooser chooser;
            private JTextField imgFileTextField;
            private JTextField shotImgFileTextField;

            public BlockDialogForBlock() {
                setModal(true);
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
                setLocationRelativeTo(null);

            }
            private boolean checkNull(){
                for(int i=0; i<inputFields.length;i++){
                    if(inputFields[i].getText().equals("")){
                        showMessageDialog(this, "입력되지 않은 값이 있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                        return true;
                    }
                }
                if(shotImgFilePath==null||imgFilePath==null ){
                    showMessageDialog(this, "image를 설정하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                    return true;
                }
                return false;
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
                    if(!checkNull()) {
                        setVisible(false);
                        for (int i = 0; i < blockSource.length; i++) {
                            //팔레트에 블록 속성을 저장한 경우
                            blockSource[i] = Integer.parseInt(inputFields[i].getText());
                        }
                        isSaved = true;
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
                    setVisible(false);
                    for(int i=0;i<blockSource.length;i++){
                        blockSource[i] = Integer.parseInt(inputFields[i].getText());
                        isSaved=true;
                    }
                    if(!forChange)
                    arrangeTabPanel.setBlockForBlock(blockSource,imgFilePath,shotImgFilePath);
                    if(forChange){
                        changeBlock(blockSource,imgFilePath,shotImgFilePath);
                    }
                }
            }
            public void setBlockAttributes(int[] blockSource, String imgFilePath, String shotImgFilePath) {
                for (int i = 0; i < blockSource.length; i++) {
                    inputFields[i].setText(String.valueOf(blockSource[i]));
                }
                imgFileTextField.setText(imgFilePath);
                shotImgFileTextField.setText(shotImgFilePath);
            }
        }
        class BlockDialogForPlayer extends JDialog{
            private JLabel xLabel = new JLabel("플레이어 x 좌표 : ");
            private JLabel yLabel = new JLabel("플레이어 y 좌표 : ");
            private JLabel wLabel = new JLabel("플레이어 너비 : ");
            private JLabel hLabel = new JLabel("플레이어 높이 : ");
            private JLabel bulletWLabel = new JLabel("총알 너비 : ");
            private JLabel bulletHLabel = new JLabel("총알 높이 : ");
            private JLabel fireTimeMsLabel = new JLabel("발사 속도 : ");
            private JLabel bulletImgLabel = new JLabel("총알 이미지 : ");
            private JLabel bulletDistanceLabel = new JLabel("총알 이동 거리 : ");
            private JLabel playerBlockImgLabel = new JLabel("플레이어 이미지 : ");
            private JButton ok = new JButton("저장");
            private JLabel[] attrs;
            private JTextField[] inputFields = new JTextField[8];
            private JFileChooser chooser;

            private JTextField playerImgFileTextField;
            private JTextField bulletImgFileTextField;
            public BlockDialogForPlayer(){
                setModal(true);
                setLayout(null);
                setResizable(false);
                attrs = new JLabel[]{wLabel,hLabel,xLabel,yLabel,bulletWLabel,bulletHLabel,fireTimeMsLabel, bulletDistanceLabel};
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
                playerBlockImgLabel.setSize(300,20);
                playerBlockImgLabel.setFont(new Font("고딕체",Font.BOLD,15));
                playerBlockImgLabel.setLocation(5,285);
                bulletImgLabel.setSize(300,20);
                bulletImgLabel.setFont(new Font("고딕체",Font.BOLD,15));
                bulletImgLabel.setLocation(5,320);
                add(playerBlockImgLabel); add(bulletImgLabel);
                playerImgFileTextField = new JTextField(30);
                playerImgFileTextField.setEnabled(false);
                playerImgFileTextField.setSize(200,20);
                playerImgFileTextField.setLocation(200,285);
                bulletImgFileTextField = new JTextField(30);
                bulletImgFileTextField.setEnabled(false);
                bulletImgFileTextField.setSize(200,20);
                bulletImgFileTextField.setLocation(200,320);
                add(playerImgFileTextField); add(bulletImgFileTextField);
                setSize(500, 570);
                addImageButton();
                setLocationRelativeTo(null);

            }
            private boolean checkNull(){
                for(int i=0; i<inputFields.length;i++){
                    if(inputFields[i].getText().equals("")){
                        showMessageDialog(this, "입력되지 않은 값이 있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                        return true;
                    }
                }
                if(bulletImgFilePath==null||playerImgFilePath==null ){
                    showMessageDialog(this, "image를 설정하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                    return true;
                }
                return false;
            }
            public void setBlockAttributes(int[] playerSource, String imgFilePath, String shotImgFilePath) {
                for (int i = 0; i < playerSource.length; i++) {
                    inputFields[i].setText(String.valueOf(playerSource[i]));
                }
                playerImgFileTextField.setText(imgFilePath);
                bulletImgFileTextField.setText(shotImgFilePath);
            }
            private void addImageButton(){
                JButton imgButton = new JButton("플레이어 이미지 추가");
                imgButton.addActionListener(new BlockDialogForPlayer.imgButtonActionListener());
                JButton shotImgButton = new JButton("총알 이미지 추가");
                shotImgButton.addActionListener(new BlockDialogForPlayer.imgButtonActionListener());
                JButton arrangeButton = new JButton("블록 배치하기");
                arrangeButton.setSize(150,30);
                arrangeButton.setLocation(150,470);
                imgButton.setSize(150,30);
                shotImgButton.setSize(150,30);
                imgButton.setLocation(30,390);
                shotImgButton.setLocation(270,390);
                ok.setSize(60, 30);
                ok.setLocation(190, 430);
                add(ok);
                ok.addActionListener(new BlockDialogForPlayer.okButtonActionListener());
                arrangeButton.addActionListener(new BlockDialogForPlayer.arrangeButtonActionListener());
                add(imgButton);
                add(shotImgButton);
                add(arrangeButton);
            }
            class imgButtonActionListener implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e){
                    JButton button = (JButton)(e.getSource());
                    chooser = new JFileChooser("C:\\Users\\서준영\\Desktop\\HSU\\과제\\3-1\\project_java\\mycode\\Editor\\images");
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG & GIF Images"
                            , "jpg", "png", "gif");
                    chooser.setFileFilter(filter);
                    int result = chooser.showOpenDialog(null);
                    if (result != JFileChooser.APPROVE_OPTION) return;
                    if(button.getText().equals("플레이어 이미지 추가")){
                        playerImgFilePath = chooser.getSelectedFile().getPath();
                        Image newBlockImg = new ImageIcon(playerImgFilePath).getImage();
                        Image scaledBlockImg = newBlockImg.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                        userNewPlayerBlock = new ImageIcon(scaledBlockImg);
                        blockLabel.setIcon(userNewPlayerBlock);
                        playerImgFileTextField.setText(playerImgFilePath);
                        repaint();
                    }
                    if(button.getText().equals("총알 이미지 추가")){
                        bulletImgFilePath = chooser.getSelectedFile().getPath();
                        Image newShotBlockImg = new ImageIcon(bulletImgFilePath).getImage();
                        Image scaledImg = newShotBlockImg.getScaledInstance(80,80,Image.SCALE_SMOOTH);
                        userNewBullet = new ImageIcon(scaledImg);
                        bulletImgFileTextField.setText(bulletImgFilePath);
                    }
                }
            }
            class okButtonActionListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!checkNull()) {
                        setVisible(false);
                        for (int i = 0; i < playerSource.length; i++) {
                            playerSource[i] = Integer.parseInt(inputFields[i].getText());
                        }
                        isSaved = true;
                    }
                }
            }
            class arrangeButtonActionListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    for(int i=0;i<playerSource.length;i++){
                        playerSource[i] = Integer.parseInt(inputFields[i].getText());
                        isSaved=true;
                    }
                    if(!forChange)
                        arrangeTabPanel.setBlockForPlayer(playerSource, playerImgFilePath, bulletImgFilePath);
                    if(forChange){
                        changePlayer(playerSource,playerImgFilePath, bulletImgFilePath);
                    }
                }
            }
        }
    }

