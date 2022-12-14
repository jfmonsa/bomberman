package com.carlosflorencio.bomberman.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

// Interfaz
import com.carlosflorencio.bomberman.Board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

public class BaseDatos implements ActionListener {
    Connection conexion = null;
    DefaultTableModel modelo;
    JTable tabla = null;

    // Interfaz
    JFrame dialog;
    JLabel l1, l2;
    JTextField t1;
    JButton btn_enviar;

    public BaseDatos() {
        // Conection with the DB
        try {
            conexion = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/bomberman",
                    "postgres", "123456789");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        gui();

    }

    public DefaultTableModel update_modelo() {
        modelo = new DefaultTableModel();
        try {
            Statement statement = conexion.createStatement();
            String consulta = "SELECT nick_name, score FROM public.top_scores ORDER BY score DESC;";
            ResultSet resultado = statement.executeQuery(consulta);

            modelo.addColumn("Nick name");
            modelo.addColumn("Score");

            while (resultado.next()) {
                String nick_name = resultado.getString(1);
                int score = resultado.getInt(2);
                Object[] fila = new Object[2];
                fila[0] = nick_name;
                fila[1] = score;
                modelo.addRow(fila);
            }
            // TODO
            // JOptionPane.showMessageDialog(null, barra);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return modelo;
    }

    public void insertar(String nick, int score) {
        try {
            Statement statement = conexion.createStatement();
            String consulta = "INSERT INTO public.top_scores(" +
                    "nick_name, score)" +
                    "VALUES ('" + nick + "', " + score + ");";
            statement.executeUpdate(consulta);

            tabla.setVisible(false);
            tabla.setModel(modelo);
            modelo.fireTableDataChanged();
            tabla.setVisible(true);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void gui() {
        final int win_width = 350 + 100 + 50;
        final int win_heigth = 310 - 30 + 100 + 100 + 100;
        final Dimension d = new Dimension((int) Math.floor(win_width * 0.9), 25);
        dialog = new JFrame("Puntajes historicos");
        dialog.setLayout(new FlowLayout());

        dialog.setSize(win_width, win_heigth);
        dialog.setBackground(Color.LIGHT_GRAY);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        l1 = new JLabel("                     Top puntajes              ");
        // l1.setFont(new Font("Segoe UI Black", 0, 24));
        l1.setFont(new Font("Space Mono", 0, 24));
        dialog.add(l1);

        l2 = new JLabel("Ingrese su nickname");
        // l1.setFont(new Font("Segoe UI Black", 0, 24));
        l2.setFont(new Font("Segoe UI Black", 0, 16));
        dialog.add(l2);

        t1 = new JTextField();
        t1.setPreferredSize(d);
        dialog.add(t1);

        btn_enviar = new JButton("Enviar");
        btn_enviar.setPreferredSize(d);
        btn_enviar.addActionListener(this);
        dialog.add(btn_enviar);

        // --------------- tabla puntajes ---------------
        tabla = new JTable(update_modelo());
        // tabla.setPreferredSize(new Dimension((int) d.getWidth(), (int) d.getHeight()
        // * 5));
        tabla.setEnabled(false);
        // tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // scroll
        JScrollPane js = new JScrollPane(tabla);
        // js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        js.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dialog.add(js);

        // dialog.add(tabla);

        // js.setVisible(true);
        // dialog.add(js);

        // Siempre al final xd
        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn_enviar) {
            insertar(t1.getText(), Board.getPoints()); // inserta :D
            System.out.println(t1.getText() + "," + Board.getPoints());

            // Actualizar tabla
            tabla.setVisible(false);
            // modelo = null;
            tabla.setModel(update_modelo());
            modelo.fireTableDataChanged();

            tabla.setVisible(true);
        }

    }
}