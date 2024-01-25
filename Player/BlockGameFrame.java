import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class BlockGameFrame extends JFrame {
    private int w;
    private int h;

    private String shotMode;
    private String blockDirection;
    private String bulletBlockDirection;
    private String blockMode;
    private ImageIcon bgImg;
    private File bgMusic;
    private File fireSound;
    BlockGameFrame() {
        new BlockGameMenuBar(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //초기 사이즈
        setSize(800,600);
        setVisible(true);
    }
    public void loadGame(File file){
        //System.out.println(file.toString());
        String XMLFile = getRelativePath(file);
        //System.out.println(XMLFile);
        XMLReader xml = new XMLReader(XMLFile);
        Node settingNode = xml.getSettingElement();
        Node bgNode = XMLReader.getNode(settingNode, XMLReader.E_BG);
        Node sizeNode = XMLReader.getNode(settingNode, XMLReader.E_SIZE);
        Node gameMode = XMLReader.getNode(settingNode,XMLReader.E_GAMEMODE);
        NodeList bgNodeList = bgNode.getChildNodes();
        for(int i=0;i<bgNodeList.getLength();i++){
            Node node = bgNodeList.item(i);
            if(node.getNodeType()!=Node.ELEMENT_NODE)
                continue;
            if(node.getNodeName().equals(XMLReader.E_BGIMG)){
                bgImg = new ImageIcon(XMLReader.getAttr(node,"bgImg"));
            }
            if(node.getNodeName().equals(XMLReader.E_BGMUSIC)){
                bgMusic = new File(XMLReader.getAttr(node,"bgMusic"));
            }
            if(node.getNodeName().equals(XMLReader.E_FIRESOUND)){
                fireSound = new File(XMLReader.getAttr(node,"fireSound"));
            }
        }
        NodeList gameModeList = gameMode.getChildNodes();
        for(int i=0;i<gameModeList.getLength();i++){
            Node node = gameModeList.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            if (node.getNodeName().equals(XMLReader.E_SHOTMODE)){
                shotMode=XMLReader.getAttr(node,"shotMode");
                //System.out.println("ShotMode :" + shotMode);
            }
            if(node.getNodeName().equals(XMLReader.E_BLOCKDIRECTION)){
                blockDirection=XMLReader.getAttr(node,"blockDirection");
                //System.out.println("BlockDirection : "+ blockDirection);
            }
            if(node.getNodeName().equals(XMLReader.E_BULLETBLOCKDIRECTION)){
                bulletBlockDirection = XMLReader.getAttr(node,"bulletBlockDirection");
                //System.out.println("bulletBlockDirection : " + bulletBlockDirection);
            }
            if(node.getNodeName().equals(XMLReader.E_BLOCKMODE)){
                blockMode = XMLReader.getAttr(node,"blockMode");
                //System.out.println("blockMode : " + blockMode);
            }
        }
        w = Integer.parseInt(XMLReader.getAttr(sizeNode, "w"));
        h = Integer.parseInt(XMLReader.getAttr(sizeNode, "h"));
        setSize(w,h);
        GamePanel gamePanel = new GamePanel(xml.getGamePanelElement(),bgImg,bgMusic,fireSound,w,h,shotMode,blockDirection,bulletBlockDirection,blockMode);
        setContentPane(gamePanel);
        setResizable(false);
        //System.out.println("bd: "+blockDirection);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
    }
    private String getRelativePath(File file) {
        String currentWorkingDir = System.getProperty("user.dir");
        String absolutePath = file.getAbsolutePath();
        //System.out.println("current : "+currentWorkingDir);
        //System.out.println("absolute : "+absolutePath);
        return absolutePath.substring(currentWorkingDir.length() + 1);
    }
    public static void main(String[] args) {
        new BlockGameFrame();
    }
}

class Block extends JLabel{
    private int stagePlusSpeed,stage,currentSpeed,sleepTime;
    private int randScope;
    private Vector<JLabel> bulletVector;
    private ImageIcon scaledShotImg;
    private int life;
    private int x,y,w,h,frameH,speed,frameW;
    JLabel blockLabel=new JLabel();
    private GamePanel gamePanel;
    private BulletBlock b;
    private JLabel bulletLabel;
    private String blockDirection;
    private boolean isSpecialBlock = false;
    private ImageIcon icon;
    private ImageIcon shoticon;
    //랜덤이 아닌경우 블록 생성자
    public Block(GamePanel gamePanel, int x, int y, int w, int h, int frameW, int frameH, ImageIcon icon, ImageIcon shoticon, int life, int speed, String blockDirection) {
        this.x=x; this.y=y; this.w=w; this.h=h; this.frameH=frameH; this.gamePanel=gamePanel;
        this.speed=speed; this.frameW=frameW; this.blockDirection=blockDirection; this.icon=icon; this.shoticon=shoticon;
        stagePlusSpeed=gamePanel.getStagePlusSpeed();
        stage = gamePanel.getStage();
        currentSpeed = speed + (stagePlusSpeed * stage); // 현재 스테이지의 총 속도
        sleepTime = Math.max(5, 100 - currentSpeed); // 5ms는 최소 `sleep` 시간
        //이미지 Label 크기에 맞게 조정
        Image scaledImage = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        Image scaledShot = shoticon.getImage().getScaledInstance(w,h,Image.SCALE_SMOOTH);
        scaledShotImg = new ImageIcon(scaledShot);
        blockLabel.setSize(w,h);
        b=gamePanel.getBulletBlock();
        blockLabel.setLocation(x,y);
        this.life = life;
        blockLabel.setText(String.valueOf(life));
        blockLabel.setFont(new Font("고딕체",Font.BOLD,30));
        blockLabel.setHorizontalTextPosition(JLabel.CENTER);
        blockLabel.setVerticalTextPosition(JLabel.CENTER);
        blockLabel.setIcon(scaledIcon);
        CheckCollisionThread checkCollisionThread = new CheckCollisionThread();
        checkCollisionThread.start();
        //System.out.println("blockdirecion : " +blockDirection);
        if(blockDirection.equals("down")){
            BlockMoveDownThread blockMoveDownThread = new BlockMoveDownThread();
            blockMoveDownThread.start();
        }
        else if(blockDirection.equals("cross")){
            BlockMoveCrossThread blockMoveCrossThread = new BlockMoveCrossThread();
            blockMoveCrossThread.start();
        }
        else if(blockDirection.equals("chase")){
            BlockMoveChaseThread blockMoveChaseThread= new BlockMoveChaseThread();
            blockMoveChaseThread.start();
        }

    }
    //랜덤용 블록 생성자
    public Block(GamePanel gamePanel, int x, int y, int w, int h, int randScope, int frameW, int frameH, ImageIcon icon, ImageIcon shoticon, int life, int speed, String blockDirection) {
        //System.out.println("random block created");
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.frameH = frameH;
        this.gamePanel = gamePanel;
        this.speed = speed;
        this.icon=icon; this.shoticon=shoticon;
        this.frameW = frameW;
        this.blockDirection = blockDirection;
        stagePlusSpeed=gamePanel.getStagePlusSpeed();
        stage = gamePanel.getStage();
        currentSpeed = speed + (stagePlusSpeed * stage); // 현재 스테이지의 총 속도
        sleepTime = Math.max(5, 100 - currentSpeed); // 5ms는 최소 `sleep` 시간
        //이미지 Label 크기에 맞게 조정
        this.randScope = randScope;
        Image scaledImage = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        Image scaledShot = shoticon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
        scaledShotImg = new ImageIcon(scaledShot);
        blockLabel.setSize(w, h);
        blockLabel.setLocation(x, y);
        this.life = life;
        blockLabel.setText(String.valueOf(life));
        blockLabel.setFont(new Font("고딕체", Font.BOLD, 30));
        blockLabel.setHorizontalTextPosition(JLabel.CENTER);
        blockLabel.setVerticalTextPosition(JLabel.CENTER);
        blockLabel.setIcon(scaledIcon);
        CheckCollisionThread checkCollisionThread = new CheckCollisionThread();
        checkCollisionThread.start();
        BlockMoveRandomThread blockMoveRandomThread = new BlockMoveRandomThread();
        blockMoveRandomThread.start();
    }
    public JLabel getBlockLabel() {return blockLabel;}
    //각각의 블록마다 스레드 붙이기
    //충돌 감지 스레드
    class CheckCollisionThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Component[] components = gamePanel.getComponents();
                    for (int i = 0; i < components.length; i++) {
                        if (components[i] instanceof JLabel && "Bullet".equals(components[i].getName())) {
                            Rectangle bulletBounds = components[i].getBounds();
                            if (blockLabel.getBounds().intersects(bulletBounds)) {
                                gamePanel.remove(components[i]);
                                gamePanel.repaint();
                                life--;
                                if (life <= 0) {
                                    blockLabel.setIcon(scaledShotImg);
                                    blockLabel.setText(String.valueOf(life));
                                    gamePanel.increase();
                                    gamePanel.decreaseBlockCount();
                                    gamePanel.repaint();
                                    sleep(1000);
                                    gamePanel.remove(blockLabel);
                                    gamePanel.repaint();
                                    this.interrupt();
                                }
                                if(isSpecialBlock){
                                    specialDestroyed();
                                }
                                blockLabel.setText(String.valueOf(life));
                            }
                        }
                    }
                    sleep(1);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    class BlockMoveChaseThread extends Thread{
        @Override
        public void run(){
            while(true){
                try{
                  //  System.out.println("Chase mode");
                    if (b.getBulletBlock().getX() < blockLabel.getX()) {
                        blockLabel.setLocation(blockLabel.getX() - 5, blockLabel.getY());
                    }
                    if (b.getBulletBlock().getX() > blockLabel.getX()) {
                        blockLabel.setLocation(blockLabel.getX() + 5, blockLabel.getY());
                    }
                    if (b.getBulletBlock().getY() < blockLabel.getY()) {
                        blockLabel.setLocation(blockLabel.getX(), blockLabel.getY() - 5);
                    }
                    if (b.getBulletBlock().getY() > blockLabel.getY()) {
                        blockLabel.setLocation(blockLabel.getX(), blockLabel.getY() + 5);
                    }
                    sleep(100);

                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
    class BlockMoveCrossThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                   // System.out.println("RandScope : "+randScope);
                    blockLabel.setLocation(blockLabel.getX()-speed, blockLabel.getY());
                    if (blockLabel.getX() < 0 ) {
                        gamePanel.remove(blockLabel);
                        gamePanel.repaint();
                        gamePanel.decreaseBlockCount();
                        this.interrupt();
                    }
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
    class BlockMoveDownThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    blockLabel.setLocation(blockLabel.getX(), blockLabel.getY() + speed);
                    if (blockLabel.getY()> frameH) {
                        gamePanel.remove(blockLabel);
                        gamePanel.repaint();
                        gamePanel.decreaseBlockCount();
                        this.interrupt();
                    }
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    class BlockMoveRandomThread extends Thread{
        private int randIntX;
        private int randIntY;
        @Override
        public void run(){
            //System.out.println("FrameH : "+frameH);
            //System.out.println("FrameW : "+frameW);
            while (true) {
                randIntX = (int) (Math.random() * randScope) - randScope / 2;
                randIntY = (int) (Math.random() * randScope) - randScope / 2;

                try {
                    int newX = Math.max(0, Math.min(frameW - blockLabel.getWidth(), blockLabel.getX() + randIntX));
                    int newY = Math.max(0, Math.min(frameH - blockLabel.getHeight(), blockLabel.getY() + randIntY));

                    blockLabel.setLocation(newX, newY);
                    gamePanel.repaint();

                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    return;
                }
            }

        }
    }
    public void setIsSpecialBlockFlag(){
        isSpecialBlock=true;
    }
    public void specialDestroyed(){
        speed-=100;
    }
    public int getX(){return x;}
    public int getY(){return y;}
    public int getW(){return w;}
    public int getH(){return h;}
    public int getRandScope(){return randScope;}
    public int getFrameW(){return frameW;}
    public int getFrameH(){return frameH;}
    public ImageIcon getIcon(){return icon;}
    public ImageIcon getShoticon(){return shoticon;}
    public int getLife(){return life;}
    public int getSpeed(){return speed;}
    public String getBlockDirection(){return blockDirection;}
}




class GamePanel extends JPanel {
    int score = 0;
    int stage=1;
    private int stagePlusSpeed;
    int scoreGap;
    private ImageIcon forright,forleft,forup,fordown;
    ImageIcon bgImg;
    int x,y,w,h,randScope,life,speed;
    private int frameW;
    private int frameH;
    private JLabel scoreLabel;
    private JLabel stageLabel;
    JLabel bulletBlockLabel;
    String shotMode;
    String blockDirection;
    String bulletBlockDirection;
    private int maxBlockCount=0;
    private String scoreContent;
    private String stageContent;
    private ImageIcon icon,shotIcon;
    int howManyBlocksNow=0;
    private String blockMode;
    private MainGameThread mainGameThread;
    private File bgMusic;

    int[] fontStyles = {
            Font.PLAIN,  // 기본 스타일
            Font.BOLD,   // 굵은 스타일
            Font.ITALIC  // 이탤릭 스타일
    };
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
    private Node gamePanelNode;
    private Vector<Block> infiniteBlocks = new Vector<>(10);
    private BulletBlock b;
    private String lastDirection="left";
    private File fireSound;
    public GamePanel(Node gamePanelNode, ImageIcon bgImg, File bgMusic, File fireSound, int frameW, int frameH, String shotMode, String blockDirection, String bulletBlockDirection, String blockMode) {
        //System.out.println("gamePanel: "+blockMode);
        setLayout(null);
        this.bgImg=bgImg;
        this.fireSound = fireSound;
        this.bgMusic=bgMusic;
        this.gamePanelNode=gamePanelNode;
        this.bulletBlockDirection=bulletBlockDirection;
        this.blockDirection=blockDirection;
        this.shotMode=shotMode;
        this.frameW = frameW;
        this.frameH = frameH;
        this.blockMode=blockMode;
        // read <Fish><Obj>s from the XML parse tree, make Food objects, and add them to the FishBowl panel.
        Node playerNode = XMLReader.getNode(gamePanelNode,XMLReader.E_PLAYER);
        Node scoreNode = XMLReader.getNode(gamePanelNode,XMLReader.E_SCORE);
        Node stageNode = XMLReader.getNode(gamePanelNode,XMLReader.E_STAGE);
        NodeList playerNodeList = playerNode.getChildNodes();
        NodeList scoreNodeList = scoreNode.getChildNodes();
        NodeList stageNodeList = stageNode.getChildNodes();
        for(int i=0;i<scoreNodeList.getLength(); i++){
            Node node = scoreNodeList.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            if(node.getNodeName().equals(XMLReader.E_SCOREBLOCK)){
                int scoreBlockX=Integer.parseInt(XMLReader.getAttr(node,"scoreblockx"));
                int scoreBlockY=Integer.parseInt(XMLReader.getAttr(node,"scoreblocky"));
                int scoreBlockW=Integer.parseInt(XMLReader.getAttr(node,"scoreblockw"));
                int scoreBlockH=Integer.parseInt(XMLReader.getAttr(node,"scoreblockh"));
                scoreContent=XMLReader.getAttr(node,"scorecontent");
                String scoreFontName = XMLReader.getAttr(node,"scorefontname");
                int scoreFontStyle = Integer.parseInt(XMLReader.getAttr(node,"scorefontstyle"));
                int scoreFontSize = Integer.parseInt(XMLReader.getAttr(node,"scorefontsize"));
                int scoreFontColor = Integer.parseInt(XMLReader.getAttr(node,"scorefontcolor"));
                Color fontColor = colors[scoreFontColor];
                scoreLabel = new JLabel();
                scoreLabel.setSize(scoreBlockW,scoreBlockH);
                scoreLabel.setLocation(scoreBlockX,scoreBlockY);
                scoreLabel.setText(scoreContent+ score);
                scoreLabel.setFont(new Font(scoreFontName,fontStyles[scoreFontStyle],scoreFontSize));
                scoreLabel.setForeground(fontColor);
                add(scoreLabel);
            }
            if(node.getNodeName().equals(XMLReader.E_RULE)){
                scoreGap = Integer.parseInt(XMLReader.getAttr(node,"rule"));
               //System.out.println("ScoreGap 파싱결과 : "+scoreGap);
            }
        }
        for(int i=0;i<stageNodeList.getLength();i++){
                Node node = stageNodeList.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                    continue;
                if(node.getNodeName().equals(XMLReader.E_STAGEBLOCK)){
                    int stageBlockX=Integer.parseInt(XMLReader.getAttr(node,"stageblockx"));
                    int stageBlockY=Integer.parseInt(XMLReader.getAttr(node,"stageblocky"));
                    int stageBlockW=Integer.parseInt(XMLReader.getAttr(node,"stageblockw"));
                    int stageBlockH=Integer.parseInt(XMLReader.getAttr(node,"stageblockh"));
                    stageContent=XMLReader.getAttr(node,"stagecontent");
                    String stageFontName = XMLReader.getAttr(node,"stagefontname");
                    int stageFontStyle = Integer.parseInt(XMLReader.getAttr(node,"stagefontstyle"));
                    int stageFontSize = Integer.parseInt(XMLReader.getAttr(node,"stagefontsize"));
                    int stageFontColor = Integer.parseInt(XMLReader.getAttr(node,"stagefontcolor"));
                    Color fontColor = colors[stageFontColor];
                    stageLabel = new JLabel();
                    stageLabel.setSize(stageBlockW,stageBlockH);
                    stageLabel.setLocation(stageBlockX,stageBlockY);
                    stageLabel.setText(stageContent+ stage);
                    stageLabel.setFont(new Font(stageFontName,fontStyles[stageFontStyle],stageFontSize));
                    stageLabel.setForeground(fontColor);
                    add(stageLabel);
                }
                if(node.getNodeName().equals(XMLReader.E_PLUSSPEED)){
                    stagePlusSpeed = Integer.parseInt(XMLReader.getAttr(node,"plusSpeed"));
                    //System.out.println("ScoreGap 파싱결과 : "+stagePlusSpeed);
                }
            }


        for(int i=0;i<playerNodeList.getLength();i++) {
            Node node = playerNodeList.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            if (node.getNodeName().equals(XMLReader.E_BULLETBLOCK)) {
                //System.out.println("불릿블락");
                ImageIcon bulletBlockIcon = new ImageIcon(XMLReader.getAttr(node, "blockimg"));
                int blockW = Integer.parseInt(XMLReader.getAttr(node, "blockw"));
                int blockH = Integer.parseInt(XMLReader.getAttr(node, "blockh"));
                int blockX = Integer.parseInt(XMLReader.getAttr(node,"blockx"));
                int blockY = Integer.parseInt(XMLReader.getAttr(node,"blocky"));
                int bulletW = Integer.parseInt(XMLReader.getAttr(node,"bulletw"));
                int bulletH = Integer.parseInt(XMLReader.getAttr(node,"bulleth"));
                int bulletDistance = Integer.parseInt(XMLReader.getAttr(node,"bulletdistance"));
                int fireTimeMs = Integer.parseInt(XMLReader.getAttr(node,"firetimems"));
                ImageIcon bulletIcon = new ImageIcon(XMLReader.getAttr(node,"bulletimg"));
                b = new BulletBlock(frameW, frameH, blockW, blockH, blockX, blockY, bulletW, bulletH,
                        bulletDistance, fireTimeMs, bulletBlockIcon, bulletIcon, this, bulletBlockDirection
                ,fireSound);
               // System.out.println(bulletBlockDirection);
                bulletBlockLabel = b.getBulletBlock();
                bulletBlockLabel.setFocusable(true);
                bulletBlockLabel.requestFocus();
                add(bulletBlockLabel);
            }
            if(node.getNodeName().equals(XMLReader.E_CHASEMODEBULLETBLOCK)){
                int blockW = Integer.parseInt(XMLReader.getAttr(node, "blockw"));
                int blockH = Integer.parseInt(XMLReader.getAttr(node, "blockh"));
                int blockX = Integer.parseInt(XMLReader.getAttr(node,"blockx"));
                int blockY = Integer.parseInt(XMLReader.getAttr(node,"blocky"));
                int bulletW = Integer.parseInt(XMLReader.getAttr(node,"bulletw"));
                int bulletH = Integer.parseInt(XMLReader.getAttr(node,"bulleth"));
                int bulletDistance = Integer.parseInt(XMLReader.getAttr(node,"bulletdistance"));
                int fireTimeMs = Integer.parseInt(XMLReader.getAttr(node,"firetimems"));
                ImageIcon bulletIcon = new ImageIcon(XMLReader.getAttr(node,"bulletimg"));
                forleft = new ImageIcon(XMLReader.getAttr(node,"blockimgforleft"));
                forright = new ImageIcon(XMLReader.getAttr(node,"blockimgforright"));
                forup = new ImageIcon(XMLReader.getAttr(node,"blockimgforup"));
                fordown = new ImageIcon(XMLReader.getAttr(node,"blockimgfordown"));
                b = new BulletBlock(frameW, frameH, blockW, blockH, blockX, blockY, bulletW, bulletH, bulletDistance, fireTimeMs, bulletIcon, this, bulletBlockDirection
                ,forleft,forright,forup,fordown,fireSound);
              //  System.out.println(bulletBlockDirection);
                bulletBlockLabel = b.getBulletBlock();
                bulletBlockLabel.setFocusable(true);
                bulletBlockLabel.requestFocus();
                add(bulletBlockLabel);
            }
            addBlocks();
            }
            if(this.shotMode.equals("auto")) {
                b.shotAuto();
                this.addKeyListener(new BulletBlockKeyAdapterforChase());
                //this.addKeyListener(new BulletBlockKeyAdapterforAuto(bulletBlockDirection));
            } else if (this.shotMode.equals("manual")) {
                this.addKeyListener(new BulletBlockKeyAdapterforManual(bulletBlockDirection));
            }

            MainGameThread mainGameThread = new MainGameThread(this,bgMusic);
            mainGameThread.start();
    }
    public void addSpecialBlocks(){
        int randInt = (int)(Math.random()*100+1); //1~100
        Node blockNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BLOCK);
        NodeList blockNodeList = blockNode.getChildNodes();
        for (int i = 0; i < blockNodeList.getLength(); i++) {
            Node node = blockNodeList.item(i);
            Block block;
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            if(node.getNodeName().equals(XMLReader.E_SPECIAL)){
                if("random".equals(XMLReader.getAttr(node,"x"))||"random".equals(XMLReader.getAttr(node,"y"))){
                    x=(int)(Math.random()*frameW);
                    y=(int)(Math.random()*frameH);
                }
                else {
                    x = Integer.parseInt(XMLReader.getAttr(node, "x"));
                    y = Integer.parseInt(XMLReader.getAttr(node, "y"));
                }
                w = Integer.parseInt(XMLReader.getAttr(node, "w"));
                h = Integer.parseInt(XMLReader.getAttr(node, "h"));
                randScope = Integer.parseInt(XMLReader.getAttr(node, "randscope"));
                life = Integer.parseInt(XMLReader.getAttr(node, "life"));
                speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
                shotIcon = new ImageIcon(XMLReader.getAttr(node,"shotimg"));
                icon = new ImageIcon(XMLReader.getAttr(node, "img"));
                int rate = Integer.parseInt(XMLReader.getAttr(node,"rate"));
             //   System.out.println("rate : " + rate);
               // System.out.println("randInt : "+randInt);
                if(randInt>rate){
                    return;
                }
                if(this.blockDirection.equals("cross") || this.blockDirection.equals("down")) {
                    block = new Block(this, x, y, w, h, frameW, frameH, icon, shotIcon, life, speed, blockDirection);
                    block.setIsSpecialBlockFlag();
                }
                else{//random block
                    block = new Block(this, x, y, w, h, randScope, frameW, frameH, icon, shotIcon, life, speed, blockDirection);
                    block.setIsSpecialBlockFlag();
                }
                JLabel blockLabel = block.getBlockLabel();
                add(blockLabel);
            }
        }
    }
    public void addBlocks(){
        Node blockNode = XMLReader.getNode(gamePanelNode, XMLReader.E_BLOCK);
        NodeList blockNodeList = blockNode.getChildNodes();
        for (int i = 0; i < blockNodeList.getLength(); i++) {
            Node node = blockNodeList.item(i);
            Block block;
            if (node.getNodeType() != Node.ELEMENT_NODE)
                continue;
            if (node.getNodeName().equals(XMLReader.E_OBJ)) {
                maxBlockCount++;
                if("random".equals(XMLReader.getAttr(node,"x"))||"random".equals(XMLReader.getAttr(node,"y"))){
                    x=(int)(Math.random()*frameW);
                    y=(int)(Math.random()*frameH);
                   // System.out.println("ran X : "+x);
                   // System.out.println("ran Y : "+y);
                }else {
                    x = Integer.parseInt(XMLReader.getAttr(node, "x"));
                    y = Integer.parseInt(XMLReader.getAttr(node, "y"));
                }
                w = Integer.parseInt(XMLReader.getAttr(node, "w"));
                h = Integer.parseInt(XMLReader.getAttr(node, "h"));
                randScope = Integer.parseInt(XMLReader.getAttr(node, "randscope"));
                life = Integer.parseInt(XMLReader.getAttr(node, "life"));
                speed = Integer.parseInt(XMLReader.getAttr(node, "speed"));
                shotIcon = new ImageIcon(XMLReader.getAttr(node,"shotimg"));
                icon = new ImageIcon(XMLReader.getAttr(node, "img"));
                if(this.blockDirection.equals("cross") || this.blockDirection.equals("down")) {
                    block = new Block(this, x, y, w, h, frameW, frameH, icon, shotIcon, life, speed, blockDirection);
                    howManyBlocksNow++;
                    infiniteBlocks.add(block);
                }
                else if(this.blockDirection.equals("random")){//random block
                    block = new Block(this, x, y, w, h, randScope, frameW, frameH, icon, shotIcon, life, speed, blockDirection);
                    howManyBlocksNow++;
                    infiniteBlocks.add(block);
                }
                else {
                    //System.out.println("block chase created");
                    block = new Block(this, x, y, w, h, frameW, frameH, icon, shotIcon, life, speed, blockDirection);
                    howManyBlocksNow++;
                    infiniteBlocks.add(block);
                }
                JLabel blockLabel = block.getBlockLabel();
                add(blockLabel);
            }
        }
        //System.out.println("MAXBLOCKCOUNT : " + maxBlockCount);
    }
    public void increase(){
        score+=scoreGap;
        scoreLabel.setText(scoreContent+score);
        scoreLabel.repaint();
    }
    public void increaseStage(){
        stage++;
        stageLabel.setText(stageContent+stage);
        stageLabel.repaint();
    }
    public BulletBlock getBulletBlock() {
        return b;
    }

    public int getScore() {
        return score;
    }

    class BulletBlockKeyAdapterforChase extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    b.getBulletBlock().setLocation(b.getBulletBlock().getX()-10,b.getBulletBlock().getY());
                    lastDirection="left";
                    break;
                case KeyEvent.VK_RIGHT:
                    b.getBulletBlock().setLocation(b.getBulletBlock().getX()+10,b.getBulletBlock().getY());
                    lastDirection="right";
                    break;
                case KeyEvent.VK_UP:
                    b.getBulletBlock().setLocation(b.getBulletBlock().getX(),b.getBulletBlock().getY()-10);
                    lastDirection="up";
                    break;
                case KeyEvent.VK_DOWN:
                    b.getBulletBlock().setLocation(b.getBulletBlock().getX(),b.getBulletBlock().getY()+10);
                    lastDirection="down";
                    break;
            }
        }
    }
    class BulletBlockKeyAdapterforManual extends KeyAdapter {
        private String direction;
        public BulletBlockKeyAdapterforManual(String direction){
            this.direction=direction;
        }
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if(direction.equals("cross")) {
                switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                        //System.out.println("LEFT KEY");
                        bulletBlockLabel.setLocation(bulletBlockLabel.getX() - 10, bulletBlockLabel.getY());
                        if (bulletBlockLabel.getX() <= 0) {
                            bulletBlockLabel.setLocation(0, bulletBlockLabel.getY());
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        //System.out.println("RIGHT KEY");
                        bulletBlockLabel.setLocation(bulletBlockLabel.getX() + 10, bulletBlockLabel.getY());
                        if (bulletBlockLabel.getX() + bulletBlockLabel.getWidth() >= frameW) {
                            bulletBlockLabel.setLocation(frameW - bulletBlockLabel.getWidth(), bulletBlockLabel.getY());
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        b.shotSpace();
                        break;
                }
            }
            else if(direction.equals("updown")){
                switch (keyCode){
                    case KeyEvent.VK_UP:
                        bulletBlockLabel.setLocation(bulletBlockLabel.getX(), bulletBlockLabel.getY()-10);
                        if (bulletBlockLabel.getY() <= 0) {
                            bulletBlockLabel.setLocation(bulletBlockLabel.getX(),0);
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        bulletBlockLabel.setLocation(bulletBlockLabel.getX(),bulletBlockLabel.getY()+10);
                        if(bulletBlockLabel.getY()>frameH-bulletBlockLabel.getHeight()){
                            bulletBlockLabel.setLocation(bulletBlockLabel.getX(),frameH-bulletBlockLabel.getHeight());
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        b.shotSpace();
                        break;
                }
            }
        }
    }
    class BulletBlockKeyAdapterforAuto extends KeyAdapter {
        private String direction;
        public BulletBlockKeyAdapterforAuto(String direction){
            this.direction=direction;
        }
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (direction.equals("cross")) {
                switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                        //System.out.println("LEFT KEY");
                        bulletBlockLabel.setLocation(bulletBlockLabel.getX() - 10, bulletBlockLabel.getY());
                        if (bulletBlockLabel.getX() <= 0) {
                            bulletBlockLabel.setLocation(0, bulletBlockLabel.getY());
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        //System.out.println("RIGHT KEY");
                        bulletBlockLabel.setLocation(bulletBlockLabel.getX() + 10, bulletBlockLabel.getY());
                        if (bulletBlockLabel.getX() + bulletBlockLabel.getWidth() >= frameW) {
                            bulletBlockLabel.setLocation(frameW - bulletBlockLabel.getWidth(), bulletBlockLabel.getY());
                        }
                        break;
                }
            }
            else if(direction.equals("updown")){
                switch (keyCode){
                    case KeyEvent.VK_UP:
                        bulletBlockLabel.setLocation(bulletBlockLabel.getX(), bulletBlockLabel.getY()-10);
                        if (bulletBlockLabel.getY() <= 0) {
                            bulletBlockLabel.setLocation(bulletBlockLabel.getX(),0);
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        bulletBlockLabel.setLocation(bulletBlockLabel.getX(),bulletBlockLabel.getY()+10);
                        if(bulletBlockLabel.getY()>=frameH){
                            bulletBlockLabel.setLocation(bulletBlockLabel.getX(),frameH);
                        }
                        break;
                }
            }
        }
    }
    class MainGameThread extends Thread{
        private int randRange;
        private GamePanel gamePanel;
        private Clip clip;
        private File audioFile;
        public MainGameThread(GamePanel gamePanel, File audioFile) {
            this.gamePanel=gamePanel;
            this.audioFile= audioFile;
            try {
                clip = AudioSystem.getClip();
                //System.out.println("Music Start");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                clip.open(audioStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (LineUnavailableException e) {e.printStackTrace();}
            catch (IOException e){e.printStackTrace();}
            catch (UnsupportedAudioFileException e){e.printStackTrace();}
        }
        @Override
        public void run(){
            while(true) {
                try {
                    if (howManyBlocksNow <=0 && blockMode.equals("limit")) {
                        addBlocks();
                        increaseStage();
                        addSpecialBlocks();
                    }
                    else if(howManyBlocksNow<maxBlockCount && blockMode.equals("infinite")){
                        randRange = maxBlockCount;
                        int randInt=(int)(Math.random()*randRange);
                        Block infiniteBlock = infiniteBlocks.get(randInt);
                        int x = infiniteBlock.getX();
                        int y = infiniteBlock.getY();
                        int w = infiniteBlock.getW();
                        int h = infiniteBlock.getH();
                        //int randScope = infiniteBlock.getRandScope();
                        int frameW = infiniteBlock.getFrameW();
                        int frameH = infiniteBlock.getFrameH();
                        ImageIcon icon = infiniteBlock.getIcon();
                        ImageIcon shotIcon = infiniteBlock.getShoticon();
                        int life = infiniteBlock.getLife();
                        int speed = infiniteBlock.getSpeed();
                        String blockDirection = infiniteBlock.getBlockDirection();
                        Block newInfiniteBlock = new Block(gamePanel,x,y,w,h,frameW,frameH,icon,shotIcon,life,speed,blockDirection);
                        gamePanel.add(newInfiniteBlock.blockLabel);
                        gamePanel.repaint();
                        howManyBlocksNow++;
                    }
                    sleep(100);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        public void setRandRange(int randRange){this.randRange = randRange;}
    }
    public void paintComponent(Graphics g) {
        g.drawImage(bgImg.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
    }
    public void decreaseBlockCount(){
        howManyBlocksNow--;
    }
    public int getStagePlusSpeed() {
        return stagePlusSpeed;
    }
    public int getStage(){
        return stage;
    }
    public String getBlockMode(){return blockMode;}
    public String getLastDirection() {
        return lastDirection;
    }
}

