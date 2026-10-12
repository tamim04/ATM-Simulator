package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {

    JLabel text, cardNo, pin;
    JButton login, signup, clear;
    JTextField cardText;
    JPasswordField pinText;

    Login() {
        Theme.setupFrame(this, "Automated Teller Machine", 860, 500);

        // Left: branding panel
        JPanel brand = new Theme.GradientPanel(Theme.PRIMARY, Theme.PRIMARY_DARK, true);
        brand.setLayout(null);
        brand.setBounds(0, 0, 360, 500);
        add(brand);

        JLabel logo = new JLabel(Theme.icon("local_atm", 88, Color.WHITE));
        logo.setBounds(40, 60, 88, 88);
        brand.add(logo);

        text = Theme.label("Welcome to ATM", Font.BOLD, 32, Color.WHITE);
        text.setBounds(40, 160, 300, 44);
        brand.add(text);

        JLabel tagline = Theme.label("<html>Fast, simple and secure banking,<br>available whenever you need it.</html>", Font.PLAIN, 15, new Color(0xC7D6F5));
        tagline.setBounds(40, 205, 290, 48);
        brand.add(tagline);

        String[][] features = {{"verified_user", "Secure PIN protected access"}, {"flash_on", "Instant fast cash withdrawal"}, {"receipt_long", "Check your balance anytime"}};
        for (int i = 0; i < features.length; i++) {
            JLabel f = Theme.label(features[i][1], Font.PLAIN, 14, Color.WHITE);
            f.setIcon(Theme.icon(features[i][0], 20, new Color(0x7FE3B6)));
            f.setIconTextGap(12);
            f.setBounds(40, 300 + i * 40, 300, 26);
            brand.add(f);
        }

        // Right: sign in form
        JLabel title = Theme.label("Sign in", Font.BOLD, 28, Theme.TEXT);
        title.setBounds(430, 50, 360, 40);
        add(title);

        JLabel subtitle = Theme.label("Enter your card number and PIN to continue", Font.PLAIN, 14, Theme.MUTED);
        subtitle.setBounds(430, 88, 380, 22);
        add(subtitle);

        cardNo = Theme.fieldLabel("Card Number");
        cardNo.setIcon(Theme.icon("credit_card", 18, Theme.PRIMARY));
        cardNo.setIconTextGap(8);
        cardNo.setBounds(430, 135, 360, 24);
        add(cardNo);

        cardText = Theme.textField();
        cardText.setBounds(430, 163, 370, 42);
        add(cardText);

        pin = Theme.fieldLabel("PIN");
        pin.setIcon(Theme.icon("lock", 18, Theme.PRIMARY));
        pin.setIconTextGap(8);
        pin.setBounds(430, 220, 360, 24);
        add(pin);

        pinText = Theme.passwordField();
        pinText.setBounds(430, 248, 370, 42);
        pinText.addActionListener(this);
        add(pinText);

        login = Theme.button("SIGN IN", "login", Theme.PRIMARY);
        login.setBounds(430, 315, 225, 44);
        login.addActionListener(this);
        add(login);

        clear = Theme.outlineButton("CLEAR", "backspace", Theme.MUTED);
        clear.setBounds(665, 315, 135, 44);
        clear.addActionListener(this);
        add(clear);

        JLabel newHere = Theme.label("New to our bank?", Font.PLAIN, 14, Theme.MUTED);
        newHere.setBounds(430, 385, 200, 22);
        add(newHere);

        signup = Theme.outlineButton("OPEN A NEW ACCOUNT", "person_add", Theme.PRIMARY);
        signup.setBounds(430, 412, 370, 44);
        signup.addActionListener(this);
        add(signup);

        getRootPane().setDefaultButton(login);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == clear) {
            cardText.setText("");
            pinText.setText("");
        } else if (ae.getSource() == login || ae.getSource() == pinText) {
            Conn conn = new Conn();
            String cardnumber = cardText.getText().trim();
            String pinnumber = new String(pinText.getPassword()).trim();

            try {
                ResultSet rs = conn.query("SELECT * FROM login WHERE cardnumber = ? AND pin = ?", cardnumber, pinnumber);
                if (rs.next()) {
                    setVisible(false);
                    new Transactions(cardnumber).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Card number or pin!!");

                }

            } catch (Exception e) {
                System.out.println(e);
            }

        } else if (ae.getSource() == signup) {
            setVisible(false);
            new SignupOne().setVisible(true);
        }
    }

    public static void main(String args[]) {
        new Login();
    }
}
