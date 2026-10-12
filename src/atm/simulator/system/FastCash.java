  package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class FastCash extends JFrame implements ActionListener {
    JLabel text;
    JButton deposit, withdrawl, fastcash, ministatement, pinchange, balanceenquiry, exit;
    String cardnumber;

    FastCash(String cardnumber) {
        this.cardnumber = cardnumber;
        JPanel card = Theme.screen(this, "Fast Cash", 420);
        text = Theme.label("Select withdrawal amount", Font.BOLD, 22, Theme.TEXT);
        text.setBounds(30, 24, 640, 32);
        card.add(text);
        JLabel hint = Theme.label("The selected amount is debited from your account instantly", Font.PLAIN, 14, Theme.MUTED);
        hint.setBounds(30, 56, 640, 22);
        card.add(hint);

        deposit = Theme.tile("Tk 100", "payments", Theme.ACCENT);
        withdrawl = Theme.tile("Tk 500", "payments", Theme.ACCENT);
        fastcash = Theme.tile("Tk 1000", "payments", Theme.ACCENT);
        ministatement = Theme.tile("Tk 2000", "payments", Theme.ACCENT);
        pinchange = Theme.tile("Tk 5000", "payments", Theme.ACCENT);
        balanceenquiry = Theme.tile("Tk 10000", "payments", Theme.ACCENT);

        JButton[] tiles = {deposit, withdrawl, fastcash, ministatement, pinchange, balanceenquiry};
        for (int i = 0; i < tiles.length; i++) {
            tiles[i].setFont(Theme.font(Font.BOLD, 18));
            tiles[i].setBounds(30 + (i % 3) * 220, 100 + (i / 3) * 110, 200, 94);
            tiles[i].addActionListener(this);
            card.add(tiles[i]);
        }

        exit = Theme.outlineButton("BACK", "arrow_back", Theme.PRIMARY);
        exit.setBounds(30, 334, 150, 44);
        exit.addActionListener(this);
        card.add(exit);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == exit) {
            setVisible(false);
            new Transactions(cardnumber).setVisible(true);
        } else {
            String amountText = ((JButton) ae.getSource()).getText().substring(3).trim(); // Get the amount from button text
            int amount = Integer.parseInt(amountText); // Convert the amount to an integer

            Conn c = new Conn();
            try {
                int balance = c.getBalance(cardnumber);

                if (balance < amount) {
                    JOptionPane.showMessageDialog(null, "Insufficient balance");
                } else {
                    Date date = new Date();
                    c.update("INSERT INTO bank VALUES(?, ?, 'Withdrawl', ?)", cardnumber, date.toString(), String.valueOf(amount));
                    JOptionPane.showMessageDialog(null, "Tk " + amount + " Debited successfully");
                    setVisible(false);
                    new Transactions(cardnumber).setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new FastCash("");
    }
}
