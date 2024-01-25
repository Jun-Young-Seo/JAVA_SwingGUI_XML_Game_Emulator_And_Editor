import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MainAuthorFrame extends JFrame {
    private ArrangePanel arrangePanel;
    private Container contentPane;
    private ItemPanel itemPanel;
    Vector<Block> playerBlocks;
    Vector<Block> arrangeBlocks;
    Vector<String> settings;
    JMenuItem close;
    JMenuItem save;
    public MainAuthorFrame(){
        arrangeBlocks = new Vector<>(30);
        settings = new Vector<>(100);
        playerBlocks = new Vector<>(30);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920,1080);
        contentPane = getContentPane();
        splitPanel();
        makeMenuBar();
        setVisible(true);
    }
    private void splitPanel(){
        setLayout(new BorderLayout());
        JSplitPane hSplitPane = new JSplitPane();
        hSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        hSplitPane.setDividerLocation(1500); // Divider 위치 설정
        contentPane.add(hSplitPane);
        arrangePanel = new ArrangePanel(this);
        itemPanel = new ItemPanel(this);
        arrangePanel.setItemPanel(itemPanel);
        itemPanel.setArrangePanel(arrangePanel);

        arrangePanel.init();
        itemPanel.init();
        hSplitPane.setLeftComponent(arrangePanel);
        hSplitPane.setRightComponent(itemPanel);
    }
    public void makeMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        save = new JMenuItem("Save as XML");
        close = new JMenuItem("Close");
        fileMenu.add(save);fileMenu.add(close);
        fileMenu.addSeparator();
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
        close.addActionListener(new MenuActionListener(this));
        save.addActionListener(new MenuActionListener(this));
    }

    public ArrangePanel getArrangePanel() {
        return arrangePanel;
    }

    class MenuActionListener extends Component implements ActionListener{
        private MainAuthorFrame mainAuthorFrame;
        public MenuActionListener(MainAuthorFrame mainAuthorFrame) {
            this.mainAuthorFrame=mainAuthorFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==close){
                System.exit(1000);
            }
            if(e.getSource()==save){//XML파일로 변환하는 코드 작성
                if(checkNull()){
                    JOptionPane.showMessageDialog(mainAuthorFrame,"입력되지 않은 값이 있습니다.","오류",JOptionPane.ERROR_MESSAGE);
                }
                String str="<BlockGame>\n" +
                        "<Setting>\n" +
                        "<Screen>\n" +
                        "<Size w=\"" + settings.get(0) + "\" h=\"" + settings.get(1) + "\"/>\n" +
                        "</Screen>\n" +
                        "<Bg>\n" +
                        "<BgImg bgImg=\"" + settings.get(6) + "\"/>\n" +
                        "<BgMusic bgMusic=\"" + settings.get(7) + "\"/>\n" +
                        "<FireSound fireSound=\"" + settings.get(8) + "\"/>\n" +
                        "</Bg>\n" +
                        "<GameMode>\n" +
                        "<BlockMode blockMode=\"" + settings.get(2) + "\"/>\n" +
                        "<ShotMode shotMode=\"" + settings.get(3) + "\"/>\n" +
                        "<BlockDirection blockDirection=\"" + settings.get(4) + "\"/>\n" +
                        "<BulletBlockDirection bulletBlockDirection=\"" + settings.get(5) + "\"/>\n" +
                        "</GameMode>\n" +
                        "</Setting>\n" +
                        "<GamePanel>\n" +
                        "<Player>\n";
                //Player(bulletBlock)
                for(int i=0; i<playerBlocks.size();i++){
                    str+=playerBlocks.get(i).playerToXML();
                }
                str+="</Player>\n";
                // Stage 부분
                str += "<Stage>\n";
                str += "<PlusSpeed plusSpeed=\"" + settings.get(18) + "\"/>\n";
                str += "<StageBlock stageblockx=\"500\" stageblocky=\"100\" stageblockw=\"300\" stageblockh=\"30\" stagecontent=\"Stage : \" stagefontname=\"고딕체\" stagefontstyle=\"1\" stagefontsize=\"30\" stagefontcolor=\"10\"/>\n";
                str += "</Stage>\n";

                // Score 부분
                str += "<Score>\n";
                str += "<Rule rule=\"" + settings.get(17) + "\"/>\n";
                str += "<ScoreBlock scoreblockx=\"" + settings.get(9) + "\" scoreblocky=\"" + settings.get(10) + "\" scoreblockw=\"" + settings.get(11) + "\" scoreblockh=\"" + settings.get(12) + "\" scorecontent=\"" + settings.get(13) + "\" scorefontname=\"" + settings.get(14) + "\" scorefontstyle=\"" + settings.get(15) + "\" scorefontsize=\"" + settings.get(16) + "\" scorefontcolor=\"" + settings.get(16) + "\"/>\n";
                str += "</Score>\n";
                str += "<Block>\n";
                //Block
                for(int i=0; i<arrangeBlocks.size();i++){
                    str += arrangeBlocks.get(i).blockToXML();
                }
                str+="</Block>\n" +
                        "</GamePanel>\n";
                str+="</BlockGame>";
                System.out.println(str);
            }
        }
        private boolean checkNull(){
            if(settings.size()==0)return true;
            for(int i=0;i<settings.size();i++){
                if(settings.get(i)==null) return true;
            }
            for(int i=0;i<playerBlocks.size();i++){
                if(playerBlocks.get(i)==null)return true;
            }
            for(int i=0;i<arrangeBlocks.size();i++){
                if(arrangeBlocks.get(i)==null)return true;
            }
            return false;
        }
    }

    public static void main (String [] args){
        new MainAuthorFrame();
    }
}
