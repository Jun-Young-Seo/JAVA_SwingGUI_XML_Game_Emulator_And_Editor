import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class BlockGameMenuBar {
    private JMenuItem open;
    private JMenuItem close;
    private JFileChooser chooser;
    public BlockGameMenuBar(BlockGameFrame blockGameFrame){
        JMenuBar mb=  new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        mb.add(fileMenu);
        open=new JMenuItem();
        close = new JMenuItem();

        open.setText("Open");
        close.setText("Close");
        fileMenu.add(open);
        fileMenu.add(close);
        open.addActionListener(new OpenActionListener(blockGameFrame));
        close.addActionListener(new CloseActionListener());
        fileMenu.addSeparator();
        blockGameFrame.setJMenuBar(mb);
    }

    class OpenActionListener implements ActionListener{
        private File selectedFile;
        private BlockGameFrame blockGameFrame;
        public OpenActionListener(BlockGameFrame blockGameFrame){
            this.blockGameFrame=blockGameFrame;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            chooser = new JFileChooser("./resource");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("xml 파일", "xml");
            chooser.addChoosableFileFilter(filter);
            int result = chooser.showOpenDialog(blockGameFrame);
            //System.out.println(result);
            if (result == JFileChooser.APPROVE_OPTION) {
                //선택한 파일의 경로 반환
                selectedFile = chooser.getSelectedFile();
                //System.out.println(selectedFile);
            }
            blockGameFrame.loadGame(selectedFile);
        }


    }

    class CloseActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
