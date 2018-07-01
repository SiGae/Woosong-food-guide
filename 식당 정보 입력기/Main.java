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
    private JTextField foodName;
    private JTextField price;
    private JTextField foodFiled;
    private JButton clearButton;
    private JButton istbt;
    private DefaultTableModel model;
    public JPanel moneyView;
    private JTable table1;
    private JButton DelBt;

    public Main() {

        String[] header = new String[]{"id","음식 이름","가격","식당 이름", "오픈 시간", "마감 시간", "분야" };
        String[][] data = null;

        storeName.addFocusListener(new changeListener());
        openTime.addFocusListener(new changeListener());
        closeTime.addFocusListener(new changeListener());
        foodName.addFocusListener(new changeListener());
        price.addFocusListener(new changeListener());
        foodFiled.addFocusListener(new changeListener());

        clearButton.addActionListener(e -> {
            JTextField arr[] = {foodName, openTime, price, storeName, foodFiled, closeTime};
            for (int i=0;i<6;i++)
                arr[i].setText("");});

        istbt.addActionListener(e -> {
            try {
                Connection con = dbconnect.makeConnection();

                String sq = "INSERT INTO woosong.woosongfood(menu, price, name, opentime, closetime, field) VALUES";
                sq+="('" + GetFoodName() + "','" + GetPrice() + "','" + GetStoreName() + "','"+GetOpentime()+ "','"+ GetClosetime()+ "','" + GetFoodFiled() +"')";

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
                    delete_item(Integer.parseInt((String) model.getValueAt(target.getSelectedRow(), 0)));
                });

            }
        });
        select();

    }

    private String GetFoodName() {
        return foodName.getText();
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

    private String GetPrice() {
        return price.getText();
    }

    private String GetFoodFiled() {
        return foodFiled.getText();
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
        String sql = "SELECT  id, menu, price, name, opentime, closetime, field FROM woosong.woosongfood ORDER BY id;";
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
                cont.add(rs.getString("name"));
                cont.add(rs.getString("opentime"));
                cont.add(rs.getString("closeTime"));
                cont.add(rs.getString("field"));
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

    private void delete_item(int id) {
        String sql = "DELETE FROM woosong.woosongfood WHERE id=" + id;
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
