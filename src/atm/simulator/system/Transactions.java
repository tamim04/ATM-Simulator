package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Transactions extends JFrame implements ActionListener {

    JLabel text;
    JButton deposit, withdrawl, fastcash, pinchange, balanceenquiry, exit;
    String cardnumber;

    Transactions(String cardnumber) {
        this.cardnumber = cardnumber;
        JPanel card = Theme.screen(this, "Main Menu", 400);
        text = Theme.label("Please select your transaction", Font.BOLD, 22, Theme.TEXT);
        text.setBounds(30, 24, 640, 32);
        card.add(text);
        JLabel hint = Theme.label("Choose one of the services below", Font.PLAIN, 14, Theme.MUTED);
        hint.setBounds(30, 56, 640, 22);
        card.add(hint);

        deposit = Theme.tile("Deposit", "savings", Theme.ACCENT);
        withdrawl = Theme.tile("Cash Withdrawal", "payments", Theme.PRIMARY);
        fastcash = Theme.tile("Fast Cash", "flash_on", Theme.WARNING);
        balanceenquiry = Theme.tile("Balance Enquiry", "account_balance_wallet", Theme.PRIMARY);
        pinchange = Theme.tile("PIN Change", "password", Theme.PRIMARY);
        exit = Theme.tile("Exit", "logout", Theme.DANGER);

        JButton[] tiles = {deposit, withdrawl, fastcash, balanceenquiry, pinchange, exit};
        for (int i = 0; i < tiles.length; i++) {
            tiles[i].setBounds(30 + (i % 3) * 220, 100 + (i / 3) * 140, 200, 120);
            tiles[i].addActionListener(this);
            card.add(tiles[i]);
        }
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == exit) {
            System.exit(0);
        } else if (ae.getSource() == deposit) {
            setVisible(false);
            new Deposit(cardnumber).setVisible(true);
        } else if (ae.getSource() == withdrawl) {
            setVisible(false);
            new Withdrawl(cardnumber).setVisible(true);
        } else if (ae.getSource() == fastcash) {
            setVisible(false);
            new FastCash(cardnumber).setVisible(true);
        } else if (ae.getSource() == pinchange) {
            setVisible(false);
            new PinChange(cardnumber).setVisible(true);
        } else if (ae.getSource() == balanceenquiry) {
            setVisible(false);
            new BalanceEnquiry(cardnumber).setVisible(true);
        }
    }

    public static void main(String args[]) {
        new Transactions("");
    }
}
