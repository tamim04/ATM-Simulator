  package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
 import java.util.*;

public class Withdrawl extends JFrame implements ActionListener {

    JTextField amount;
    JButton withdraw, back;
    String cardnumber;
    
    Withdrawl(String cardnumber) {
        this.cardnumber = cardnumber;
        JPanel card = Theme.screen(this, "Cash Withdrawal", 330);
        Theme.cardTitle(card, "Withdraw cash", "Enter the amount you want to withdraw");

        JLabel text = Theme.fieldLabel("Amount (Tk)");
        text.setIcon(Theme.icon("attach_money", 18, Theme.PRIMARY));
        text.setIconTextGap(6);
        text.setBounds(30, 105, 300, 24);
        card.add(text);

        amount = Theme.textField();
        amount.setFont(Theme.font(Font.BOLD, 22));
        amount.setBounds(30, 135, 640, 52);
        card.add(amount);

        withdraw = Theme.button("WITHDRAW", "payments", Theme.PRIMARY);
        withdraw.setBounds(490, 230, 180, 46);
        withdraw.addActionListener(this);
        card.add(withdraw);

        back = Theme.outlineButton("BACK", "arrow_back", Theme.PRIMARY);
        back.setBounds(30, 230, 150, 46);
        back.addActionListener(this);
        card.add(back);

        getRootPane().setDefaultButton(withdraw);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == withdraw) {
            String number = amount.getText().trim();
            Date date = new Date();
            int value = Conn.parseAmount(number);
            if (number.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter the amount you want to withdraw");
            } else if (value < 0) {
                JOptionPane.showMessageDialog(null, "Please enter a valid amount");
            } else {
                try {
                    Conn conn = new Conn();
                    if (conn.getBalance(cardnumber) < value) {
                        JOptionPane.showMessageDialog(null, "Insufficient balance");
                        return;
                    }
                    conn.update("INSERT INTO bank VALUES(?, ?, 'Withdrawl', ?)", cardnumber, date.toString(), String.valueOf(value));
                    JOptionPane.showMessageDialog(null, "Tk " + value + " withdrawn successfully");
                    setVisible(false);
                    new Transactions(cardnumber).setVisible(true);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        } else if (ae.getSource() == back) {
            setVisible(false);
            new Transactions(cardnumber).setVisible(true);
        }
    }
    
    public static void main(String args[]) {
        new Withdrawl("");
    }
}
