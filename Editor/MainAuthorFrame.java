import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    private String content;

    public MainAuthorFrame() {
        arrangeBlocks = new Vector<>(30);
        settings = new Vector<>(100);
        playerBlocks = new Vector<>(30);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        contentPane = getContentPane();
        splitPanel();
        makeMenuBar();
        setVisible(true);
    }

    private void splitPanel() {
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

    public void makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        save = new JMenuItem("Save as XML");
        close = new JMenuItem("Close");
        fileMenu.add(save);
        fileMenu.add(close);
        fileMenu.addSeparator();
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
        close.addActionListener(new MenuActionListener(this));
        save.addActionListener(new MenuActionListener(this));
    }

    public String makeString(){
        content = "<BlockGame>\n" +
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
        for (int i = 0; i < playerBlocks.size(); i++) {
            content += playerBlocks.get(i).playerToXML();
        }
        content += "</Player>\n";
        // Stage 부분
        content += "<Stage>\n";
        content += "<PlusSpeed plusSpeed=\"" + settings.get(19) + "\"/>\n";
        content += "<StageBlock stageblockx=\"500\" " +
                "stageblocky=\"100\" stageblockw=\"300\" stageblockh=\"30\" stagecontent=\"Stage : \" " +
                "stagefontname=\"고딕체\" stagefontstyle=\"1\" stagefontsize=\"30\" stagefontcolor=\"10\"/>\n";
        content += "</Stage>\n";

        // Score 부분
        content += "<Score>\n";
        content += "<Rule rule=\"" + settings.get(18) + "\"/>\n";
        content += "<ScoreBlock scoreblockx=\"" + settings.get(9) + "\" scoreblocky=\"" + settings.get(10)
                + "\" scoreblockw=\"" + settings.get(11) + "\" scoreblockh=\"" + settings.get(12)
                + "\" scorecontent=\"" + settings.get(13) + "\" scorefontname=\"" + settings.get(14)
                + "\" scorefontstyle=\"" + settings.get(16) + "\" scorefontsize=\"" + settings.get(15)
                + "\" scorefontcolor=\"" + settings.get(17) + "\"/>\n";
        content += "</Score>\n";
        content += "<Block>\n";
        //Block
        for (int i = 0; i < arrangeBlocks.size(); i++) {
            content += arrangeBlocks.get(i).blockToXML();
        }
        content += "</Block>\n" +
                "</GamePanel>\n";
        content += "</BlockGame>";
        return content;
    }
    public ArrangePanel getArrangePanel() {
        return arrangePanel;
    }

    class MenuActionListener extends Component implements ActionListener {
        private MainAuthorFrame mainAuthorFrame;
        private JFileChooser chooser;
        private String content;

        public MenuActionListener(MainAuthorFrame mainAuthorFrame) {
            this.mainAuthorFrame = mainAuthorFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == close) {
                System.exit(1000);
            }
            if (e.getSource() == save) {
                if (checkNull()) {
                    JOptionPane.showMessageDialog(mainAuthorFrame, "입력되지 않은 값이 있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
                content = makeString();
                //System.out.println(content);
                saveXmlFile(content);
            }
        }

        private boolean checkNull() {
            if (settings.size() == 0) return true;
            for (int i = 0; i < settings.size(); i++) {
                if (settings.get(i) == null || settings.get(i).equals("")) return true;
            }
            for (int i = 0; i < playerBlocks.size(); i++) {
                if (playerBlocks.get(i) == null || playerBlocks.get(i).equals("")) return true;
            }
            for (int i = 0; i < arrangeBlocks.size(); i++) {
                if (arrangeBlocks.get(i) == null || arrangeBlocks.get(i).equals("")) return true;
            }
            return false;
        }

        private void saveXmlFile(String content) {
            chooser = new JFileChooser("C:\\Users\\서준영\\Desktop\\HSU\\과제\\3-1\\project_java\\mycode\\Player\\resource");
            chooser.setDialogTitle("Save XML");
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("XML Files", "xml"));

            int userSelection = chooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = chooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".xml")) {
                    filePath += ".xml";
                }

                try (FileWriter fileWriter = new FileWriter(filePath)) {
                    fileWriter.write(content);
                    JOptionPane.showMessageDialog(this, "저장되었습니다.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "에러: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    public static void main(String[] args) {
        new MainAuthorFrame();
    }
}
