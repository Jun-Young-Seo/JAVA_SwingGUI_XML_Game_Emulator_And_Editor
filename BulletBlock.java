import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Vector;

class BulletBlock extends JLabel {
    ImageIcon scaledImgforLeft,scaledImgforRight,scaledImgforUp,scaledImgforDown;
    private BulletThreadForManual bulletThreadForManual;
    private boolean spaceFirstFlag=true;
    private JLabel bulletLabel;
    private int blockW,blockH,blockX,blockY;
    private JLabel bulletBlock;
    private ImageIcon bulletImg;
    private int frameW, frameH;
    private String bulletBlockDirection;
    private int bulletW, bulletH,bulletDistance, fireTimeMs;
    private GamePanel gamePanel;
    private Vector<JLabel> bulletVector = new Vector<>(10);
    private long lastFireTime = 0; // 마지막 발사 시간을 기록
    private final long fireDelay = 500; // 최소 발사 간격

    public JLabel getBulletBlock() {
        return bulletBlock;
    }
    public BulletBlock(int frameW, int frameH, int blockW, int blockH,int blockX, int blockY, int bulletW, int bulletDistance,int bulletH, int fireTimeMs,ImageIcon bulletBlockIcon, ImageIcon bulletIcon, GamePanel gamePanel,String bulletBlockDirection) {
        this.frameW = frameW; this.blockW=blockW; this.blockH=blockH; this.bulletW=bulletW; this.bulletH=bulletH;
        this.fireTimeMs = fireTimeMs; this.bulletDistance= bulletDistance; this.bulletBlockDirection=bulletBlockDirection;
        this.frameH = frameH; this.blockX=blockX; this.blockY=blockY;
        this.gamePanel = gamePanel;
        Image scaledImage = bulletBlockIcon.getImage().getScaledInstance(blockW, blockH, Image.SCALE_SMOOTH);
        ImageIcon scaledImg = new ImageIcon(scaledImage);
        Image bulletScaledImage = bulletIcon.getImage().getScaledInstance(bulletW,bulletH,Image.SCALE_SMOOTH);
        bulletImg = new ImageIcon(bulletScaledImage);
        bulletBlock = new JLabel();
        bulletBlock.setIcon(scaledImg);
        bulletBlock.setSize(blockW, blockH);
        bulletBlock.setLocation(blockX,blockY);
    }

    public BulletBlock(int frameW, int frameH, int blockW, int blockH,int blockX, int blockY, int bulletW,
                       int bulletDistance,int bulletH, int fireTimeMs,ImageIcon bulletIcon, GamePanel gamePanel,String bulletBlockDirection
    ,ImageIcon forLeft, ImageIcon forRight, ImageIcon forUp, ImageIcon forDown) {
        this.frameW = frameW; this.blockW=blockW; this.blockH=blockH; this.bulletW=bulletW; this.bulletH=bulletH;
        this.fireTimeMs = fireTimeMs; this.bulletDistance= bulletDistance; this.bulletBlockDirection=bulletBlockDirection;
        this.frameH = frameH; this.blockX=blockX; this.blockY=blockY;
        this.gamePanel = gamePanel;
        Image scaledImageforLeft =forLeft.getImage().getScaledInstance(blockW, blockH, Image.SCALE_SMOOTH);
        scaledImgforLeft = new ImageIcon(scaledImageforLeft);
        Image scaledImageforRight =forRight.getImage().getScaledInstance(blockW, blockH, Image.SCALE_SMOOTH);
        scaledImgforRight = new ImageIcon(scaledImageforRight);
        Image scaledImageforUp =forUp.getImage().getScaledInstance(blockW, blockH, Image.SCALE_SMOOTH);
        scaledImgforUp = new ImageIcon(scaledImageforUp);
        Image scaledImageforDown =forDown.getImage().getScaledInstance(blockW, blockH, Image.SCALE_SMOOTH);
        scaledImgforDown = new ImageIcon(scaledImageforDown);
        Image bulletScaledImage = bulletIcon.getImage().getScaledInstance(bulletW,bulletH,Image.SCALE_SMOOTH);
        bulletImg = new ImageIcon(bulletScaledImage);
        bulletBlock = new JLabel();
        bulletBlock.setIcon(scaledImgforLeft);
        bulletBlock.setSize(blockW, blockH);
        bulletBlock.setLocation(blockX,blockY);
        System.out.println("in bullt block direction:"+bulletBlockDirection);
    }
    public void shotAuto(){
        BulletBlockThreadForAuto bulletBlockThreadForAuto = new BulletBlockThreadForAuto();
        bulletBlockThreadForAuto.start();
    }
    public void shotSpace(){
        if(spaceFirstFlag) {
            bulletThreadForManual = new BulletThreadForManual();
            bulletThreadForManual.start();
            bulletThreadForManual.addBullet();
            spaceFirstFlag=false;
        }
        else{
            bulletThreadForManual.addBullet();
        }
    }
    //스페이스 키로 발사되는 총알 스레드
    class BulletThreadForManual extends Thread {
        private Vector<JLabel> bullets = new Vector<>(10);
        public void addBullet() {
            JLabel bullet = new JLabel();
            bullet.setName("Bullet");
            bullet.setLocation(bulletBlock.getX(), bulletBlock.getY());
            bullet.setSize(bulletW, bulletH);
            bullet.setIcon(bulletImg);
            gamePanel.add(bullet);
            gamePanel.repaint();
            bullets.add(bullet);
        }

