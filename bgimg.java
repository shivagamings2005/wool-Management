package project;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class bgimg {
    BufferedImage img;
    Image im;
    JLabel bg;
    public JLabel bg(String x){
        try{img=ImageIO.read(new File(x));}
        catch(IOException e){e.printStackTrace();}
        im = img.getScaledInstance((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(), Image.SCALE_SMOOTH);
        bg=new JLabel(new ImageIcon(im));
        return bg;
    }
}
