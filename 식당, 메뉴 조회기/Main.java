import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

/**
 * Created by sigae on 5/12/16.
 */
public class Main extends JFrame {
    private JButton istbt;
    private DefaultTableModel model;
    public JPanel moneyView;
    private JTable table1;

    public Main() {

        String[] header = new String[]{"id","음식 이름","가격", "분야","식당 이름", "오픈 시간", "마감 시간", "전화번호" };
        String[][] data = null;




        istbt.addActionListener(e -> {
                    select();


        });

        model = new CustomTableModel(data, header);
        table1.setModel(model);

        table1.addMouseListener(new MouseAdapter() {

        });
        select();

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




    private void select() {
        String sql = "select id, menu, price, field, storename, opentime, closetime, phone from woosong.woosongfood join woosong.storeinfo on woosong.woosongfood.storename = woosong.storeinfo.name order by id;;";
        ResultSet rs = null;
        PreparedStatement stmt = null;


        Connection con = dbconnect.makeConnection();
        try {
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                Vector<String> cont = new Vector<>();
                cont.add(rs.getString("id"));
                cont.add(rs.getString("menu"));
                cont.add(rs.getString("price"));
                cont.add(rs.getString("field"));
                cont.add(rs.getString("storename"));
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



}
