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
    private ArrangePanel arrangePanel;
    private ArrangeTabPanel arrangeTabPanel;
    public ItemPanel(ArrangePanel arrangePanel) {
        setLayout(null);
        this.arrangePanel = arrangePanel;
        this.arrangeTabPanel = arrangePanel.getArrangeTabPanel();
        firstSetting();
        addButtons();
        drawBlocks();
    }

    public Vector<Block> getItems() {
        return items;
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
                Block b = new Block(x, y);
                items.add(b);
            }
        }
    }

    public void drawBlocks() {
        removeAll();
        addButtons();
        for (int i = 0; i < items.size(); i++) {
            Block b = items.get(i);
            JLabel blockLabel = b.getSampleBlockLabel();
            blockLabel.setLocation(b.getBlockX(), b.getBlockY());
            add(blockLabel);
        }
        repaint();
    }

    class Block {
        //눌러져 있는 아이템인지 확인
        boolean isChoosed = false;
        //아이템 패널에서 좌표
        int blockX;
        int blockY;
        private JLabel sampleBlockLabel;
        private ImageIcon newUserBlockImg;
        //arrangePanel용 변수
        int x,y,w,h,randscope,life,speed;
        private ImageIcon blockImg;
        private ImageIcon shotImg;

        //ItemPanel 표기용 블록 생성자
        public Block(int blockX, int blockY) {
            this.blockX = blockX;
            this.blockY = blockY;
            sampleBlockLabel = new JLabel();
            sampleBlockLabel.setIcon(mouseNormalBlock);
            sampleBlockLabel.setSize(80, 80);
            sampleBlockLabel.addMouseListener(new BlockActionListener());
        }
        //ItemPanel에서 사용자가 이미지 바꾼 블록 생성자
        public Block(int blockX, int blockY, ImageIcon newOriginalBlockImg) {
            this.blockX = blockX;
            this.blockY = blockY;
            sampleBlockLabel = new JLabel();
            Image newBlockImg = newOriginalBlockImg.getImage();
            Image scaledNewBlockImg = newBlockImg.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            newUserBlockImg = new ImageIcon(scaledNewBlockImg);
            sampleBlockLabel.setIcon(newUserBlockImg);
            sampleBlockLabel.setSize(80, 80);
            sampleBlockLabel.addMouseListener(new BlockActionListener());
        }

        //블록 멤버로 attribute 설정
        public void setAttribute(){
            String inputAttr = getInput();
            StringTokenizer stk = new StringTokenizer(inputAttr,",");
            while(stk.hasMoreTokens()){
                x=Integer.parseInt(stk.nextToken());
                y=Integer.parseInt(stk.nextToken());
                w=Integer.parseInt(stk.nextToken());
                h=Integer.parseInt(stk.nextToken());
                randscope=Integer.parseInt(stk.nextToken());
                life=Integer.parseInt(stk.nextToken());
                speed=Integer.parseInt(stk.nextToken());
            }
            Block forArrangePanelBlock = new Block(x,y);
            items.add(forArrangePanelBlock);
        }

        public int getBlockX() {
            return blockX;
        }

        public int getBlockY() {
            return blockY;
        }

        public JLabel getSampleBlockLabel() {
            return sampleBlockLabel;
        }
        public Block getBlock(){return this;}
    }

    public Block findBlock(JLabel block) {
        for (Block b : items) {
            if (b.getSampleBlockLabel() == block) {
                return b;
            }
        }
        return null;
    }

    class BlockActionListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                JLabel block = (JLabel) e.getSource();
                Block b = findBlock(block);
                BlockDialog blockDialog = new BlockDialog();
                blockDialog.setVisible(true);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            JLabel block = (JLabel) e.getSource();
            Block b = findBlock(block);
            for (int i = 0; i < items.size(); i++) {
                items.get(i).isChoosed = false;
                items.get(i).getSampleBlockLabel().setIcon(mouseNormalBlock);
            }
            if (b != null) {
                b.isChoosed = true;
                block.setIcon(mousePressedBlock);
                repaint();
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
            private JButton ok = new JButton("확인");
            private JLabel[] attrs;


            public BlockDialog() {
                setLayout(null);
                setResizable(false);
                attrs = new JLabel[]{x, y, w, h, randscope, life, speed, img, shotimg};
                inputFields = new JTextField[9];
                System.out.println(attrs.length);
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
                setSize(500, 500);
                ok.setSize(60, 30);
                ok.setLocation(200, 400);
                add(ok);
                ok.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);

                    }
                });
            }

        }
    }

    class ButtonActionListener extends MouseAdapter {
        private JFileChooser chooser;
        @Override
        public void mousePressed(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("블록 추가")) {
                chooser = new JFileChooser("./images");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG & GIF Images"
                        , "jpg", "png", "gif");
                chooser.setFileFilter(filter);
                int result = chooser.showOpenDialog(null);
                if (result != JFileChooser.APPROVE_OPTION) return;
                String filePath = chooser.getSelectedFile().getPath();
                ImageIcon newOriginalBlockImg = new ImageIcon(filePath);
                Block newBlock = new Block(removedX, removedY, newOriginalBlockImg);
                newBlock.getSampleBlockLabel().setLocation(removedX, removedY);
                add(newBlock.getSampleBlockLabel());
                repaint();
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
        addButton = new JButton("블록 추가");
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
