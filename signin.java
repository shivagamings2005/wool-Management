package project;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;
public class signin extends JPanel {
    String user="";
    Image bg;
    JScrollPane sp=new JScrollPane(this);
    JFrame f=new JFrame();
    bgimg b=new bgimg();
    BufferedImage img;
    public signin(){
        setLayout(null);
        repaint();
        JLabel bg=b.bg("E:/welcome.png" );
        bg.setBounds(-10, -100, 1300,800 );

        JLabel wel = new JLabel ("WELCOME");
        wel.setBounds (225, 570, 600, 65);
        wel.setFont(new Font("arial",Font.ITALIC,70));
        wel.setForeground(Color.WHITE);
        add (wel);

        JButton btin = new JButton ("Select user");
        btin.setBounds (790, 100, 150, 30);

        JPopupMenu min=new JPopupMenu();
        min.setSize(150, 30);
        JMenuItem inprod=new JMenuItem("producer");
        JMenuItem inproc=new JMenuItem("wool process");
        JMenuItem inqu=new JMenuItem("quality check");
        JMenuItem inst=new JMenuItem("storage");
        JMenuItem intr=new JMenuItem("transport");
        JMenuItem inbu=new JMenuItem("buyer");
        Dimension d=new Dimension(150,30);
        inprod.setPreferredSize(d);
        inproc.setPreferredSize(d);
        inqu.setPreferredSize(d);
        inst.setPreferredSize(d);
        intr.setPreferredSize(d);
        inbu.setPreferredSize(d);

        inprod.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                user="producer";
                btin.setText(user);
            }
        });
        inproc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                user="woolprocess";
                btin.setText(user);
            }
        });
        inqu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                user="qualitychecker";
                btin.setText(user);
            }
        });
        inst.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                user="storageowner";
                btin.setText(user);
            }
        });
        intr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                user="transport";
                btin.setText(user);
            }
        });
        inbu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                user="buyer";
                btin.setText(user);
            }
        });
        btin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){
                btin.setText("Select user");
                user="";
                if(e.getSource()==btin) min.show(btin, 0,25);
            }
            public void mouseClicked(MouseEvent e){
                btin.setText("Select user");
                user="";
                if(e.getSource()==btin) min.show(btin, 0,25);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){   
                min.setVisible(false);
            }  
        });
        JLabel us=new JLabel("Username",JLabel.CENTER);
        us.setBounds(700,200,120,30);
        us.setOpaque(true);
        us.setBackground(Color.WHITE);
        us.setForeground(Color.BLACK);
        
        JTextField ust=new JTextField();
        ust.setBounds(880,200,120,30);

        JLabel pass=new JLabel("Password",JLabel.CENTER);
        pass.setBounds(700,300,120,30);
        pass.setOpaque(true);
        pass.setBackground(Color.WHITE);
        pass.setForeground(Color.BLACK);

        JPasswordField passt=new JPasswordField();
        passt.setBounds(880,300,120,30);
        passt.setOpaque(true);

        JLabel cap=new JLabel("",JLabel.CENTER);
        captcha c=new captcha();
        cap.setText(c.captchaGenerate());
        cap.setBounds(700,400,120,30);
        cap.setOpaque(true);
        cap.setBackground(Color.WHITE);
        cap.setForeground(Color.BLACK);
        cap.setFont(new Font("Arial", Font.PLAIN, 22));        

        JTextField capt=new JTextField();
        capt.setBounds(880,400,120,30);

        try{img=ImageIO.read(new File("E:/reload-modified.png"));}
        catch(IOException e){e.printStackTrace();}
        Image im = img.getScaledInstance(30,30, Image.SCALE_SMOOTH);
        JButton recaptcha=new JButton(new ImageIcon(im));
        recaptcha.setBounds(1010,400,30,30);        
        recaptcha.setContentAreaFilled(false);
        recaptcha.setFocusPainted(false);
        recaptcha.setBorderPainted(false);
        recaptcha.setOpaque(false);
        recaptcha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 cap.setText(c.captchaGenerate());
            }
        });
        JButton bin=new JButton("Sign in");//bimg);
        bin.setBounds(800,500,100,30);

        JLabel sup=new JLabel("Don't have account");
        sup.setBounds(700,550,130,40);
        sup.setForeground(Color.WHITE);

        JButton supb=new JButton("Sign UP");
        supb.setBounds(810,550,90,40);
        supb.setForeground(Color.WHITE);
        supb.setContentAreaFilled(false);
        supb.setFocusPainted(false);
        supb.setBorderPainted(false);
        supb.setOpaque(false);
        supb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(user!=""){
                    removeAll();
                    if(user=="producer") new producer();
                    else if(user=="qualitychecker") new qualitycheck();
                    else if(user=="storageowner") new storage();
                    else if(user=="woolprocess") new process();
                    else if(user=="transport") new transport();
                    else if(user=="buyer") new buyer();
                    f.dispose();
                }
                else JOptionPane.showMessageDialog(null,"Select user type","NOTE",JOptionPane.INFORMATION_MESSAGE);
            }
        });
        add(sup);
        add(supb);
        
        add(us);
        add(pass);
        add(cap);
        add(ust);
        add(passt);
        add(capt);
        add(recaptcha);
        add(bin);
        min.add(inprod);
        min.add(inproc);
        min.add(inst);
        min.add(inqu);
        min.add(intr);
        min.add(inbu);
        add (btin);
        f.add(sp,BorderLayout.CENTER);
        add(bg);
        
        setPreferredSize(new Dimension(JFrame.MAXIMIZED_HORIZ, 600));
        bin.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(user!=""){
                    if (!ust.getText().isEmpty() && !new String(passt.getPassword()).isEmpty() && !capt.getText().isEmpty()) {
                        if(c.captchaCheck(capt.getText())){
                            try{
                                String pas=new String();
                                int chk=0;  
                                sqlconnection sq=new sqlconnection();
                                String qr="Select name,passwd from "+user;
                                sq.res=sq.st.executeQuery(qr);
                                while(sq.res.next()){
                                    if(sq.res.getString("name").equals(ust.getText())) {
                                        pas=sq.res.getString("passwd");
                                        chk=1;
                                    }
                                }
                                if(chk==0){
                                    JOptionPane.showMessageDialog(null,"Username doesnt exists","NOTE",JOptionPane.INFORMATION_MESSAGE);
                                    ust.setText("");
                                    passt.setText("");
                                    capt.setText("");
                                    cap.setText(c.captchaGenerate());
                                }
                                else{
                                    if(pas.equals(new String(passt.getPassword()))){
                                        if(user=="producer") new prodproc(ust.getText());
                                        else if(user=="qualitychecker") new qcproc(ust.getText());
                                        else if(user=="woolprocess") new wpproc(ust.getText());
                                        else if(user=="storageowner") new soproc(ust.getText());
                                        else if(user=="transport") new trproc(ust.getText());
                                        else if(user=="buyer") new buproc(ust.getText());
                                        sq.res.close();
                                        sq.st.close();
                                        sq.con.close();
                                        f.dispose();
                                    }
                                    else{
                                        JOptionPane.showMessageDialog(null,"Password incorrect","NOTE",JOptionPane.INFORMATION_MESSAGE);
                                        passt.setText("");
                                    }
                                }
                            }
                            catch(Exception ex){
                                JOptionPane.showMessageDialog(null,"user doesn't exists","NOTE",JOptionPane.INFORMATION_MESSAGE);
                                removeAll();
                                if(user=="producer") new producer();
                                else if(user=="qualitychecker") new qualitycheck();
                                else if(user=="storageowner") new storage();
                                else if(user=="woolprocess") new process();
                                else if(user=="transport") new transport();
                                else if(user=="buyer") new buyer();
                                f.dispose();
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(null,"Capthcha mismatch","NOTE",JOptionPane.INFORMATION_MESSAGE);
                            cap.setText(c.captchaGenerate());
                            capt.setText("");
                        }
                    }
                    else JOptionPane.showMessageDialog(null,"Field must not be empty","NOTE",JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    if(ust.getText().equals("ADMIN") && new String(passt.getPassword()).equals("METHEBOSS" )){
                        try{new admin();}
                        catch(Exception ea){ea.printStackTrace();}
                        f.dispose();
                    }
                        
                    }
            }
        });
        
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}