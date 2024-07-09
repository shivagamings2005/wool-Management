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

public class qcproc {
    Image bg;
    JFrame f=new JFrame();
    bgimg b=new bgimg();
    JPanel inbp=new JPanel();
    JTable table;
    JScrollPane sp;
    int totrow,selrow;
    String prname;
    String[] colname;
    Object[][] data;
    String[][] infodata;
    public qcproc(String ust) throws Exception{
        JLabel bg=b.bg("C:/Users/Admin/Downloads/quality.jpg");
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
        sq.st.executeUpdate("create table if not exists "+ust+"qualitycheckerwall (id int primary key auto_increment,`from` char(30),dept char(20),`to` char(30),amt int)");
        sq.res=sq.st.executeQuery("select count(*) from "+ust+"qualitycheckerwall where `from` is NULL and dept is NULL and `to` is NULL order by id desc limit 1");
        sq.res.next();
        if(sq.res.getInt("count(*)")==0) wal.setText("0");
        else{
            sq.res=sq.st.executeQuery("select amt from "+ust+"qualitycheckerwall where `from` is NULL and dept is NULL and `to` is NULL order by id desc limit 1");
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
                        sq.st.executeUpdate("insert into "+ust+"qualitycheckerwall(amt)values("+amt+")");
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
                int opt=JOptionPane.showConfirmDialog(f,p,"Info",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
                if(opt==JOptionPane.OK_OPTION){
                    try{
                        int amt=Integer.parseInt(wal.getText())-Integer.parseInt(txt.getText());
                        if(amt>=0){
                            sq.st.executeUpdate("insert into "+ust+"qualitycheckerwall(amt)values("+amt+")");
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
        sq.st.executeUpdate("create table if not exists "+ust+"qualitycheckerinbox (id int primary key auto_increment,`from` char(30),info char(100))");
        sq.res=sq.st.executeQuery("select count(*) from "+ust+"qualitycheckerinbox");
        sq.res.next();
        inb.setText("Inbox:"+sq.res.getInt("count(*)"));
        f.add(inb);
        
        JPanel qcp=new JPanel();
        qcp.setBounds(300,30,650,550);
        qcp.setLayout(null);
        qcp.setVisible(false);
        
        JButton post=new JButton("Post");
        post.setBounds(550,620,70,30);
        post.setVisible(false);

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
                                                sq.res=sq.st.executeQuery("select address from qualitychecker where name='"+ust+"'");
                                                sq.res.next();
                                                String toad=sq.res.getString("address");
                                                sq.st.executeUpdate("update qualitychecker set availablity='no' where name='"+ust+"'");
                                                sq.st.executeUpdate("insert into "+infodata[selrow][1]+"transportinbox (`from`,info) values ('"+prname+"','"+frad+"\n"+toad+"')");//info from adr,to adr
                                                qcp.setVisible(true);
                                                post.setVisible(true);
                                                inbp.setVisible(false);
                                                f.setComponentZOrder(post, 0);
                                                f.repaint();
                                            }
                                            catch(Exception ex){ex.printStackTrace();}
                                        }
                                        else{
                                            try{
                                                sq.st.executeUpdate("delete from "+ust+"qualitycheckerinbox where `from`='"+prname+"'");
                                                sq.st.executeUpdate("insert into "+prodname+"producerinbox (`from`,info) values ('"+ust+"qualitychecker','$cancel')");
                                                inbp.setVisible(false);
                                                sq.res=sq.st.executeQuery("select count(*) from "+ust+"qualitycheckerinbox");
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

        JLabel tw=new JLabel("Staple Length",JLabel.CENTER);
        tw.setBounds(200,50,120,30);
        tw.setOpaque(true);
        tw.setBackground(Color.WHITE);
        tw.setForeground(Color.decode("#19e668"));

        JTextField twt=new JTextField();
        twt.setBounds(380,50,120,30);
        
        JLabel qcl=new JLabel("Staple diameter",JLabel.CENTER);
        qcl.setBounds(200,150,120,30);
        qcl.setOpaque(true);
        qcl.setBackground(Color.WHITE);
        qcl.setForeground(Color.decode("#19e668"));

        JTextField qclt=new JTextField();
        qclt.setBounds(380,150,120,30);
        
        JLabel qt=new JLabel("Staple type",JLabel.CENTER);
        qt.setBounds(200,250,120,30);
        qt.setOpaque(true);
        qt.setBackground(Color.WHITE);
        qt.setForeground(Color.decode("#19e668"));

        String s[]={"Select type","fleece","locks","broken","bellies"};
        JComboBox<String> nam=new JComboBox<String>(s);
        nam.setBounds(380,250,130,30);
        nam.setBackground(Color.WHITE);
        nam.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel tl=new JLabel("Staple avg colour",JLabel.CENTER);
        tl.setBounds(200,350,120,30);
        tl.setOpaque(true);
        tl.setBackground(Color.WHITE);
        tl.setForeground(Color.decode("#19e668"));

        String col[]={"Select colour","white","black","light grey","dark grey","light brown","dark brown"};
        JComboBox<String> namt=new JComboBox<String>(col);
        namt.setBounds(380,350,130,30);
        namt.setBackground(Color.WHITE);
        namt.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel bt=new JLabel("Staple breed type",JLabel.CENTER);
        bt.setBounds(200,450,120,30);
        bt.setOpaque(true);
        bt.setBackground(Color.WHITE);
        bt.setForeground(Color.decode("#19e668"));

        String bta[]={"Select breed","merino","debouillet","rambouillet","cornmo","comeback","bond","polwarth","targhee","tesswater","finnish landrance"};
        JComboBox<String> btt=new JComboBox<String>(bta);
        btt.setBounds(380,450,130,30);
        btt.setBackground(Color.WHITE);
        btt.setFont(new Font("Arial", Font.PLAIN, 15));

        
        post.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try{
                    JPanel postp=new JPanel();
                    int count=0;
                    JLabel reqamt=new JLabel("Enter charge:");
                    JTextField txt = new JTextField(10);
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

                    JComboBox<String> namtr=new JComboBox<String>(namet);
                    namtr.setBackground(Color.WHITE);
                    namtr.setFont(new Font("Arial", Font.PLAIN, 12));
                    postp.add(reqamt);
                    postp.add(txt);
                    postp.add(tl);
                    postp.add(namtr);
                    int opt=JOptionPane.showConfirmDialog(f,postp,"Info",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
                    if(opt==JOptionPane.OK_OPTION){
                        sq.st.executeUpdate("delete from "+ust+"qualitycheckerinbox where `from`='"+prname+"'");
                        sq.st.executeUpdate("update qualitychecker set availablity='yes' where name='"+ust+"'");
                        String tr=String.valueOf(namtr.getSelectedItem());
                        String na1="";
                        for(char c:tr.toCharArray()){if(Character.isLetter(c)) na1+=c;}
                        sq.res=sq.st.executeQuery("select address from producer where name='"+prname.split("producer")[0]+"'");
                        sq.res.next();
                        String toad=sq.res.getString("address");
                        sq.res=sq.st.executeQuery("select address from qualitychecker where name='"+ust+"'");
                        sq.res.next();
                        String frad=sq.res.getString("address");
                        sq.st.executeUpdate("insert into "+na1+"transportinbox (`from`,info) values ('"+ust+"qualitychecker','"+frad+"\n"+toad+"')");
                        int woolind=((((Integer.parseInt(twt.getText())-3)*2)-1)+(((Integer.parseInt(qclt.getText())-11)*2)-1)+Math.abs(5-nam.getSelectedIndex())+Math.abs(7-namt.getSelectedIndex())+Math.abs(11-btt.getSelectedIndex()));
                        int validprice=(int)Math.ceil((800-(800/(800%woolind)))*4.5*Integer.parseInt(infodata[selrow][0]));//<==
                        String infotxt="staple length:"+twt.getText()+"\\nstaple diameter:"+qclt.getText()+"\\nStaple type:"+(String)nam.getSelectedItem()+"\\nStaple avg colour:"+(String)namt.getSelectedItem()+"\\nStaple breed type:"+(String)btt.getSelectedItem()+"\\nValid Price:"+Integer.toString(validprice)+"\\namt:"+txt.getText();
                        sq.st.executeUpdate("insert into "+prname+"inbox (`from`,info) values ('"+ust+"qualitychecker','"+infotxt+"')");
                        new qcproc(ust);
                        f.dispose();
                        /**woolqualityindex=(((int)woollength-3)*2)-1;   4-7.5
                        woolqualityindex+=(((int)wooldiameter-11)*2)-1;   12-22
                        woolqualityindex+=Math.abs(5-woolquality)+Math.abs(7-woolcolor)+Math.abs(11-woolbreed);
                        validprice=woolprice+(woolprice/(woolprice%woolqualityindex));//*weight*tot sheep*/
                    }
                }
                catch(Exception ex){ex.printStackTrace();}
            }
        });

        qcp.add(tw);
        qcp.add(twt);
        qcp.add(qcl);
        qcp.add(qclt);
        qcp.add(qt);
        qcp.add(nam);
        qcp.add(namt);
        qcp.add(tl);
        qcp.add(bt);
        qcp.add(btt);

        f.add(inbp);
        f.add(qcp);
        f.add(bg);
        f.add(post);
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
            sq.res=sq.st.executeQuery("select * from "+ust+"qualitycheckerinbox");
            ResultSetMetaData md=sq.res.getMetaData();
            int totcol = md.getColumnCount();
            totrow = getRowCount(sq.res);
            colname = new String[totcol];
            data = new Object[totrow][totcol];
            infodata =new String[totrow][];
            for (int i = 1; i < totcol; i++) colname[i-1] = md.getColumnName(i);
            colname[totcol-1]="Total sheeps";
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
