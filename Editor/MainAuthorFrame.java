import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import static javax.swing.JOptionPane.showMessageDialog;

public class MainAuthorFrame extends JFrame {
    private ImageIcon handler;
    private JLabel handlerLabel = new JLabel();
    private ArrangeTabPanel arrangeTabPanel;
    private ArrangePanel arrangePanel;
    private Container contentPane;
    private ItemPanel itemPanel;
    Vector<Block> playerBlocks;
    Vector<Block> arrangeBlocks;
    Vector<String> settings;
    JMenuItem close;
    JMenuItem newFile;
    JMenuItem saveAs;
    JMenuItem open;
    JMenuItem save;
    private File xmlFile;
    private String content;
    MainAuthorFrame mainAuthorFrame=this;
    private boolean isFirstOpen = true;

    public MainAuthorFrame() {
        arrangeBlocks = new Vector<>(30);
        settings = new Vector<>(100);
        playerBlocks = new Vector<>(30);
        handler = makeImage(new ImageIcon("./images/bomb.png"),30,30);
        handlerLabel.setIcon(handler);
        handlerLabel.setSize(30,30);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1920, 1080);
        contentPane = getContentPane();
        splitPanel();
        makeMenuBar();
        setVisible(true);
    }
    public JLabel getHandlerLabel(){return handlerLabel;}
    public ImageIcon makeImage(ImageIcon original,int w,int h){
        Image newImg = original.getImage();
        Image scaledNewImg = newImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        ImageIcon result = new ImageIcon(scaledNewImg);
        return result;
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
        arrangeTabPanel = arrangePanel.getArrangeTabPanel();

        this.revalidate();
        this.repaint();
    }

    public void makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        saveAs = new JMenuItem("Save As");
        close = new JMenuItem("Close");
        open = new JMenuItem("Open");
        newFile = new JMenuItem("New");
        save = new JMenuItem("save");

        fileMenu.add(newFile);
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(saveAs);
        fileMenu.add(close);

        fileMenu.addSeparator();
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
        save.addActionListener(new MenuActionListener(this));
        newFile.addActionListener(new MenuActionListener(this));
        open.addActionListener(new MenuActionListener(this));
        close.addActionListener(new MenuActionListener(this));
        saveAs.addActionListener(new MenuActionListener(this));
    }
    public String makeString() {
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

    class MenuActionListener implements ActionListener {
        private MainAuthorFrame mainAuthorFrame;
        private JFileChooser chooser;
        private String content;
        private String currentFilePath = null;


        public MenuActionListener(MainAuthorFrame mainAuthorFrame) {
            this.mainAuthorFrame = mainAuthorFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == close) {
                System.exit(1000);
            }
            if(e.getSource()==newFile){
                isFirstOpen=false;
            }
            if(e.getSource()==save){
                if(checkNull()){
                    showMessageDialog(mainAuthorFrame, "입력되지 않은 값이 있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                content = makeString();
                saveXmlFile(content);
            }
            if (e.getSource() == saveAs) {
                if (checkNull()) {
                    showMessageDialog(mainAuthorFrame, "입력되지 않은 값이 있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                content = makeString();
                saveXmlFile(content);
            }
            if(e.getSource() == open){

                arrangeTabPanel.removeAll();
                arrangeTabPanel.repaint();
                playerBlocks.clear();
                arrangeBlocks.clear();
                settings.clear();
                openXmlFile();
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

            if(currentFilePath!=null){ //기존에 저장된게 있던 경우 --> save
                try (FileWriter fileWriter = new FileWriter(currentFilePath)) {
                    fileWriter.write(content);
                    showMessageDialog(mainAuthorFrame, "저장되었습니다.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    showMessageDialog(mainAuthorFrame, "에러: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            if(currentFilePath==null) { //기존에 저장된 게 없는 경우 -->save as
                int userSelection = chooser.showSaveDialog(mainAuthorFrame);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = chooser.getSelectedFile();
                    String filePath = fileToSave.getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(".xml")) {
                        filePath += ".xml";
                    }

                    try (FileWriter fileWriter = new FileWriter(filePath)) {
                        fileWriter.write(content);
                        showMessageDialog(mainAuthorFrame, "저장되었습니다.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        showMessageDialog(mainAuthorFrame, "에러: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

        private void openXmlFile() {
            chooser = new JFileChooser("C:\\Users\\서준영\\Desktop\\HSU\\과제\\3-1\\project_java\\mycode\\Player\\resource");
            chooser.setDialogTitle("Open XML");
            chooser.addChoosableFileFilter(new FileNameExtensionFilter("XML Files", "xml"));
            int userSelection = chooser.showOpenDialog(mainAuthorFrame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                xmlFile = chooser.getSelectedFile();
                currentFilePath = xmlFile.getAbsolutePath();
                arrangeTabPanel.setBlocksByXml(xmlFile);
            }
        }
    }
    public static void main(String[] args) {
        new MainAuthorFrame();
    }
}
