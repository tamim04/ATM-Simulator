 
package atm.simulator.system;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Deposit extends JFrame implements ActionListener{
    
    JTextField t1,t2;
    JButton b1,b2,b3;
    JLabel l1,l2,l3;
    String cardnumber;
    Deposit(String cardnumber){
        this.cardnumber = cardnumber;
        JPanel card = Theme.screen(this, "Deposit", 330);
        Theme.cardTitle(card, "Deposit money", "Enter the amount you want to deposit into your account");

        l1 = Theme.fieldLabel("Amount (Tk)");
        l1.setIcon(Theme.icon("attach_money", 18, Theme.PRIMARY));
        l1.setIconTextGap(6);
        l1.setBounds(30, 105, 300, 24);
        card.add(l1);

        t1 = Theme.textField();
        t1.setFont(Theme.font(Font.BOLD, 22));
        t1.setBounds(30, 135, 640, 52);
        card.add(t1);

        b1 = Theme.button("DEPOSIT", "savings", Theme.ACCENT);
        b1.setBounds(490, 230, 180, 46);
        card.add(b1);

        b2 = Theme.outlineButton("BACK", "arrow_back", Theme.PRIMARY);
        b2.setBounds(30, 230, 150, 46);
        card.add(b2);

        b1.addActionListener(this);
        b2.addActionListener(this);
        getRootPane().setDefaultButton(b1);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        try{        
            String amount = t1.getText().trim();
            Date date = new Date();
            if(ae.getSource()==b1){
                if(amount.equals("")){
                    JOptionPane.showMessageDialog(null, "Please enter the Amount to you want to Deposit");
                }else if(Conn.parseAmount(amount) < 0){
                    JOptionPane.showMessageDialog(null, "Please enter a valid amount");
                }else{
                    amount = String.valueOf(Conn.parseAmount(amount));
                    Conn c1 = new Conn();
                    c1.update("INSERT INTO bank VALUES(?, ?, 'Deposit', ?)", cardnumber, date.toString(), amount);
                    JOptionPane.showMessageDialog(null, "Tk. "+amount+" Deposited Successfully");
                    setVisible(false);
                    new Transactions(cardnumber).setVisible(true);
                }
            }else if(ae.getSource()==b2){
                setVisible(false);
                new Transactions(cardnumber).setVisible(true);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
            
    }
    
    public static void main(String[] args){
        new Deposit("");
    }
}