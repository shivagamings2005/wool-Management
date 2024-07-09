package project;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.*;

public class admin {
    int val=10;
    String user="";
    Image bg;
    JTable table;
    JFrame f=new JFrame();
    bgimg b=new bgimg();
    JPanel p=new JPanel();
    JScrollPane sp;
    String[] colname;
    Object[][] data;
    int totrow;
    Connection con;
    Statement st;
    ResultSet rs;
    public admin() throws SQLException{
        JLabel bg=b.bg("C:\\Users\\shiva\\Downloads\\wool background\\quality.jpg");
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
        
        JPanel jp=new JPanel();
        jp.setBounds(10,100,250, 350);
        jp.setLayout(null);

        Timer t2 = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(val>=10) ((Timer) e.getSource()).stop();
                else{
                    jp.setBounds(val,100,250, 350);
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
        
        
        JButton btin = new JButton ("Select User");
        btin.setBounds (10, 50, 150, 30);
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
                if(e.getSource()==btin) min.show(btin, 0,25);
            }
            public void mouseClicked(MouseEvent e){
                btin.setText("Select user");
                if(e.getSource()==btin) min.show(btin, 0,25);
            }
        });
        jp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e){   
                min.setVisible(false);
            }  
        });
        JLabel us=new JLabel("Username",JLabel.CENTER);
        us.setBounds(10,150,100,30);
        us.setOpaque(true);
        us.setBackground(Color.WHITE);
        us.setForeground(Color.decode("#19e668"));
        
        JTextField ust=new JTextField();
        ust.setBounds(120,150,100,30);
        
        Timer t = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(val<=-190) ((Timer) e.getSource()).stop();
                else{
                    jp.setBounds(val,100,250, 350);
                    val-=3;
                }
            }
        });
        
        JButton srh = new JButton ("Fetch data");
        srh.setBounds (10, 200, 150, 30);
        
        p.setBounds(300,80,700,450);
        p.setVisible(true);
        
        min.add(inprod);
        min.add(inproc);
        min.add(inst);
        min.add(inqu);
        min.add(intr);
        min.add(inbu);
        jp.add(btin);
        jp.add(us);
        jp.add(ust);
        jp.add(srh);
        f.add(jp);
        f.add(p);
        
        con=DriverManager.getConnection("jdbc:mysql://localhost:3306/wool","root","SSS!A!55");
        st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        srh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ex){
                try{
                    if("".equals(ust.getText())) rs = st.executeQuery("select * from "+user);
                    else rs = st.executeQuery("select * from "+ust.getText()+user+"wall where `from` is not NULL or dept is not NULL or `to` is not NULL;");
                    fetch(ust.getText());
                    table.addMouseListener(new MouseAdapter() {
                        public void mouseEntered(MouseEvent e){
                            t.start();
                        }
                    });
                }
                catch(Exception e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,"Username doesn't exists","NOTE",JOptionPane.INFORMATION_MESSAGE);
                    ust.setText("");
                }
            }
        });
        
        f.add(bg);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    private void fetch(String ust) throws SQLException{
        ResultSetMetaData md = rs.getMetaData();
        int totcol = md.getColumnCount();
        totrow = getRowCount(rs);
        
        if("".equals(ust)){
            colname = new String[totcol-1];
            data = new Object[totrow][totcol-1];
        }
        else{
            colname = new String[totcol];
            data = new Object[totrow][totcol];
        }
        int passwdval=0;
        int x=0;
        for (int i = 1; i <= totcol; i++) {
            if(!"passwd".equals(md.getColumnName(i))){
                colname[x] = md.getColumnName(i);
                x+=1;
            }
            else{ passwdval=i;}
        }
        int k=0;
        while (rs.next()) {
            x=0;
            for (int j = 1; j <= totcol;j++) {
                if(j!=passwdval){
                    if(rs.getObject(j)==null) data[k][x]=(ust.split(user))[0];
                    else data[k][x] = rs.getObject(j);
                    x+=1;
                }
            }
            k+=1;
        }
        addtab();
    }
    private void addtab(){
        if(table!=null){
            p.remove(sp);
            p.revalidate();
            p.repaint();
            table=null;
        }
        table = new JTable(data, colname);
        table.setRowHeight(25);
        int height=(totrow*25)+30;
        if(height<450) table.setPreferredSize(new Dimension(700,450));
        else table.setPreferredSize(new Dimension(700,height));
        sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(700,450));
        p.add(sp, BorderLayout.CENTER);
        
        f.add(p);
        p.setVisible(true);
        f.setVisible(true);
    }
    private int getRowCount(ResultSet rs) throws SQLException {
        int rowCount = 0;
        while (rs.next()) {
            rowCount++;
        }
        rs.beforeFirst();
        return rowCount;
    }
}