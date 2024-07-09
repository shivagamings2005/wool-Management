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

public class soproc {
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
    public soproc(String ust) throws Exception{
        JLabel bg=b.bg("C:/Users/Admin/Downloads/storage.jpeg");
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
        sq.st.executeUpdate("create table if not exists "+ust+"storageownerwall (id int primary key auto_increment,`from` char(30),dept char(20),`to` char(30),amt int)");
        sq.res=sq.st.executeQuery("select count(*) from "+ust+"storageownerwall");
        sq.res.next();
        if(sq.res.getInt("count(*)")==0) wal.setText("0");
        else{
            sq.res=sq.st.executeQuery("select amt from "+ust+"storageownerwall where `from` is NULL and dept is NULL and `to` is NULL order by id desc limit 1");
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
                        sqlconnection sq=new sqlconnection();
                        int amt=Integer.parseInt(wal.getText())+Integer.parseInt(txt.getText());
                        sq.st.executeUpdate("insert into "+ust+"storageownerwall(amt)values("+amt+")");
                        wal.setText(Integer.toString(amt));
                        sq.res.close();
                        sq.st.close();
                        sq.con.close();
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
                            sq.st.executeUpdate("insert into "+ust+"storageownerwall(amt)values("+amt+")");
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
        sq.st.executeUpdate("create table if not exists "+ust+"storageownerinbox (id int primary key auto_increment,`from` char(30),info char(100),visit char(3) default 'no')");
        sq.res=sq.st.executeQuery("select count(*) from "+ust+"storageownerinbox where visit='no'");
        sq.res.next();
        inb.setText("Inbox:"+sq.res.getInt("count(*)"));
        
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
                                            int id=(int)table.getValueAt(selrow, 0);
                                            prname = (String) table.getValueAt(selrow, 1);
                                            String prodname=prname.split("producer")[0];
                                            String msg="Storage request from "+prodname;
                                            JOptionPane.showMessageDialog(null,msg,"NOTE",JOptionPane.INFORMATION_MESSAGE);
                                            JPanel postp=new JPanel();
                                            JLabel reqamt=new JLabel("Enter charge:");
                                            JTextField txt = new JTextField(10);
                                            postp.add(reqamt);
                                            postp.add(txt);
                                            JOptionPane.showMessageDialog(null,postp,"NOTE",JOptionPane.PLAIN_MESSAGE);
                                            String infotxt="storage Amount:"+txt.getText()+"\\namt:"+txt.getText();
                                            try{
                                                sq.st.executeUpdate("insert into "+prname+"inbox (`from`,info) values ('"+ust+"storageowner','"+infotxt+"')");
                                                sq.res=sq.st.executeQuery("select address from producer where name='"+prodname+"'");
                                                sq.res.next();
                                                String frad=sq.res.getString("address");
                                                sq.res=sq.st.executeQuery("select address from storageowner where name='"+ust+"'");
                                                sq.res.next();
                                                String toad=sq.res.getString("address");
                                                sq.st.executeUpdate("insert into "+infodata[selrow][1]+"transportinbox (`from`,info) values ('"+prname+"','"+frad+"\n"+toad+"')");//info from adr,to adr
                                                sq.st.executeUpdate("update "+ust+"storageownerinbox set visit='yes' where id="+id);
                                                sq.res=sq.st.executeQuery("select count(*) from "+ust+"storageownerinbox where visit='no'");
                                                sq.res.next();
                                                inb.setText("Inbox:"+sq.res.getInt("count(*)"));
                                                inbp.remove(sp);
                                                inbp.revalidate();
                                                inbp.repaint();
                                                table=null;
                                                MouseEvent event = new MouseEvent(inb,MouseEvent.MOUSE_CLICKED,System.currentTimeMillis(),0,5, 5,1, false);
                                                inb.dispatchEvent(event);
                                            }
                                            catch(Exception ex){ex.printStackTrace();}
                                        }
                                    }
                                });
                            }
                        else{
                            inbp.setVisible(false);
                            JOptionPane.showMessageDialog(null,"All Messages are read","NOTE",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    else JOptionPane.showMessageDialog(null,"No Messages","NOTE",JOptionPane.INFORMATION_MESSAGE);
                }
                catch(Exception ex){ex.printStackTrace();}
            }
        });
        
        f.add(inb);
        f.add(inbp);
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
            sq.res=sq.st.executeQuery("select * from "+ust+"storageownerinbox where visit='no'");
            ResultSetMetaData md=sq.res.getMetaData();
            int totcol = md.getColumnCount()-1;
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
