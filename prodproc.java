package project;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.awt.*;

public class prodproc {
    int val=10;
    Image bg;
    String user="qualitychecker";
    JPanel inbp=new JPanel();
    JTable table=null;
    JScrollPane sp;
    JFrame f=new JFrame();
    JTabbedPane tp = new JTabbedPane();
    bgimg b=new bgimg();
    BufferedImage img;
    String[] colname,name,namet,namewp,nameso;
    String dept,to;
    Object[][] data;
    Object[] infodata;
    int totrow,selrow,wallamt,totsto=0;;
    public prodproc(String ust) throws Exception{
        JLabel bg=b.bg("C:/Users/Admin/Downloads/producer.jpeg");
        bg.setBounds(-10, -100, 1300,800 );

        JButton lo=new JButton("Logout");
        lo.setBounds(1150,0,100,30);
        lo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                new signin();
                f.dispose();
            }
        });
        JPanel jp=new JPanel();
        jp.setBounds(30,100,200, 350);
        jp.setLayout(null);

        inbp.setBounds(300,80,700,450);
        inbp.setVisible(false);
        
        JButton qc=new JButton("QUALITY CHECK");
        qc.setBounds(20,50,150,50);
        
        JButton wp=new JButton("PROCESS WOOL");
        wp.setBounds(20,150,150,50);
        
        JButton so=new JButton("STORE WOOL");
        so.setBounds(20,250,150,50);
        
        jp.add(qc);
        jp.add(so);
        jp.add(wp);

        JLabel wal=new JLabel("",JLabel.CENTER);
        wal.setBounds(110,0,100,30);
        wal.setOpaque(true);
        img=ImageIO.read(new File("C:/Users/Admin/Downloads/goldcoin.png"));
        Image im = img.getScaledInstance(30,30, Image.SCALE_SMOOTH);
        wal.setIcon(new ImageIcon(im));
        sqlconnection sq=new sqlconnection();
        sq.st.executeUpdate("create table if not exists "+ust+"producerwall (id int primary key auto_increment,`from` char(30),dept char(20),`to` char(30),amt int)");
        sq.res=sq.st.executeQuery("select count(*) from "+ust+"producerwall");
        sq.res.next();
        if(sq.res.getInt("count(*)")==0) wal.setText("0");
        else{
            sq.res=sq.st.executeQuery("select amt from "+ust+"producerwall where `from` is NULL and dept is NULL and `to` is NULL order by id desc limit 1");
            sq.res.next();
            wallamt=sq.res.getInt("amt");
            wal.setText(Integer.toString(wallamt));
        }
        
        JPopupMenu walpm=new JPopupMenu();
        JMenuItem deposit = new JMenuItem("Deposit");
        JMenuItem withdraw = new JMenuItem("Withdraw");
        Dimension d=new Dimension(100,30);
        deposit.setPreferredSize(d);
        withdraw.setPreferredSize(d);
        wal.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                walpm.show(wal, 0, 30);
            }
            public void mouseClicked(MouseEvent e){
                walpm.show(wal, 100, 0);
            }
        });
        f.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                walpm.setVisible(false);
            }
        });
        deposit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JTextField txt = new JTextField(10);
                JPanel p = new JPanel();
                p.add(new JLabel("Enter an amount to deposit:"));
                p.add(txt);
                int opt=JOptionPane.showConfirmDialog(f,p,"Custom MessageBox",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
                if(opt==JOptionPane.OK_OPTION){
                    try{
                        sqlconnection sq=new sqlconnection();
                        int amt=Integer.parseInt(wal.getText())+Integer.parseInt(txt.getText());
                        sq.st.executeUpdate("insert into "+ust+"producerwall(amt)values("+amt+")");
                        wallamt=amt;
                        wal.setText(Integer.toString(amt));
                    }
                    catch(Exception ex){ex.printStackTrace();}
                }
            }
        });
        withdraw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JTextField txt = new JTextField(10);
                JPanel p = new JPanel();
                p.add(new JLabel("Enter an amount to withdraw:"));
                p.add(txt);
                int opt=JOptionPane.showConfirmDialog(f,p,"Custom MessageBox",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
                if(opt==JOptionPane.OK_OPTION){
                    try{
                        sqlconnection sq=new sqlconnection();
                        int amt=Integer.parseInt(wal.getText())-Integer.parseInt(txt.getText());
                        if(amt>=0){
                            sq.st.executeUpdate("insert into "+ust+"producerwall(amt)values("+amt+")");
                            wallamt=amt;
                            wal.setText(Integer.toString(amt));
                        }
                        else JOptionPane.showMessageDialog(null,"Invalid withdraw amount","NOTE",JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch(Exception ex){ex.printStackTrace();}
                }
            }
        });
        walpm.add(deposit);
        walpm.add(withdraw);
        f.add(wal);

        JLabel inb=new JLabel("Inbox",JLabel.CENTER);
        inb.setBounds(0,0,100,30);
        inb.setOpaque(true);
        img=ImageIO.read(new File("C:/Users/Admin/Downloads/mail.jpg"));
        im = img.getScaledInstance(30,30, Image.SCALE_SMOOTH);
        inb.setIcon(new ImageIcon(im));
        sq.st.executeUpdate("create table if not exists "+ust+"producerinbox (id int primary key auto_increment,`from` char(30),info char(200),payment char(3) default 'yes')");
        sq.res=sq.st.executeQuery("select count(*) from "+ust+"producerinbox where payment='yes'");
        sq.res.next();
        inb.setText("Inbox:"+sq.res.getInt("count(*)"));
        f.add(inb);
        inb.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent E){
                try{
                    sq.res=sq.st.executeQuery("select count(*) from "+ust+"producerinbox where payment='yes'");
                    sq.res.next();
                    if(sq.res.getInt("count(*)")!=0){
                        sq.res=sq.st.executeQuery("select info,id from "+ust+"producerinbox");
                        while(sq.res.next()){
                            if(sq.res.getString("info").equals("$cancel")){
                                try{
                                    sqlconnection sq1=new sqlconnection();
                                    sq1.res=sq1.st.executeQuery("select * from "+ust+"producerinbox where id="+sq.res.getString("id"));
                                    sq1.res.next();
                                    String msg=sq1.res.getString("from")+" cancelled your request";
                                    JOptionPane.showMessageDialog(null,msg,"NOTE",JOptionPane.INFORMATION_MESSAGE);
                                    sq1.st.executeUpdate("delete from "+ust+"producerinbox where id="+sq.res.getString("id"));
                                }
                                catch(Exception ex){ex.printStackTrace();}
                            }
                        }
                        sq.res=sq.st.executeQuery("select count(*) from "+ust+"producerinbox where payment='yes'");
                        sq.res.next();
                        inb.setText("Inbox:"+sq.res.getInt("count(*)"));
                        if(sq.res.getInt("count(*)")!=0) fetch(ust);
                        if(table!=null){
                            table.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent e){
                                    selrow=table.getSelectedRow();
                                    if(selrow!=-1){
                                        String info[]=((String)infodata[selrow]).split("\\n");
                                        JPanel pay=new JPanel();
                                        int topay=Integer.parseInt((info[info.length-1].split(":"))[1]);
                                        JLabel amt=new JLabel("Amount to pay:"+topay);
                                        amt.setFont(new Font("Arial", Font.BOLD, 15));
                                        String remdata=info[0];
                                        for(int i=1;i<info.length-1;i++){
                                            remdata+="\n";
                                            remdata+=info[i];
                                        }
                                        JLabel reml=new JLabel("<html>"+remdata.replace("\n", "<br>") + "</html>");
                                        reml.setFont(new Font("Arial", Font.PLAIN, 12));
                                        pay.add(amt);
                                        pay.add(reml);
                                        int chk=JOptionPane.showOptionDialog(f,pay,"INFO",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Pay", "Cancel"},"pay");
                                        if(chk==JOptionPane.OK_OPTION){
                                            String remdatains[]=remdata.split("\nPricing details");
                                            if(wallamt-topay>=0){
                                                try{
                                                    sq.st.executeUpdate("insert into "+ust+"producerwall(amt)values("+(wallamt-topay)+")");
                                                    wal.setText(Integer.toString(wallamt-topay));
                                                    sq.st.executeUpdate("update "+ust+"producerinbox set info='"+remdatains[0]+ "'where id="+data[selrow][0]);
                                                    sq.st.executeUpdate("update "+ust+"producerinbox set payment='no' where id="+data[selrow][0]);
                                                    sq.st.executeUpdate("insert into "+data[selrow][1]+"wall(`from`,dept,amt)values('"+ust+"','producer','"+topay+"')");
                                                    
                                                    sq.res=sq.st.executeQuery("select count(*) from "+data[selrow][1]+"wall where `from` is NULL and dept is NULL and `to` is NULL order by id desc limit 1");
                                                    sq.res.next();
                                                    int avaiamt;
                                                    if(sq.res.getInt("count(*)")!=0){
                                                        sq.res=sq.st.executeQuery("select amt from "+data[selrow][1]+"wall where `from` is NULL and dept is NULL and `to` is NULL order by id desc limit 1");
                                                        sq.res.next();
                                                        avaiamt=sq.res.getInt("amt");
                                                        avaiamt+=topay;
                                                        sq.st.executeUpdate("insert into "+data[selrow][1]+"wall(amt) values('"+avaiamt+"')");
                                                    }
                                                    else sq.st.executeUpdate("insert into "+data[selrow][1]+"wall(amt) values('"+topay+"')");
                                                    String toname=(String)data[selrow][1];
                                                    if(toname.contains("woolprocess")){
                                                        to=(toname.split("woolprocess"))[0];
                                                        dept="woolprocess";
                                                    }
                                                    else if(toname.contains("transport")){
                                                        to=(toname.split("transport"))[0];
                                                        dept="transport";
                                                    }
                                                    else if(toname.contains("storageowner")){
                                                        to=(toname.split("storageowner"))[0];
                                                        dept="storageowner";
                                                    }
                                                    else if(toname.contains("qualitychecker")){
                                                        to=(toname.split("qualitychecker"))[0];
                                                        dept="qualitychecker";
                                                    }
                                                    sq.st.executeUpdate("insert into "+ust+"producerwall(dept,`to`,amt) values('"+dept+"','"+to+"','"+(wallamt-topay)+"')");
                                                    JOptionPane.showMessageDialog(null,"Payment Successfull","NOTE",JOptionPane.INFORMATION_MESSAGE);
                                                    tp.setVisible(true);
                                                    inbp.setVisible(false);
                                                    sq.res=sq.st.executeQuery("select count(*) from "+ust+"producerinbox where payment='yes'");
                                                    sq.res.next();
                                                    inb.setText("Inbox:"+sq.res.getInt("count(*)"));
                                                }
                                                catch(Exception ex){ex.printStackTrace();}
                                            }
                                            else JOptionPane.showMessageDialog(null,"Insufficient balance","NOTE",JOptionPane.INFORMATION_MESSAGE);
                                        }
                                    }
                                }
                            });
                        }
                    }
                    else JOptionPane.showMessageDialog(null,"No Messages","NOTE",JOptionPane.INFORMATION_MESSAGE);
                }
                catch(Exception ex){ex.printStackTrace();}
            }
        });

        Timer t2 = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(val>=10) ((Timer) e.getSource()).stop();
                else{
                    jp.setBounds(val,100,200, 350);
                    val+=3;
                }
            }
        });
        jp.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                t2.start();
            }
            public void mouseExited(MouseEvent e){
                val=10;
            }
        });

        int count=0;
        try{
            sq.res=sq.st.executeQuery("select count(id)  from qualitychecker");
            if(sq.res.next()) count=sq.res.getInt("count(id)");
            name=new String[count+1];
            name[0]="Select anyone";
            sq.res=sq.st.executeQuery("select name,exp from qualitychecker where availablity='yes'");
            for(int i=1;i<=count;i++){
                sq.res.next();
                name[i]=sq.res.getString("name")+" "+sq.res.getString("exp");
            }
        }
        catch(SQLException sqex){
            name=new String[1];
            name[0]="Empty";
        }
        
        try{
            sq.res=sq.st.executeQuery("select count(id)  from transport where availablity='yes'");
            if(sq.res.next()) count=sq.res.getInt("count(id)");
            namet=new String[count+1];
            namet[0]="Select anyone";
            sq.res=sq.st.executeQuery("select name,exp from transport where availablity='yes'");
            for(int i=1;i<=count;i++){
                sq.res.next();
                namet[i]=sq.res.getString("name")+" "+sq.res.getString("exp");
            }
        }
        catch(SQLException sqex){
            namet=new String[1];
            namet[0]="Empty";
        }
        
        try{
            sq.res=sq.st.executeQuery("select count(id)  from woolprocess");
            if(sq.res.next()) count=sq.res.getInt("count(id)");
            namewp=new String[count+1];
            namewp[0]="Select anyone";
            sq.res=sq.st.executeQuery("select name,exp from woolprocess where availablity='yes'");
            for(int i=1;i<=count;i++){
                sq.res.next();
                namewp[i]=sq.res.getString("name")+" "+sq.res.getString("exp");
            }
        }
        catch(SQLException sqex){
            namewp=new String[1];
            namewp[0]="Empty";
        }

        try{
            sq.res=sq.st.executeQuery("select count(id)  from storageowner");
            if(sq.res.next()) count=sq.res.getInt("count(id)");
            nameso=new String[count+1];
            nameso[0]="Select anyone";
            sq.res=sq.st.executeQuery("select name,storage from storageowner where availablity='yes'");
            for(int i=1;i<=count;i++){
                sq.res.next();
                nameso[i]=sq.res.getString("name")+" "+sq.res.getString("storage");
            }
        }
        catch(SQLException sqex){
            nameso=new String[1];
            nameso[0]="Empty";
        }

        tp.setBounds(300,30,650,550);
        
        Timer t = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(val<=-190) ((Timer) e.getSource()).stop();
                else{
                    jp.setBounds(val,100,200, 350);
                    val-=3;
                }
            }
        });
        tp.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){
                t.start();
            }
        });
        JPanel qcp=new JPanel();
        qcp.setBounds(0,0,650,550);
        qcp.setLayout(null);
        qcp.setVisible(true);

        JLabel tw=new JLabel("Total sheep",JLabel.CENTER);
        tw.setBounds(200,100,120,30);
        tw.setOpaque(true);
        tw.setBackground(Color.WHITE);
        tw.setForeground(Color.decode("#19e668"));

        JTextField twt=new JTextField();
        twt.setBounds(380,100,120,30);
        
        JLabel qcl=new JLabel("quality checker",JLabel.CENTER);
        qcl.setBounds(200,200,120,30);
        qcl.setOpaque(true);
        qcl.setBackground(Color.WHITE);
        qcl.setForeground(Color.decode("#19e668"));
        
        JComboBox<String> nam=new JComboBox<String>(name);
        nam.setBounds(380,200,130,30);
        nam.setSelectedIndex(0);
        nam.setBackground(Color.WHITE);
        nam.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel tl=new JLabel("Transport",JLabel.CENTER);
        tl.setBounds(200,300,120,30);
        tl.setOpaque(true);
        tl.setBackground(Color.WHITE);
        tl.setForeground(Color.decode("#19e668"));

        JComboBox<String> namt=new JComboBox<String>(namet);
        namt.setSelectedIndex(0);
        namt.setBounds(380,300,130,30);
        namt.setBackground(Color.WHITE);
        namt.setFont(new Font("Arial", Font.PLAIN, 15));

        qcp.add(tw);
        qcp.add(twt);
        qcp.add(qcl);
        qcp.add(nam);
        qcp.add(namt);
        qcp.add(tl);

        JPanel pwp=new JPanel();
        pwp.setBounds(0,0,650,550);
        pwp.setLayout(null);
        pwp.setVisible(true);
        JLabel wptw=new JLabel("Total weight",JLabel.CENTER);
        wptw.setBounds(200,100,120,30);
        wptw.setOpaque(true);
        wptw.setBackground(Color.WHITE);
        wptw.setForeground(Color.decode("#19e668"));

        JTextField wptwt=new JTextField();
        wptwt.setBounds(380,100,120,30);
        
        JLabel wpqcl=new JLabel("woolprocess",JLabel.CENTER);
        wpqcl.setBounds(200,200,120,30);
        wpqcl.setOpaque(true);
        wpqcl.setBackground(Color.WHITE);
        wpqcl.setForeground(Color.decode("#19e668"));
        

        JComboBox<String> wpnam=new JComboBox<String>(namewp);
        wpnam.setSelectedIndex(0);
        wpnam.setBounds(380,200,130,30);
        wpnam.setBackground(Color.WHITE);
        wpnam.setFont(new Font("Arial", Font.PLAIN, 15));

         JLabel wptl=new JLabel("Transport",JLabel.CENTER);
        wptl.setBounds(200,300,120,30);
        wptl.setOpaque(true);
        wptl.setBackground(Color.WHITE);
        wptl.setForeground(Color.decode("#19e668"));

        JComboBox<String> wpnamt=new JComboBox<String>(namet);
        wpnamt.setSelectedIndex(0);
        wpnamt.setBounds(380,300,130,30);
        wpnamt.setBackground(Color.WHITE);
        wpnamt.setFont(new Font("Arial", Font.PLAIN, 15));

        pwp.add(wptw);
        pwp.add(wptwt);
        pwp.add(wpqcl);
        pwp.add(wpnamt);
        pwp.add(wptl);
        pwp.add(wpnam);

        JPanel swp=new JPanel();
        swp.setBounds(0,0,650,550);
        swp.setLayout(null);
        swp.setVisible(true);
        JLabel swtw=new JLabel("Total weight",JLabel.CENTER);
        swtw.setBounds(200,100,120,30);
        swtw.setOpaque(true);
        swtw.setBackground(Color.WHITE);
        swtw.setForeground(Color.decode("#19e668"));

        JTextField swtwt=new JTextField();
        swtwt.setBounds(380,100,120,30);
        
        JLabel swqcl=new JLabel("Store wool",JLabel.CENTER);
        swqcl.setBounds(200,200,120,30);
        swqcl.setOpaque(true);
        swqcl.setBackground(Color.WHITE);
        swqcl.setForeground(Color.decode("#19e668"));
      

        JComboBox<String> swnam=new JComboBox<String>(nameso);
        swnam.setSelectedIndex(0);
        swnam.setBounds(380,200,130,30);
        swnam.setBackground(Color.WHITE);
        swnam.setFont(new Font("Arial", Font.PLAIN, 15));

         JLabel swtl=new JLabel("Transport",JLabel.CENTER);
        swtl.setBounds(200,300,120,30);
        swtl.setOpaque(true);
        swtl.setBackground(Color.WHITE);
        swtl.setForeground(Color.decode("#19e668"));

        JComboBox<String> swnamt=new JComboBox<String>(namet);
        swnamt.setSelectedIndex(0);
        swnamt.setBounds(380,300,130,30);
        swnamt.setBackground(Color.WHITE);
        swnamt.setFont(new Font("Arial", Font.PLAIN, 15));

        swp.add(swtw);
        swp.add(swtwt);
        swp.add(swqcl);
        swp.add(swnamt);
        swp.add(swtl);
        swp.add(swnam);

        tp.addTab("",qcp);
        tp.addTab("",pwp);
        tp.addTab("",swp);
        tp.setEnabled(false);
       // tp.setLayout(null);
        //tp.setOpaque(true);
        
        qc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                user="qualitychecker";
                tp.setSelectedIndex(0);
            }
        });
        wp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                tp.setSelectedIndex(1);
                user="woolprocess";
            }
        });
        so.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                tp.setSelectedIndex(2);
                user="storageowner";
            }
        });

        JButton book=new JButton("Book");
        book.setBounds(550,620,70,30);
        book.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(user.equals("qualitychecker")){
                    if(((String)nam.getSelectedItem()).equals("Empty") || ((String)namt.getSelectedItem()).equals("Empty")){
                        JOptionPane.showMessageDialog(null,"Unable to book","NOTE",JOptionPane.INFORMATION_MESSAGE);}
                    else{
                        if(((String)nam.getSelectedItem()).equals("Select anyone") || ((String)namt.getSelectedItem()).equals("Select anyone")){
                            JOptionPane.showMessageDialog(null,"Fill up the details","NOTE",JOptionPane.INFORMATION_MESSAGE);}
                        else{
                            String na=(String)nam.getSelectedItem();
                            String na1="";
                            for(char c:na.toCharArray()){ if(Character.isLetter(c)) na1+=c;}
                            try{sq.st.executeUpdate("insert into "+na1+"qualitycheckerinbox (`from`,info) values ('"+ust+"producer','"+twt.getText()+"\\n"+(String)namt.getSelectedItem()+"')");}
                            catch(Exception ex){ex.printStackTrace();}
                            JOptionPane.showMessageDialog(null,"Booked your request","NOTE",JOptionPane.INFORMATION_MESSAGE);
                            try{
                                new prodproc(ust);
                                f.dispose();
                            }
                            catch(Exception ex){ex.printStackTrace();}
                        }
                    }
                }
                else if(user.equals("woolprocess")){
                    if(((String)wpnam.getSelectedItem()).equals("Empty") || ((String)wpnamt.getSelectedItem()).equals("Empty")){
                        JOptionPane.showMessageDialog(null,"Unable to book","NOTE",JOptionPane.INFORMATION_MESSAGE);}
                    else{
                        if(((String)wpnam.getSelectedItem()).equals("Select anyone") || ((String)wpnamt.getSelectedItem()).equals("Select anyone")){
                            JOptionPane.showMessageDialog(null,"Fill up the details","NOTE",JOptionPane.INFORMATION_MESSAGE);}
                        else{
                            String na=(String)wpnam.getSelectedItem();
                            String na1="";
                            for(char c:na.toCharArray()){ if(Character.isLetter(c)) na1+=c;}
                            try{sq.st.executeUpdate("insert into "+na1+"woolprocessinbox (`from`,info) values ('"+ust+"producer','"+wptwt.getText()+"\\n"+(String)wpnamt.getSelectedItem()+"')");}
                            catch(Exception ex){ex.printStackTrace();}
                            JOptionPane.showMessageDialog(null,"Booked your request","NOTE",JOptionPane.INFORMATION_MESSAGE);
                            try{
                                new prodproc(ust);
                                f.dispose();
                            }
                            catch(Exception ex){ex.printStackTrace();}
                        }
                    }
                }
                else if(user.equals("storageowner")){
                    if(((String)swnam.getSelectedItem()).equals("Empty") || ((String)swnamt.getSelectedItem()).equals("Empty")){
                        JOptionPane.showMessageDialog(null,"Unable to book","NOTE",JOptionPane.INFORMATION_MESSAGE);}
                    else{
                        if(((String)swnam.getSelectedItem()).equals("Select anyone") || ((String)swnamt.getSelectedItem()).equals("Select anyone")){
                            JOptionPane.showMessageDialog(null,"Fill up the details","NOTE",JOptionPane.INFORMATION_MESSAGE);}
                        else{
                            String na=(String)swnam.getSelectedItem();
                            String na1="";
                            for(char c:na.toCharArray()){ if(Character.isLetter(c)) na1+=c;}
                            try{
                                sq.res=sq.st.executeQuery("select info from "+na1+"storageownerinbox");
                                while (sq.res.next()) totsto+=Integer.parseInt(((sq.res.getString("info")).split("\\n"))[0]);
                                totsto+=Integer.parseInt(swtwt.getText());
                                sq.res=sq.st.executeQuery("select storage from storageowner where name='"+na1+"'");
                                sq.res.next();
                                if(sq.res.getInt("storage")>=totsto) sq.st.executeUpdate("insert into "+na1+"storageownerinbox (`from`,info) values ('"+ust+"producer','"+swtwt.getText()+"\\n"+(String)swnamt.getSelectedItem()+"')");
                                else{
                                    JOptionPane.showMessageDialog(null,"Insufficient storage","NOTE",JOptionPane.INFORMATION_MESSAGE);
                                    totsto=0;
                                    return;
                                }
                            }
                            catch(Exception ex){ex.printStackTrace();}
                            JOptionPane.showMessageDialog(null,"Booked your request","NOTE",JOptionPane.INFORMATION_MESSAGE);
                            try{
                                new prodproc(ust);
                                f.dispose();
                            }
                            catch(Exception ex){ex.printStackTrace();}
                        }
                    }
                }
            }
        });
        f.add(book);
        f.add(lo);
        f.add(jp);
        f.add(tp);
        f.add(inbp);
        f.add(bg);
        
        tp.setVisible(true);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
        private void fetch(String ust){
            try{
                sqlconnection sq=new sqlconnection();
                sq.st=sq.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                sq.res=sq.st.executeQuery("select * from "+ust+"producerinbox where payment='yes'");
                ResultSetMetaData md=sq.res.getMetaData();
                int totcol = md.getColumnCount();
                totrow = getRowCount(sq.res);
                colname = new String[totcol];
                data = new Object[totrow][totcol];
                infodata=new Object[totrow];
                for (int i = 1; i <= totcol; i++) colname[i-1] = md.getColumnName(i);
                int k=0;
                while(sq.res.next()){
                    for (int i = 1; i <= totcol; i++){
                        if(i!=totcol-1) data[k][i-1] = sq.res.getObject(i);
                        else{
                            infodata[k]=sq.res.getObject(i);
                            data[k][totcol-2]="Job Completed";
                        }
                    }
                    k+=1;
                }
                addtab();
            }
            catch(Exception ex){ex.printStackTrace();}
        }
        private int getRowCount(ResultSet rs) throws SQLException {
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            rs.beforeFirst();
            return rowCount;
        }
        private void addtab(){
            if(table==null){
                table = new JTable(data, colname);
                sp = new JScrollPane(table);
            }
            table.setRowHeight(25);
            int height=(totrow*25)+30;
            if(height<450) table.setPreferredSize(new Dimension(700,450));
            else table.setPreferredSize(new Dimension(700,height));
            
            sp.setPreferredSize(new Dimension(700,450));
            inbp.add(sp, BorderLayout.CENTER);
            tp.setVisible(false);
            inbp.setVisible(true);
            f.setVisible(true);
        }
    }
