package project;

import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class buproc {
    Image bg;
    JFrame f=new JFrame();
    bgimg b=new bgimg();
    JPanel inbp=new JPanel();
    JTable table;
    JScrollPane sp;
    JButton ba=new JButton("Back");
    JLabel wal;
    int totrow,selrow;
    String prname,user;
    String[] colname,det;
    Object[][] data;
    String[][] infodata;
    public buproc(String ust) throws Exception{
        JLabel bg=b.bg("C:/Users/Admin/Downloads/buyer.jpg");
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

        ba.setBounds(900,550,100,30);
        f.add(ba);
        ba.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                inbp.remove(sp);
                inbp.revalidate();
                inbp.repaint();
                table=null;
                fetchuser(ust);
            }
        });
        inbp.setBounds(300,80,700,450);
        inbp.setVisible(false);

        wal=new JLabel("",JLabel.CENTER);
        wal.setBounds(20,0,100,30);
        wal.setOpaque(true);
        BufferedImage img=ImageIO.read(new File("C:/Users/Admin/Downloads/goldcoin.png"));
        Image im = img.getScaledInstance(30,30, Image.SCALE_SMOOTH);
        wal.setIcon(new ImageIcon(im));
        sqlconnection sq=new sqlconnection();
        sq.st.executeUpdate("create table if not exists "+ust+"buyerwall (id int primary key auto_increment,`from` char(30),dept char(20),`to` char(30),amt int)");
        sq.res=sq.st.executeQuery("select count(*) from "+ust+"buyerwall");
        sq.res.next();
        if(sq.res.getInt("count(*)")==0) wal.setText("0");
        else{
            sq.res=sq.st.executeQuery("select amt from "+ust+"buyerwall where `from` is NULL and dept is NULL and `to` is NULL order by id desc limit 1");
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
                        sq.st.executeUpdate("insert into "+ust+"buyerwall(amt)values("+amt+")");
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
                            sq.st.executeUpdate("insert into "+ust+"buyerwall(amt)values("+amt+")");
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
        fetchuser(ust);
        f.add(inbp);
        f.add(bg);
        f.add(wal);
        f.setLayout(null);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    private void fetchuser(String ust){
        ba.setVisible(false);
        try{
            sqlconnection sq=new sqlconnection();
            sq.st=sq.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sq.res=sq.st.executeQuery("select * from producer");
            ResultSetMetaData md=sq.res.getMetaData();
            int totcol = md.getColumnCount();
            totrow = getRowCount(sq.res);
            colname = new String[totcol-2];
            data = new Object[totrow][totcol-2];
            int passwdval=0,x=0;
            for (int i = 2; i <= totcol; i++){
                if(!"passwd".equals(md.getColumnName(i))){
                    colname[x] = md.getColumnName(i);
                    x+=1;
                }
                else passwdval=i;
            }
            int k=0;
            while(sq.res.next()){
                x=0;
                for (int i = 2; i <= totcol; i++){
                    if(i!=passwdval){
                        data[k][x] = sq.res.getObject(i);
                        x+=1;
                    }
                }
                k+=1;
            }
            addtabuser(ust);
        }
        catch(Exception ex){ex.printStackTrace();}
    }
    private void addtabuser(String ust){
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
        if(table!=null){
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e){
                    selrow=table.getSelectedRow();
                    if(selrow!=-1){
                        user=(String)table.getValueAt(selrow, 0);
                        inbp.remove(sp);
                        inbp.revalidate();
                        inbp.repaint();
                        table=null;
                        fetch(ust);
                    }
                }
            });
        }
    }
    private void fetch(String ust){
        ba.setVisible(true);
        try{
            sqlconnection sq=new sqlconnection();
            sq.st=sq.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sq.res=sq.st.executeQuery("select * from "+user+"producerinbox");
            ResultSetMetaData md=sq.res.getMetaData();
            int totcol = md.getColumnCount()-2;
            totrow = getRowCount(sq.res);
            colname = new String[totcol];
            data = new Object[totrow][totcol];
            infodata =new String[totrow][];
            det=new String[totrow];
            colname[0]="Name";
            colname[1]="Amount";
            int k=0;
            while(sq.res.next()){
                if(((String)sq.res.getObject(2)).contains("qualitychecker")){
                    data[k][0]=(((String)sq.res.getObject(2)).split("qualitychecker"))[0];
                    String a[]=((String)sq.res.getObject(3)).split("Valid Price:");
                    data[k][1]=a[1];
                    det[k]=a[0];
                    k+=1;
                }
            }
            addtab(ust);
        }
        catch(Exception ex){ex.printStackTrace();}
     }
     private int getRowCount(ResultSet rs) throws SQLException {
        int rowCount = 0;
        while (rs.next())rowCount++;
        rs.beforeFirst();
        return rowCount;
    }
    private void addtab(String ust){
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
        if(table!=null){
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e){
                    selrow=table.getSelectedRow();
                    if(selrow!=-1){
                        inbp.setVisible(false);
                        JPanel pay=new JPanel();
                        JLabel amt=new JLabel("Amount to pay:"+((String)table.getValueAt(selrow, 1)));
                        amt.setFont(new Font("Arial", Font.BOLD, 15));
                        JLabel reml=new JLabel("<html>Details:<br>"+det[selrow].replace("\n", "<br>") + "</html>");
                        reml.setFont(new Font("Arial", Font.PLAIN, 12));
                        pay.add(amt);
                        pay.add(reml);
                        int chk=JOptionPane.showOptionDialog(f,pay,"INFO",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Buy", "Cancel"},"Buy");
                        if(chk==JOptionPane.OK_OPTION){
                            try{
                                sqlconnection sq=new sqlconnection();
                                if((Integer.parseInt(((amt.getText()).split(":"))[1]))<=(Integer.parseInt(wal.getText()))){
                                    sq.st.executeUpdate("delete from "+user+"producerinbox where info like '%"+((String)table.getValueAt(selrow, 1))+"%'");
                                    sq.res=sq.st.executeQuery("select amt from "+user+"producerwall where `from` is NULL and dept is NULL and `to` is NULL order by id desc limit 1");
                                    sq.res.next();
                                    int wallamt=sq.res.getInt("amt");
                                    sq.st.executeUpdate("insert into "+user+"producerwall(dept,`from`,amt) values('Buyer','"+ust+"','"+(wallamt-(Integer.parseInt(((amt.getText()).split(":"))[1])))+"')");
                                }
                                else JOptionPane.showMessageDialog(null,"Insufficient Balance","NOTE",JOptionPane.INFORMATION_MESSAGE);
                            }
                            catch(Exception ex){ex.printStackTrace();}
                            inbp.remove(sp);
                            inbp.revalidate();
                            inbp.repaint();
                            table=null;
                            fetch(ust);
                        }
                        else inbp.setVisible(true);
                    }
                }
            });    
        }
        else JOptionPane.showMessageDialog(null,"No Wool On Demand","NOTE",JOptionPane.INFORMATION_MESSAGE);
    }
}
