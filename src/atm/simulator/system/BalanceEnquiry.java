package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BalanceEnquiry extends JFrame implements ActionListener {

    JButton back;
    JLabel text;
    String cardnumber;

    BalanceEnquiry(String cardnumber) {
        this.cardnumber = cardnumber;
        JPanel card = Theme.screen(this, "Balance Enquiry", 360);
        Theme.cardTitle(card, "Account balance", "Your available balance as of today");

        Conn c = new Conn();
        int balance = 0;
        try {
            balance = c.getBalance(cardnumber);
        } catch (Exception e) {
            System.out.println(e);
        }

        JPanel box = new Theme.GradientPanel(Theme.PRIMARY, Theme.PRIMARY_DARK, true);
        box.setLayout(null);
        box.setBounds(30, 100, 640, 150);
        card.add(box);

        JLabel boxIcon = new JLabel(Theme.icon("account_balance_wallet", 56, Color.WHITE));
        boxIcon.setBounds(30, 47, 56, 56);
        box.add(boxIcon);

        JLabel caption = Theme.label("CURRENT BALANCE", Font.BOLD, 13, new Color(0xC7D6F5));
        caption.setBounds(110, 40, 400, 20);
        box.add(caption);

        text = Theme.label("Tk. " + String.format("%,d", balance), Font.BOLD, 40, Color.WHITE);
        text.setBounds(108, 62, 500, 52);
        box.add(text);

        back = Theme.outlineButton("BACK", "arrow_back", Theme.PRIMARY);
        back.setBounds(30, 280, 150, 44);
        back.addActionListener(this);
        card.add(back);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        setVisible(false);
        new Transactions(cardnumber).setVisible(true);
    }

    public static void main(String args[]) {
        new BalanceEnquiry("");
    }
}
