package project;
import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.event.*;
import java.awt.*;
public class producer extends JPanel{
    String user="";
    Image bg;
    JScrollPane sp=new JScrollPane(this);
    JFrame f=new JFrame();
    bgimg b=new bgimg();
    public producer() {
        setLayout(null);
        revalidate();
        repaint();
        JLabel bg=b.bg("C:/Users/Admin/Downloads/producer.jpg");
        bg.setBounds(-10, -100, 1300,800 );

        JLabel sup=new JLabel("Already have account");
        sup.setBounds(450,1000,130,40);
        sup.setForeground(Color.WHITE);

        JButton siginb=new JButton("Sign IN");
        siginb.setBounds(570,1000,90,40);
        siginb.setForeground(Color.WHITE);
        siginb.setContentAreaFilled(false);
        siginb.setFocusPainted(false);
        siginb.setBorderPainted(false);
        siginb.setOpaque(false);
        siginb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                new signin();
                f.dispose();
            }
        });

        JLabel us=new JLabel("Username",JLabel.CENTER);
        us.setBounds(400,100,120,30);
        us.setOpaque(true);
        us.setBackground(Color.WHITE);
        us.setForeground(Color.decode("#19e668"));
        
        JTextField ust=new JTextField();
        ust.setBounds(580,100,120,30);
        ust.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                ust.setBorder(new LineBorder(Color.WHITE,2,true));
            }
        });

        JLabel pass=new JLabel("Password",JLabel.CENTER);
        pass.setBounds(400,200,120,30);
        pass.setOpaque(true);
        pass.setBackground(Color.WHITE);
        pass.setForeground(Color.decode("#19e668"));

        JPasswordField passt=new JPasswordField();
        passt.setBounds(580,200,120,30);
        passt.setOpaque(true);
        passt.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                passt.setBorder(new LineBorder(Color.WHITE,2,true));
            }
        });

        JLabel repas=new JLabel("retype Password",JLabel.CENTER);
        repas.setBounds(400,300,120,30);
        repas.setOpaque(true);
        repas.setBackground(Color.WHITE);
        repas.setForeground(Color.decode("#19e668"));

        JPasswordField repasst=new JPasswordField();
        repasst.setBounds(580,300,120,30);
        repasst.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                repasst.setBorder(new LineBorder(Color.WHITE,2,true));
            }
        });

        JLabel dob=new JLabel("DOB",JLabel.CENTER);
        dob.setBounds(400,400,120,30);
        dob.setOpaque(true);
        dob.setBackground(Color.WHITE);
        dob.setForeground(Color.decode("#19e668"));

        String dates[]= { "01", "02", "03", "04", "05","06", "07", "08", "09", "10","11", "12", "13", "14", "15","16", "17", "18", "19", "20","21", "22", "23", "24", "25","26", "27", "28", "29", "30","31" };
        String months[]= { "01", "02", "03", "04", "05","06", "07", "08", "09", "10","11", "12" };
        String years[]= { "1995", "1996", "1997", "1998","1999", "2000", "2001", "2002","2003", "2004", "2005", "2006","2007", "2008", "2009", "2010","2011", "2012", "2013", "2014","2015", "2016", "2017", "2018","2019" };

        JComboBox<String> dat=new JComboBox<String>(dates);
        dat.setBounds(550,405,50,20);
        dat.setBackground(Color.WHITE);
        dat.setFont(new Font("Arial", Font.PLAIN, 15));

        JComboBox<String> mon=new JComboBox<String>(months);
        mon.setBounds(610,405,50,20);
        mon.setBackground(Color.WHITE);

        JComboBox<String> yr=new JComboBox<String>(years);
        yr.setBounds(670,405,60,20);
        yr.setBackground(Color.WHITE);

        JLabel addr=new JLabel("Address",JLabel.CENTER);
        addr.setBounds(400,550,120,30);
        addr.setOpaque(true);
        addr.setBackground(Color.WHITE);
        addr.setForeground(Color.decode("#19e668"));

        JTextArea addrt=new JTextArea();
        addrt.setBounds(580,520,180,130);
        addrt.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                addrt.setBorder(new LineBorder(Color.WHITE,2,true));
            }
        });

        JLabel ph=new JLabel("phone",JLabel.CENTER);
        ph.setBounds(400,750,120,30);
        ph.setOpaque(true);
        ph.setBackground(Color.WHITE);
        ph.setForeground(Color.decode("#19e668"));

        JTextField pht=new JTextField();
        pht.setBounds(580,750,120,30);
        pht.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                pht.setBorder(new LineBorder(Color.WHITE,2,true));
            }
        });

        JLabel aad=new JLabel("Aadhar no",JLabel.CENTER);
        aad.setBounds(400,850,120,30);
        aad.setOpaque(true);
        aad.setBackground(Color.WHITE);
        aad.setForeground(Color.decode("#19e668"));

        JTextField aadt=new JTextField();
        aadt.setBounds(580,850,120,30);
        aadt.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                aadt.setBorder(new LineBorder(Color.WHITE,2,true));
            }
        });

        JButton bin=new JButton("Sign up");
        bin.setBounds(490,950,90,30);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JScrollBar sb=sp.getVerticalScrollBar();

        add(bg);
        add(us);
        add(pass);
        add(repas);
        add(dob);
        add(addr);
        add(ph);
        add(aad);
       
        add(ust);
        add(passt);
        add(repasst);
        add(dat);
        add(mon);
        add(yr);
        add(addrt);
        add(pht);
        add(aadt);
        add(bin);
        add(sup);
        add(siginb);
        f.add(sp,BorderLayout.CENTER);
        setPreferredSize(new Dimension(JFrame.MAXIMIZED_HORIZ, 1100));
        bin.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (!ust.getText().isEmpty() && !new String(passt.getPassword()).isEmpty() && !new String(repasst.getPassword()).isEmpty() && !addrt.getText().isEmpty() && !pht.getText().isEmpty() && !aadt.getText().isEmpty()) {
                     try{
                        sqlconnection sq=new sqlconnection();
                        sq.st.executeUpdate("create table if not exists producer(id int primary key auto_increment,name char(20),passwd char(20),dob date,address char(40),phone char(13),aadhar char(12))");
                        sq.res=sq.st.executeQuery("select count(name) from producer where name='"+ust.getText()+"'");
                        sq.res.next();
                        if(sq.res.getInt("count(name)")==0){
                            if(new String(passt.getPassword()).equals(new String(repasst.getPassword()))){
                                if(pht.getText().length()==10 || pht.getText().length()==12 || pht.getText().length()==13){
                                    if(aadt.getText().length()==12 ){
                                        String sqldate=yr.getSelectedItem()+"-"+mon.getSelectedItem()+"-"+dat.getSelectedItem();
                                        sq.st.executeUpdate("insert into producer(name,passwd,dob,address,phone,aadhar) values('"+ust.getText()+"','"+new String(passt.getPassword())
                                        +"','"+sqldate+"','"+addrt.getText()+"','"+pht.getText()+"','"+aadt.getText()+"')");
                                        new prodproc(ust.getText());
                                        f.dispose();
                                    }
                                    else{
                                        JOptionPane.showMessageDialog(null,"Invalid aadhar number","NOTE",JOptionPane.INFORMATION_MESSAGE);    
                                        aadt.setText("");
                                    }
                                }
                                else{
                                    JOptionPane.showMessageDialog(null,"Invalid phone number","NOTE",JOptionPane.INFORMATION_MESSAGE);    
                                    pht.setText("");
                                }
                            }
                            else{
                                JOptionPane.showMessageDialog(null,"Password doesn't match","NOTE",JOptionPane.INFORMATION_MESSAGE);
                                passt.setText("");
                                repasst.setText("");
                            }
                        }
                        else JOptionPane.showMessageDialog(null,"user already exists","NOTE",JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch(Exception ex){ex.printStackTrace();}
                }
                else{
                    Component[] c = getComponents();
                    for (Component co : c) {
                        if (co instanceof JTextField ) {
                            JTextField t = (JTextField) co;
                            if(t.getText().equals(""))
                            t.setBorder(new LineBorder(Color.RED,2,true));
                        }
                        else if( co instanceof JTextArea){
                            JTextArea t = (JTextArea) co;
                            if(t.getText().equals(""))
                            t.setBorder(new LineBorder(Color.RED,2,true));
                        }
                    }
                    JOptionPane.showMessageDialog(null,"Field must not be empty","NOTE",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        sb.setUnitIncrement(20);
        sb.setBlockIncrement(20);
        sb.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int value = e.getValue();
                bg.setLocation(-10,+value-100);
                setLocation(getX(), -value);
                add(bg,Integer.valueOf(0));
            }
        });
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
} 