        @Override
        public void run() {
                if (bulletBlockDirection.equals("cross")) {
                    while (true) {
                        try {
                            // 총알 이동 로직
                            for (int i = 0; i < bullets.size(); i++) {
                                bullets.get(i).setLocation(bullets.get(i).getX(), bullets.get(i).getY() - bulletDistance);
                                if (bullets.get(i).getY() < 100) {
                                    gamePanel.remove(bullets.get(i));
                                    gamePanel.repaint();
                                }
                                gamePanel.repaint();
                            }sleep(fireTimeMs);
                        } catch (InterruptedException e) {
                        return;
                        }

                    }

                } else if (bulletBlockDirection.equals("updown")) {
                    while (true) {
                        try {
                            // 총알 이동 로직
                            for (int i = 0; i < bullets.size(); i++) {
                                bullets.get(i).setLocation(bullets.get(i).getX() + bulletDistance, bullets.get(i).getY());
                                if (bullets.get(i).getX() > frameW) {
                                    gamePanel.remove(bullets.get(i));
                                    gamePanel.repaint();
                                }
                                gamePanel.repaint();
                            }
                        sleep(fireTimeMs);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }

            }

    }



    //자동 발사 총알 스레드
    class BulletBlockThreadForAuto extends Thread {
        class BulletForChaseMode{
            private String direction;
            private JLabel bullet;
            private int dx,dy;
            public BulletForChaseMode(String direction, JLabel bullet){
                this.direction=direction; this.bullet=bullet;
                directionParseInt(direction);
                BulletForChaseModeThread bulletForChaseModeThread = new BulletForChaseModeThread();
                bulletForChaseModeThread.start();
            }
            public String getDirection() {
                return direction;
            }
            public JLabel getBullet() {
                return bullet;
            }
            public void directionParseInt(String direction){
                switch (direction){
                    case "right":
                        dx=1; dy=0;
                        break;
                    case "left":
                        dx=-1; dy=0;
                        break;
                    case "up":
                        dx=0; dy=-1;
                        break;
                    case"down":
                        dx=0;dy=1;
                        break;
                }
            }
            class BulletForChaseModeThread extends Thread{
                @Override
                public void run(){
                    try{
                        while(true){
                            bullet.setLocation(bullet.getX()+dx*bulletDistance,bullet.getY()+dy*bulletDistance);
                            if(bullet.getX()<0 || bullet.getX()>frameW ||bullet.getY()<0 || bullet.getY()>frameH){
                                gamePanel.remove(bullet);
                                gamePanel.repaint();
                                this.interrupt();
                            }
                            sleep(10);
                        }
                    } catch (InterruptedException e) {
                        //System.out.println("interrupted");
                        return;
                    }
                }
            }
        }

        @Override
        public void run() {
            if(bulletBlockDirection.equals("cross")) {
                while (true) {
                    JLabel bulletLabel = new JLabel();
                    bulletLabel.setName("Bullet");
                    bulletLabel.setLocation(bulletBlock.getX(), bulletBlock.getY());
                    bulletLabel.setSize(bulletW, bulletH);
                    bulletLabel.setIcon(bulletImg);
                    gamePanel.add(bulletLabel);
                    bulletVector.add(bulletLabel);
                    try {
                        for (int i = 0; i < bulletVector.size(); i++) {
                            bulletVector.get(i).setLocation(bulletVector.get(i).getX(), bulletVector.get(i).getY() - bulletDistance);
                            if (bulletVector.get(i).getY() <= 100) {
                                gamePanel.remove(bulletVector.get(i));
                                gamePanel.repaint();
                                bulletVector.remove(i);
                                i--;
                                //System.out.println("bullet removed");
                            }
                        }
                        sleep(fireTimeMs);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
            else if(bulletBlockDirection.equals("updown")){
                while (true) {
                    JLabel bulletLabel = new JLabel();
                    bulletLabel.setName("Bullet");
                    bulletLabel.setLocation(bulletBlock.getX(), bulletBlock.getY());
                    bulletLabel.setSize(bulletW, bulletH);
                    bulletLabel.setIcon(bulletImg);
                    gamePanel.add(bulletLabel);
                    bulletVector.add(bulletLabel);
                    try {
                        for (int i = 0; i < bulletVector.size(); i++) {
                            bulletVector.get(i).setLocation(bulletVector.get(i).getX()+bulletDistance, bulletVector.get(i).getY());
                            if (bulletVector.get(i).getX() >= frameW-100) {
                                gamePanel.remove(bulletVector.get(i));
                                gamePanel.repaint();
                                bulletVector.remove(i);
                                i--;
                                //System.out.println("bullet removed");
                            }
                        }
                        sleep(fireTimeMs);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
            //for chase block mode
            else if(bulletBlockDirection.equals("fourway")){
                try{
                while(true) {
                    String direction = gamePanel.getLastDirection();
                    switch(direction){
                        case "left":
                            bulletBlock.setIcon(scaledImgforLeft);
                            break;
                        case "right":
                            bulletBlock.setIcon(scaledImgforRight);
                            break;
                        case "up":
                            bulletBlock.setIcon(scaledImgforUp);
                            break;
                        case "down":
                            bulletBlock.setIcon(scaledImgforDown);
                            break;
                    }
                    JLabel bulletLabel = new JLabel();
                    bulletLabel.setName("Bullet");
                    bulletLabel.setLocation(bulletBlock.getX(), bulletBlock.getY());
                    bulletLabel.setSize(bulletW, bulletH);
                    bulletLabel.setIcon(bulletImg);
                    gamePanel.add(bulletLabel);
                    bulletVector.add(bulletLabel);
                    BulletForChaseMode bulletForChaseMode = new BulletForChaseMode(direction, bulletLabel);
                    sleep(fireTimeMs);
                }
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }


    public Vector<JLabel> getBulletVector() {
        return bulletVector;
    }




}
