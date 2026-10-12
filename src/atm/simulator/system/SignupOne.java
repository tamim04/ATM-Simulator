package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import com.toedter.calendar.JDateChooser;
import java.awt.event.*;

public class SignupOne extends JFrame implements ActionListener {

    long random;
    JLabel name, fname, dob, gender, email, marital, address, city, district, pin;
    JTextField nameTextField, fnameTextField, emailTextField, addressTextField, stateTextField, cityTextField, pinTextField;
    JRadioButton male, female, other, married, unmarried;
    JButton next;
    ButtonGroup maritalGroup, genderGroup;
    JDateChooser dateChooser;

    SignupOne() {
        Random ran = new Random();
        random = Math.abs(ran.nextLong() % 9000L + 1000L);

        JPanel card = Theme.signupScreen(this, "Application Form No. " + random, 1, "Personal Details", 590);

        int y = 30, gap = 48;
        name = Theme.formLabel(card, "Name", "person", y);
        nameTextField = Theme.textField();
        nameTextField.setBounds(250, y, 480, 38);
        card.add(nameTextField);

        y += gap;
        fname = Theme.formLabel(card, "Father's Name", "person", y);
        fnameTextField = Theme.textField();
        fnameTextField.setBounds(250, y, 480, 38);
        card.add(fnameTextField);

        y += gap;
        dob = Theme.formLabel(card, "Date of Birth", "calendar_today", y);
        dateChooser = new JDateChooser();
        dateChooser.setOpaque(false);
        dateChooser.setBounds(250, y, 480, 38);
        dateChooser.setDateFormatString("dd-MM-yyyy");
        Theme.styleField((JTextField) dateChooser.getDateEditor().getUiComponent());
        JButton calendarButton = dateChooser.getCalendarButton();
        calendarButton.setIcon(Theme.icon("calendar_today", 20, Theme.PRIMARY));
        calendarButton.setBackground(Theme.CARD);
        calendarButton.setFocusPainted(false);
        calendarButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.add(dateChooser);

        y += gap;
        gender = Theme.formLabel(card, "Gender", "account_circle", y);
        male = new JRadioButton("Male");
        Theme.styleToggle(male);
        male.setBounds(250, y, 120, 38);
        card.add(male);

        female = new JRadioButton("Female");
        Theme.styleToggle(female);
        female.setBounds(390, y, 120, 38);
        card.add(female);

        genderGroup = new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);

        y += gap;
        email = Theme.formLabel(card, "Email Address", "email", y);
        emailTextField = Theme.textField();
        emailTextField.setBounds(250, y, 480, 38);
        card.add(emailTextField);

        y += gap;
        marital = Theme.formLabel(card, "Marital Status", "person", y);
        married = new JRadioButton("Married");
        Theme.styleToggle(married);
        married.setBounds(250, y, 120, 38);
        card.add(married);

        unmarried = new JRadioButton("Unmarried");
        Theme.styleToggle(unmarried);
        unmarried.setBounds(390, y, 120, 38);
        card.add(unmarried);

        other = new JRadioButton("Other");
        Theme.styleToggle(other);
        other.setBounds(530, y, 120, 38);
        card.add(other);

        maritalGroup = new ButtonGroup();
        maritalGroup.add(married);
        maritalGroup.add(unmarried);
        maritalGroup.add(other);

        y += gap;
        address = Theme.formLabel(card, "Address", "home", y);
        addressTextField = Theme.textField();
        addressTextField.setBounds(250, y, 480, 38);
        card.add(addressTextField);

        y += gap;
        city = Theme.formLabel(card, "District", "location_on", y);
        cityTextField = Theme.textField();
        cityTextField.setBounds(250, y, 480, 38);
        card.add(cityTextField);

        y += gap;
        district = Theme.formLabel(card, "Division", "location_on", y);
        stateTextField = Theme.textField();
        stateTextField.setBounds(250, y, 480, 38);
        card.add(stateTextField);

        y += gap;
        pin = Theme.formLabel(card, "Postal Code", "location_on", y);
        pinTextField = Theme.textField();
        pinTextField.setBounds(250, y, 480, 38);
        card.add(pinTextField);

        next = Theme.button("NEXT", "arrow_forward", Theme.PRIMARY);
        next.setHorizontalTextPosition(SwingConstants.LEFT);
        next.setBounds(580, 520, 150, 44);
        next.addActionListener(this);
        card.add(next);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String formno = "" + random;
        String name = nameTextField.getText();
        String fname = fnameTextField.getText();
        String dob = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText();
        String gender = null;
        if (male.isSelected()) {
            gender = "Male";
        } else if (female.isSelected()) {
            gender = "Female";
        }
        String email = emailTextField.getText();
        String marital = null;
        if (married.isSelected()) {
            marital = "Married";
        } else if (unmarried.isSelected()) {
            marital = "Unmarried";
        } else if (other.isSelected()) {
            marital = "Other";
        }
        String address = addressTextField.getText();
        String city = cityTextField.getText();
        String state = stateTextField.getText();
        String pin = pinTextField.getText();

        try {
            if (name.equals("")) {
                JOptionPane.showMessageDialog(null, "Name is required");
            } else if (fname.equals("")) {
                JOptionPane.showMessageDialog(null, "Father name is required");
            } else if (email.equals("")) {
                JOptionPane.showMessageDialog(null, "Email is required");
            } else if (address.equals("")) {
                JOptionPane.showMessageDialog(null, "Address is required");
            } else if (city.equals("")) {
                JOptionPane.showMessageDialog(null, "District is required");
            } else if (state.equals("")) {
                JOptionPane.showMessageDialog(null, "Division is required");
            } else if (pin.equals("")) {
                JOptionPane.showMessageDialog(null, "Pin is required");
            } else {
                Conn c = new Conn();
                c.update("INSERT INTO signup VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        formno, name, fname, dob, gender, email, marital, address, city, pin, state);
                setVisible(false);
                new SignupTwo(formno).setVisible(true);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String args[]) {
        new SignupOne();
    }
}
