package atm.simulator.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignupTwo extends JFrame implements ActionListener {

    JLabel rel, cat, inc, qual, occup, pan, nid, citz, pin;
    JTextField panTextField, nidTextField;
    JRadioButton syes, sno, eyes, eno;
    JComboBox religion, category, income, qualification, occupation;
    JButton next;
    ButtonGroup group1, group2;
    String formno;

    SignupTwo(String formno) {
        this.formno = formno;
        JPanel card = Theme.signupScreen(this, "Application Form No. " + formno, 2, "Additional Details", 540);

        int y = 30, gap = 48;
        rel = Theme.formLabel(card, "Religion", null, y);
        String religionArray[] = {"Muslim", "Hindu", "Christian", "Other"};
        religion = new JComboBox(religionArray);
        Theme.styleCombo(religion);
        religion.setBounds(250, y, 480, 38);
        card.add(religion);

        y += gap;
        cat = Theme.formLabel(card, "Category", null, y);
        String catArray[] = {"General", "Special", "VIP", "others"};
        category = new JComboBox(catArray);
        Theme.styleCombo(category);
        category.setBounds(250, y, 480, 38);
        card.add(category);

        y += gap;
        inc = Theme.formLabel(card, "Income", "attach_money", y);
        String incomeArray[] = {"0", "0 to 50000", "50000 to 100000", "upto 1000000"};
        income = new JComboBox(incomeArray);
        Theme.styleCombo(income);
        income.setBounds(250, y, 480, 38);
        card.add(income);

        y += gap;
        qual = Theme.formLabel(card, "Education", "school", y);
        String educationArray[] = {"Non-Graduation", "Graduation", "Post Graduation", "PHD", "Others"};
        qualification = new JComboBox(educationArray);
        Theme.styleCombo(qualification);
        qualification.setBounds(250, y, 480, 38);
        card.add(qualification);

        y += gap;
        occup = Theme.formLabel(card, "Occupation", "work", y);
        String occupationArray[] = {"Student", "Teacher", "Banker", "Doctor", "Others"};
        occupation = new JComboBox(occupationArray);
        Theme.styleCombo(occupation);
        occupation.setBounds(250, y, 480, 38);
        card.add(occupation);

        y += gap;
        pan = Theme.formLabel(card, "Phone Number", "phone", y);
        panTextField = Theme.textField();
        panTextField.setBounds(250, y, 480, 38);
        card.add(panTextField);

        y += gap;
        nid = Theme.formLabel(card, "NID Number", "badge", y);
        nidTextField = Theme.textField();
        nidTextField.setBounds(250, y, 480, 38);
        card.add(nidTextField);

        y += gap;
        citz = Theme.formLabel(card, "Bangladeshi Citizen", "verified_user", y);
        syes = new JRadioButton("Yes");
        Theme.styleToggle(syes);
        syes.setBounds(250, y, 120, 38);
        card.add(syes);

        sno = new JRadioButton("No");
        Theme.styleToggle(sno);
        sno.setBounds(390, y, 120, 38);
        card.add(sno);

        group1 = new ButtonGroup();
        group1.add(syes);
        group1.add(sno);

        y += gap;
        pin = Theme.formLabel(card, "Existing Account", "account_balance", y);
        eyes = new JRadioButton("Yes");
        Theme.styleToggle(eyes);
        eyes.setBounds(250, y, 120, 38);
        card.add(eyes);

        eno = new JRadioButton("No");
        Theme.styleToggle(eno);
        eno.setBounds(390, y, 120, 38);
        card.add(eno);

        group2 = new ButtonGroup();
        group2.add(eyes);
        group2.add(eno);

        next = Theme.button("NEXT", "arrow_forward", Theme.PRIMARY);
        next.setHorizontalTextPosition(SwingConstants.LEFT);
        next.setBounds(580, 470, 150, 44);
        next.addActionListener(this);
        card.add(next);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String sreligion = (String) religion.getSelectedItem();
        String scategory = (String) category.getSelectedItem();
        String sincome = (String) income.getSelectedItem();
        String squalification = (String) qualification.getSelectedItem();
        String soccupation = (String) occupation.getSelectedItem();

        String seniorcitizen = null;
        if (syes.isSelected()) {
            seniorcitizen = "Yes";
        } else if (sno.isSelected()) {
            seniorcitizen = "No";
        }
        String existingaccount = null;
        if (eyes.isSelected()) {
            existingaccount = "Yes";
        } else if (eno.isSelected()) {
            existingaccount = "No";
        }
        String span = panTextField.getText();
        String snid = nidTextField.getText();
        try {
            if (span.equals("")) {
                JOptionPane.showMessageDialog(null, "Phone Number is required");
            } else if (snid.equals("")) {
                JOptionPane.showMessageDialog(null, "NID   is required");
            } else {
                Conn c = new Conn();
                c.update("INSERT INTO signuptwo VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        formno, sreligion, scategory, sincome, squalification, soccupation, span, snid, seniorcitizen, existingaccount);
                setVisible(false);
                new SignupThree(formno).setVisible(true);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        new SignupTwo("");
    }
}
