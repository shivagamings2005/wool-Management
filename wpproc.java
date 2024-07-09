package project;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.awt.*;

public class wpproc {
    Image bg;
    JFrame f=new JFrame();
    bgimg b=new bgimg();
    JPanel inbp=new JPanel();
    JTabbedPane tp = new JTabbedPane();
    JTable table;
    JScrollPane sp;
    int totrow,selrow,totamt=0;
    String prname,inf="";
    String[] colname;
    Object[][] data;
    String[][] infodata;
    public wpproc(String ust) throws Exception{
        JLabel bg=b.bg("C:/Users/Admin/Downloads/process.png");
        bg.setBounds(-10, -100, 1300,800 );

        JButton lo=new JButton("Logout");
        lo.setBounds(1150,0,100,30);
        lo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                new signin();
                f.dispose();
            }
        });
        f.add(lo);

        inbp.setBounds(300,80,700,450);
        inbp.setVisible(false);

        JLabel wal=new JLabel("",JLabel.CENTER);
        wal.setBounds(100,0,100,30);
        wal.setOpaque(true);
        BufferedImage img=ImageIO.read(new File("C:/Users/Admin/Downloads/goldcoin.png"));
        Image im = img.getScaledInstance(30,30, Image.SCALE_SMOOTH);
        wal.setIcon(new ImageIcon(im));
        sqlconnection sq=new sqlconnection();
        sq.st.executeUpdate("create table if not exists "+ust+"woolprocesswall (id int primary key auto_increment,`from` char(30),dept char(20),`to` char(30),amt int)");
        sq.res=sq.st.executeQuery("select count(*) from "+ust+"woolprocesswall where `from` is NULL and dept is NULL and `to` is NULL order by id desc limit 1");
        sq.res.next();
        if(sq.res.getInt("count(*)")==0) wal.setText("0");
        else{
            sq.res=sq.st.executeQuery("select amt from "+ust+"woolprocesswall where `from` is NULL and dept is NULL and `to` is NULL order by id desc limit 1");
            if(sq.res.next())wal.setText(Integer.toString(sq.res.getInt("amt")));
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
                        int amt=Integer.parseInt(wal.getText())+Integer.parseInt(txt.getText());
                        sq.st.executeUpdate("insert into "+ust+"woolprocesswall(amt)values("+amt+")");
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
                        int amt=Integer.parseInt(wal.getText())-Integer.parseInt(txt.getText());
                        if(amt>=0){
                            sq.st.executeUpdate("insert into "+ust+"woolprocesswall(amt)values("+amt+")");
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
        
        JLabel inb=new JLabel("",JLabel.CENTER);
        inb.setBounds(0,0,100,30);
        inb.setOpaque(true);
        img=ImageIO.read(new File("C:/Users/Admin/Downloads/mail.jpg"));
        im = img.getScaledInstance(30,30, Image.SCALE_SMOOTH);
        inb.setIcon(new ImageIcon(im));
        sq.st.executeUpdate("create table if not exists "+ust+"woolprocessinbox (id int primary key auto_increment,`from` char(30),info char(100))");
        sq.res=sq.st.executeQuery("select count(*) from "+ust+"woolprocessinbox");
        sq.res.next();
        inb.setText("Inbox:"+sq.res.getInt("count(*)"));
        f.add(inb);
        
        JPanel qcp=new JPanel();
        qcp.setBounds(300,30,650,550);
        qcp.setLayout(null);
        
        JLabel ln=new JLabel("Provide the length");
        ln.setFont(new Font("Arial", Font.BOLD, 18));
        ln.setBounds(50,10,250,30);
        ln.setForeground(Color.decode("#19e668"));

        JLabel tw=new JLabel("Ultra Fine",JLabel.CENTER);
        tw.setBounds(200,50,120,30);
        tw.setOpaque(true);
        tw.setBackground(Color.WHITE);
        tw.setForeground(Color.decode("#19e668"));

        JTextField twt=new JTextField();
        twt.setBounds(380,50,120,30);
        
        JLabel qcl=new JLabel("Super Fine",JLabel.CENTER);
        qcl.setBounds(200,150,120,30);
        qcl.setOpaque(true);
        qcl.setBackground(Color.WHITE);
        qcl.setForeground(Color.decode("#19e668"));

        JTextField qclt=new JTextField();
        qclt.setBounds(380,150,120,30);

        JLabel qt=new JLabel("Fine",JLabel.CENTER);
        qt.setBounds(200,250,120,30);
        qt.setOpaque(true);
        qt.setBackground(Color.WHITE);
        qt.setForeground(Color.decode("#19e668"));

        JTextField nam=new JTextField();
        nam.setBounds(380,250,120,30);
        
        JLabel tl=new JLabel("medium",JLabel.CENTER);
        tl.setBounds(200,350,120,30);
        tl.setOpaque(true);
        tl.setBackground(Color.WHITE);
        tl.setForeground(Color.decode("#19e668"));

        JTextField namt=new JTextField();
        namt.setBounds(380,350,120,30);

        JLabel bt=new JLabel("Course",JLabel.CENTER);
        bt.setBounds(200,450,120,30);
        bt.setOpaque(true);
        bt.setBackground(Color.WHITE);
        bt.setForeground(Color.decode("#19e668"));

        JTextField btt=new JTextField();
        btt.setBounds(380,450,120,30);

        JButton nxt=new JButton("Next");
        nxt.setBounds(550,480,70,30);

        JButton post=new JButton("Post");
        post.setBounds(550,480,70,30);

        qcp.add(ln);
        qcp.add(tw);
        qcp.add(twt);
        qcp.add(qcl);
        qcp.add(qclt);
        qcp.add(qt);
        qcp.add(nam);
        qcp.add(tl);
        qcp.add(namt);
        qcp.add(bt);
        qcp.add(btt);
        qcp.add(nxt);

        JPanel qcp1=new JPanel();
        qcp1.setBounds(300,30,650,550);
        qcp1.setLayout(null);

        tp.setBounds(300,30,650,550);
        tp.addTab("",qcp);
        tp.addTab("",qcp1);
        tp.setEnabled(false);
        tp.setVisible(false);

        nxt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Component comp[]=qcp.getComponents();
                for(Component c:comp){
                    if (c instanceof JLabel){
                        if(!(((JLabel) c).getText().equals("Provide the length"))){
                            inf+=((JLabel) c).getText();
                            inf+=":";
                        }
                    }
                     else if (c instanceof JTextComponent) {
                        inf+=((JTextComponent) c).getText();
                        inf+="\\n";
                     }
                     else continue;
                }
                inf+="Pricing details\\n";
                ln.setText("Provide the price of each");
                tw.setText("Clensing");
                qcl.setText("Washing");
                qt.setText("Carding");
                tl.setText("Spinning");
                bt.setText("Finishing");
                twt.setText("");
                qclt.setText("");
                namt.setText("");
                nam.setText("");
                btt.setText("");
                qcp1.add(ln);
                qcp1.add(tw);
                qcp1.add(twt);
                qcp1.add(qcl);
                qcp1.add(qclt);
                qcp1.add(qt);
                qcp1.add(nam);
                qcp1.add(tl);
                qcp1.add(namt);
                qcp1.add(bt);
                qcp1.add(btt);
                qcp1.add(post);
                tp.setSelectedIndex(1);
                post.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent E){
                        Component comp[]=qcp1.getComponents();
                        for(Component c:comp){
                            if (c instanceof JLabel){
                                if(!(((JLabel) c).getText().equals("Provide the price of each"))){
                                    inf+=((JLabel) c).getText();
                                    inf+=":";
                                }
                            }
                            else if (c instanceof JTextComponent) {
                                inf+=((JTextComponent) c).getText();
                                totamt+=Integer.parseInt(((JTextComponent) c).getText());
                                inf+="\\n";
                            }
                            else continue;
                        }
                        inf+=("amt:"+Integer.toString(totamt));
                        try{
                            JPanel postp=new JPanel();
                            int count=0;
                            sq.res=sq.st.executeQuery("select count(id)  from transport where availablity='yes'");
                            if(sq.res.next()) count=sq.res.getInt("count(id)");
                            String namet[]=new String[count+1];
                            namet[0]="Select anyone";
                            sq.res=sq.st.executeQuery("select name,exp from transport where availablity='yes'");
                            for(int i=1;i<=count;i++){
                                sq.res.next();
                                namet[i]=sq.res.getString("name")+" "+sq.res.getString("exp");
                            }
                            
                            JLabel tl=new JLabel("Transport",JLabel.CENTER);
                            tl.setOpaque(true);
                            tl.setBackground(Color.WHITE);
                            tl.setForeground(Color.decode("#19e668"));

                            JComboBox<String> namt=new JComboBox<String>(namet);
                            namt.setBackground(Color.WHITE);
                            namt.setFont(new Font("Arial", Font.PLAIN, 12));
                            postp.add(tl);
                            postp.add(namt);
                            int opt=JOptionPane.showConfirmDialog(f,postp,"Custom MessageBox",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
                            if(opt==JOptionPane.OK_OPTION){
                                sq.st.executeUpdate("delete from "+ust+"woolprocessinbox where `from`='"+prname+"'");
                                sq.st.executeUpdate("update woolprocess set availablity='yes' where name='"+ust+"'");
                                String tr=String.valueOf(namt.getSelectedItem());
                                String na1="";
                                for(char c:tr.toCharArray()){if(Character.isLetter(c)) na1+=c;}
                                sq.res=sq.st.executeQuery("select address from producer where name='"+prname.split("producer")[0]+"'");
                                sq.res.next();
                                String toad=sq.res.getString("address");
                                sq.res=sq.st.executeQuery("select address from woolprocess where name='"+ust+"'");
                                sq.res.next();
                                String frad=sq.res.getString("address");
                                sq.st.executeUpdate("insert into "+na1+"transportinbox (`from`,info) values ('"+ust+"woolprocess','"+frad+"\n"+toad+"')");
                                sq.st.executeUpdate("insert into "+prname+"inbox (`from`,info) values ('"+ust+"woolprocess','"+inf+"')");
                                new wpproc(ust);
                                f.dispose();
                            }
                        }
                        catch(Exception ex){ex.printStackTrace();}
                    }
                });
            }
        });

        inb.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent E){
                try{
                    if(sq.res.getInt("count(*)")!=0){
                        fetch(ust);
                        if(table!=null){
                            table.addMouseListener(new MouseAdapter() {
                                public void mouseClicked(MouseEvent e){
                                    selrow=table.getSelectedRow();
                                    if(selrow!=-1){
                                        prname = (String) table.getValueAt(selrow, 1);
                                        String msg="wanna accept the request from "+prname;
                                        int chk=JOptionPane.showOptionDialog(null,msg,"Accept or Decline",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Accept", "Decline"},"Accept");
                                        String prodname=prname.split("producer")[0];
                                        if(chk==JOptionPane.YES_OPTION){
                                            try{
                                                sq.res=sq.st.executeQuery("select address from producer where name='"+prodname+"'");
                                                sq.res.next();
                                                String frad=sq.res.getString("address");
                                                sq.res=sq.st.executeQuery("select address from woolprocess where name='"+ust+"'");
                                                sq.res.next();
                                                String toad=sq.res.getString("address");
                                                sq.st.executeUpdate("update woolprocess set availablity='no' where name='"+ust+"'");
                                                sq.st.executeUpdate("insert into "+infodata[selrow][1]+"transportinbox (`from`,info) values ('"+prname+"','"+frad+"\n"+toad+"')");//info from adr,to adr
                                                tp.setVisible(true);
                                                inbp.setVisible(false);
                                                f.repaint();
                                            }
                                            catch(Exception ex){ex.printStackTrace();}
                                        }
                                        else{
                                            try{
                                                sq.st.executeUpdate("delete from "+ust+"woolprocessinbox where `from`='"+prname+"'");
                                                sq.st.executeUpdate("insert into "+prodname+"producerinbox (`from`,info) values ('"+ust+"woolprocess','$cancel')");
                                                inbp.setVisible(false);
                                                sq.res=sq.st.executeQuery("select count(*) from "+ust+"woolprocessinbox");
                                                sq.res.next();
                                                inb.setText("Inbox:"+sq.res.getInt("count(*)"));
                                            }
                                            catch(Exception ex){ex.printStackTrace();}
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

        f.add(inb);
        f.add(inbp);
        f.add(tp);
        f.add(bg);
        f.setLayout(null);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    private void fetch(String ust){
        try{
            sqlconnection sq=new sqlconnection();
            sq.st=sq.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sq.res=sq.st.executeQuery("select * from "+ust+"woolprocessinbox");
            ResultSetMetaData md=sq.res.getMetaData();
            int totcol = md.getColumnCount();
            totrow = getRowCount(sq.res);
            colname = new String[totcol];
            data = new Object[totrow][totcol];
            infodata =new String[totrow][];
            for (int i = 1; i < totcol; i++) colname[i-1] = md.getColumnName(i);
            colname[totcol-1]="Total Weight";
            int k=0;
            while(sq.res.next()){
                for (int i = 1; i <= totcol; i++){
                    if(i!=totcol) data[k][i-1] = sq.res.getObject(i);
                    else{
                        infodata[k]=(String.valueOf(sq.res.getObject(i))).split("\\n");
                        data[k][i-1]=infodata[k][0];
                        String na1="";
                        for(char c:infodata[k][1].toCharArray()){if(Character.isLetter(c)) na1+=c;}
                        infodata[k][1]=na1;
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
        inbp.setVisible(true);
        f.setVisible(true);
    }

}