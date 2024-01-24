import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class MainAuthorFrame extends JFrame {
    private ArrangePanel arrangePanel;
    private Container contentPane;
    private ItemPanel itemPanel;
    Vector<Block> arrangeBlocks;
    Vector<String> settings;
    JMenuItem close;
    JMenuItem save;
    public MainAuthorFrame(){
        arrangeBlocks = new Vector<>(30);
        settings = new Vector<>(100);
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
        close.addActionListener(new MenuActionListener());
        save.addActionListener(new MenuActionListener());
    }
    class MenuActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==close){
                System.exit(1000);
            }
            if(e.getSource()==save){//XML파일로 변환하는 코드 작성
                String str="";
                for(int i=0; i<arrangeBlocks.size();i++){
                    str += arrangeBlocks.get(i).toXML();
                }
                System.out.println(str);
            }
        }
    }
    public static void main (String [] args){
        new MainAuthorFrame();
    }
}
