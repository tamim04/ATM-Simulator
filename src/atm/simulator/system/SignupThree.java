package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SignupThree extends JFrame implements ActionListener {

    JLabel type, card, carddetails, number, pin, pindetails, pnumber, services;
    JRadioButton r1, r2, r3, r4;
    JCheckBox c1, c2, c3, c4, c5, c6, c7;
    JButton submit, cancel;
    ButtonGroup groupaccount;
    String formno;

    SignupThree(String formno) {
        this.formno = formno;
        JPanel form = Theme.signupScreen(this, "Application Form No. " + formno, 3, "Account Details", 540);

        type = Theme.label("Account Type", Font.BOLD, 17, Theme.TEXT);
        type.setIcon(Theme.icon("account_balance", 20, Theme.PRIMARY));
        type.setIconTextGap(8);
        type.setBounds(40, 25, 400, 28);
        form.add(type);

        r1 = new JRadioButton("Saving Account");
        r2 = new JRadioButton("Fixed Deposit Account");
        r3 = new JRadioButton("Current Account");
        r4 = new JRadioButton("Recurring Deposit Account");
        groupaccount = new ButtonGroup();
        JRadioButton[] types = {r1, r2, r3, r4};
        for (int i = 0; i < types.length; i++) {
            Theme.styleToggle(types[i]);
            types[i].setBounds(40 + (i % 2) * 340, 60 + (i / 2) * 34, 320, 30);
            groupaccount.add(types[i]);
            form.add(types[i]);
        }

        // Card number and PIN are generated after submission
        JPanel info = new JPanel(null);
        info.setBackground(Theme.PRIMARY_LIGHT);
        info.setBounds(40, 145, 690, 110);
        form.add(info);

        JLabel cardIcon = new JLabel(Theme.icon("credit_card", 26, Theme.PRIMARY));
        cardIcon.setBounds(20, 14, 26, 26);
        info.add(cardIcon);

        card = Theme.label("Card Number", Font.BOLD, 15, Theme.TEXT);
        card.setBounds(58, 10, 250, 20);
        info.add(card);

        carddetails = Theme.label("Your 16 digit card number", Font.PLAIN, 12, Theme.MUTED);
        carddetails.setBounds(58, 30, 250, 18);
        info.add(carddetails);

        number = Theme.label("XXXX-XXXX-XXXX-1234", Font.BOLD, 18, Theme.PRIMARY);
        number.setBounds(360, 14, 320, 28);
        info.add(number);

        JLabel pinIcon = new JLabel(Theme.icon("password", 26, Theme.PRIMARY));
        pinIcon.setBounds(20, 66, 26, 26);
        info.add(pinIcon);

        pin = Theme.label("PIN", Font.BOLD, 15, Theme.TEXT);
        pin.setBounds(58, 62, 250, 20);
        info.add(pin);

        pindetails = Theme.label("Your 4 digit password", Font.PLAIN, 12, Theme.MUTED);
        pindetails.setBounds(58, 82, 250, 18);
        info.add(pindetails);

        pnumber = Theme.label("XXXX", Font.BOLD, 18, Theme.PRIMARY);
        pnumber.setBounds(360, 66, 320, 28);
        info.add(pnumber);

        services = Theme.label("Services Required", Font.BOLD, 17, Theme.TEXT);
        services.setIcon(Theme.icon("check_circle", 20, Theme.PRIMARY));
        services.setIconTextGap(8);
        services.setBounds(40, 275, 400, 28);
        form.add(services);

        c1 = new JCheckBox("ATM Card");
        c2 = new JCheckBox("Internet Banking");
        c3 = new JCheckBox("Mobile Banking");
        c4 = new JCheckBox("Email & SMS Alerts");
        c5 = new JCheckBox("Cheque Book");
        c6 = new JCheckBox("E-Statement");
        JCheckBox[] boxes = {c1, c2, c3, c4, c5, c6};
        for (int i = 0; i < boxes.length; i++) {
            Theme.styleToggle(boxes[i]);
            boxes[i].setBounds(40 + (i % 3) * 230, 312 + (i / 3) * 34, 220, 30);
            form.add(boxes[i]);
        }

        c7 = new JCheckBox("I hereby declare that the above entered details are correct");
        Theme.styleToggle(c7);
        c7.setFont(Theme.font(Font.BOLD, 14));
        c7.setBounds(40, 410, 690, 34);
        form.add(c7);

        cancel = Theme.outlineButton("CANCEL", "cancel", Theme.DANGER);
        cancel.setBounds(40, 465, 150, 44);
        cancel.addActionListener(this);
        form.add(cancel);

        submit = Theme.button("SUBMIT", "check_circle", Theme.ACCENT);
        submit.setBounds(580, 465, 150, 44);
        submit.addActionListener(this);
        form.add(submit);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            String accounType = null;
            if (r1.isSelected()) {
                accounType = "Saving Account";
            } else if (r2.isSelected()) {
                accounType = "Fixed Deposit Account";
            } else if (r3.isSelected()) {
                accounType = "Current Account";
            } else if (r4.isSelected()) {
                accounType = "Recurring Deposit Account";
            }
            Random random = new Random();
            String pinnumber = "" + Math.abs((random.nextLong() % 9000L) + 1000L);
            // Collect every selected service, not just the first one
            StringBuilder facility = new StringBuilder();
            for (JCheckBox box : new JCheckBox[]{c1, c2, c3, c4, c5, c6}) {
                if (box.isSelected()) {
                    if (facility.length() > 0) {
                        facility.append(", ");
                    }
                    facility.append(box.getText());
                }
            }
            try {
                if (accounType == null) {
                    JOptionPane.showMessageDialog(null, "Account type is required");
                } else if (!c7.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Please confirm that the entered details are correct");
                } else {
                    Conn c1 = new Conn();
                    // Card number identifies the account, so make sure it isn't already taken
                    String cardnumber;
                    do {
                        cardnumber = "" + Math.abs((random.nextLong() % 90000000L) + 5040936000000000L);
                    } while (c1.query("SELECT 1 FROM login WHERE cardnumber = ?", cardnumber).next());
                    c1.update("INSERT INTO signupThree VALUES(?, ?, ?, ?, ?)", formno, accounType, cardnumber, pinnumber, facility.toString());
                    c1.update("INSERT INTO login VALUES(?, ?, ?)", formno, cardnumber, pinnumber);
                    JOptionPane.showMessageDialog(null, "Card Number: " + cardnumber + "\n Pin:" + pinnumber);
                    setVisible(false);
                    new Deposit(cardnumber).setVisible(true);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (ae.getSource() == cancel) {
            setVisible(false);
            new Login().setVisible(true);
        }
    }

    public static void main(String args[]) {
        new SignupThree("");
    }
}
