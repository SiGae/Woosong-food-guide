import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

/**
 * Created by sigae on 5/12/16.
 */
public class Main extends JFrame {
    private JTextField storeName;
    private JTextField openTime;
    private JTextField closeTime;
    private JTextField phone;
    private JButton clearButton;
    private JButton istbt;
    private DefaultTableModel model;
    public JPanel moneyView;
    private JTable table1;
    private JButton DelBt;

    public Main() {

        String[] header = new String[]{"식당 이름", "오픈 시간", "마감 시간", "전화번호" };
        String[][] data = null;

        storeName.addFocusListener(new changeListener());
        openTime.addFocusListener(new changeListener());
        closeTime.addFocusListener(new changeListener());
        phone.addFocusListener(new changeListener());

        clearButton.addActionListener(e -> {
            JTextField arr[] = {storeName, openTime, closeTime, phone};
            for (int i=0;i<6;i++)
                arr[i].setText("");});

        istbt.addActionListener(e -> {
            try {
                Connection con = dbconnect.makeConnection();

                String sq = "INSERT INTO woosong.storeinfo(name, opentime, closetime, phone) VALUES";
                sq+="('" + GetStoreName() + "','"+GetOpentime()+ "','"+ GetClosetime()+ "','" + GetPhoneFiled() +"')";

                PreparedStatement stmt = con.prepareStatement(sq);
                int i =stmt.executeUpdate();
                if (i==1) {
                    System.out.println("성공!");
                    select();
                }
                else {
                    System.out.println("ㅠㅠ");
                }
            }
            catch (SQLException e3) {
                e3.printStackTrace();
            }

        });

        model = new CustomTableModel(data, header);
        table1.setModel(model);

        table1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                DelBt.addActionListener(e1->{
                    JTable target = (JTable) e.getSource();
                    delete_item(((String) model.getValueAt(target.getSelectedRow(), 0)));
                });

            }
        });
        select();

    }


    private String GetStoreName() {
        return storeName.getText();
    }

    private String GetOpentime() {
        return openTime.getText();
    }

    private String GetClosetime() {
        return closeTime.getText();
    }

    private String GetPhoneFiled() {
        return phone.getText();
    }


    private class CustomTableModel extends DefaultTableModel {
        public CustomTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }


    private class changeListener extends FocusAdapter {
        @Override
        public void focusLost(FocusEvent e) {
            super.focusLost(e);
        }
    }

    private void select() {
        String sql = "SELECT  name, opentime, closetime, phone FROM woosong.storeinfo ORDER BY name ;";
        ResultSet rs = null;
        PreparedStatement stmt = null;


        Connection con = dbconnect.makeConnection();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                Vector<String> cont = new Vector<>();
                cont.add(rs.getString("name"));
                cont.add(rs.getString("opentime"));
                cont.add(rs.getString("closeTime"));
                cont.add(rs.getString("phone"));
                model.addRow(cont);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert rs != null;
                
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void delete_item(String deStoreName) {
        String sql = "DELETE FROM woosong.storeinfo WHERE name=" + deStoreName;
        int result;
        PreparedStatement stmt = null;


        Connection con = dbconnect.makeConnection();
        try {
            stmt = con.prepareStatement(sql);
            result = stmt.executeUpdate();

            System.out.println("Success: " + result);
            select();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
