package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PinChange extends JFrame implements ActionListener {

    JLabel pintext, repintext;
    JPasswordField pin, repin;
    JButton change, back;
    String cardnumber;
    PinChange(String cardnumber) {
        this.cardnumber = cardnumber;
        JPanel card = Theme.screen(this, "PIN Change", 400);
        Theme.cardTitle(card, "Change your PIN", "Choose a new 4 digit PIN and keep it secret");

        pintext = Theme.fieldLabel("New PIN");
        pintext.setIcon(Theme.icon("lock", 18, Theme.PRIMARY));
        pintext.setIconTextGap(6);
        pintext.setBounds(30, 100, 300, 24);
        card.add(pintext);

        pin = Theme.passwordField();
        pin.setBounds(30, 128, 640, 44);
        card.add(pin);

        repintext = Theme.fieldLabel("Re-enter New PIN");
        repintext.setIcon(Theme.icon("lock", 18, Theme.PRIMARY));
        repintext.setIconTextGap(6);
        repintext.setBounds(30, 190, 300, 24);
        card.add(repintext);

        repin = Theme.passwordField();
        repin.setBounds(30, 218, 640, 44);
        card.add(repin);

        change = Theme.button("CHANGE PIN", "check_circle", Theme.PRIMARY);
        change.setBounds(490, 300, 180, 46);
        change.addActionListener(this);
        card.add(change);

        back = Theme.outlineButton("BACK", "arrow_back", Theme.PRIMARY);
        back.setBounds(30, 300, 150, 46);
        back.addActionListener(this);
        card.add(back);

        getRootPane().setDefaultButton(change);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == change) {
            try {
                String npin = new String(pin.getPassword()).trim();
                String rpin = new String(repin.getPassword()).trim();
                if (npin.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter new pin");
                    return;
                }
                if (rpin.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please re-enter new pin");
                    return;
                }
                if (!npin.equals(rpin)) {
                    JOptionPane.showMessageDialog(null, "Entered pin does not match");
                    return;
                }
                if (!npin.matches("\\d{4}")) {
                    JOptionPane.showMessageDialog(null, "PIN must be exactly 4 digits");
                    return;
                }
                Conn c1 = new Conn();
                c1.update("UPDATE login SET pin = ? WHERE cardnumber = ?", rpin, cardnumber);
                c1.update("UPDATE signupThree SET pinnumber = ? WHERE cardnumber = ?", rpin, cardnumber);
                JOptionPane.showMessageDialog(null, "Pin Changed successfully ");
                setVisible(false);
            new Transactions(cardnumber).setVisible(true);

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            setVisible(false);
            new Transactions(cardnumber).setVisible(true);
        }
    }

    public static void main(String args[]) {
        new PinChange("").setVisible(true);
    }
}
