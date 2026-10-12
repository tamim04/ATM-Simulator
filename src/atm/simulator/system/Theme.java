package atm.simulator.system;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

/**
 * Shared look and feel for every screen: colors, fonts, icons and styled components.
 */
public final class Theme {

    // Palette
    public static final Color PRIMARY = new Color(0x0B3D91);
    public static final Color PRIMARY_DARK = new Color(0x06245A);
    public static final Color PRIMARY_LIGHT = new Color(0xE8EFFC);
    public static final Color ACCENT = new Color(0x0FA968);
    public static final Color DANGER = new Color(0xD64545);
    public static final Color WARNING = new Color(0xE8A317);
    public static final Color BG = new Color(0xF1F4FA);
    public static final Color CARD = Color.WHITE;
    public static final Color TEXT = new Color(0x1E293B);
    public static final Color MUTED = new Color(0x64748B);
    public static final Color BORDER = new Color(0xCBD5E1);

    private static final String FONT_FAMILY = pickFontFamily();

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e);
        }
        UIManager.put("OptionPane.messageFont", font(Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", font(Font.BOLD, 13));
        UIManager.put("ComboBox.font", font(Font.PLAIN, 14));
        UIManager.put("Label.font", font(Font.PLAIN, 14));
    }

    private Theme() {
    }

    private static String pickFontFamily() {
        String[] available = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String preferred : new String[]{"Segoe UI", "Helvetica Neue", "Arial"}) {
            for (String name : available) {
                if (name.equals(preferred)) {
                    return name;
                }
            }
        }
        return Font.SANS_SERIF;
    }

    public static Font font(int style, int size) {
        return new Font(FONT_FAMILY, style, size);
    }

    /** Loads a PNG from the icons folder, scales it and recolors it with the given tint. */
    public static ImageIcon icon(String name, int size, Color tint) {
        URL url = ClassLoader.getSystemResource("icons/" + name + ".png");
        if (url == null) {
            System.out.println("Icon not found: " + name);
            return null;
        }
        Image src = new ImageIcon(url).getImage();
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(src, 0, 0, size, size, null);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(tint);
        g.fillRect(0, 0, size, size);
        g.dispose();
        return new ImageIcon(img);
    }

    /** Common frame setup: title, app icon, size and centered position. */
    public static void setupFrame(JFrame frame, String title, int width, int height) {
        frame.setTitle(title);
        ImageIcon appIcon = icon("account_balance", 64, PRIMARY);
        if (appIcon != null) {
            frame.setIconImage(appIcon.getImage());
        }
        frame.setLayout(null);
        frame.getContentPane().setBackground(BG);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public static JLabel label(String text, int style, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font(style, size));
        label.setForeground(color);
        return label;
    }

    /** Label for a form field. */
    public static JLabel fieldLabel(String text) {
        return label(text, Font.BOLD, 14, TEXT);
    }

    /** Navy header strip with logo, title and subtitle. */
    public static JPanel header(String title, String subtitle, int width) {
        JPanel header = new GradientPanel(PRIMARY, PRIMARY_DARK, false);
        header.setLayout(null);
        header.setBounds(0, 0, width, 90);

        JLabel logo = new JLabel(icon("account_balance", 44, Color.WHITE));
        logo.setBounds(30, 23, 44, 44);
        header.add(logo);

        JLabel t = label(title, Font.BOLD, 24, Color.WHITE);
        t.setBounds(90, 16, width - 120, 34);
        header.add(t);

        JLabel s = label(subtitle, Font.PLAIN, 14, new Color(0xC7D6F5));
        s.setBounds(91, 50, width - 120, 22);
        header.add(s);
        return header;
    }

    /** Standard ATM screen: header plus a centered white card. Returns the card to add content to. */
    public static Card screen(JFrame frame, String subtitle, int cardHeight) {
        setupFrame(frame, "ATM Simulator - " + subtitle, 820, cardHeight + 160);
        frame.add(header("ATM Simulator", subtitle, 820));
        Card card = new Card();
        card.setBounds(60, 120, 704, cardHeight);
        frame.add(card);
        return card;
    }

    /** Signup form screen: header with a 3-step progress indicator plus a card. Returns the card. */
    public static Card signupScreen(JFrame frame, String formTitle, int step, String stepName, int cardHeight) {
        setupFrame(frame, "Account Application - Step " + step + " of 3", 850, cardHeight + 130);
        JPanel header = header(formTitle, "Step " + step + " of 3  -  " + stepName, 850);
        Stepper stepper = new Stepper(step);
        stepper.setBounds(545, 8, 270, 76);
        header.add(stepper);
        frame.add(header);

        Card card = new Card();
        card.setBounds(40, 110, 774, cardHeight);
        frame.add(card);
        return card;
    }

    /** Label on the left side of a form row inside a card. */
    public static JLabel formLabel(JPanel card, String text, String iconName, int y) {
        JLabel l = fieldLabel(text);
        if (iconName != null) {
            l.setIcon(icon(iconName, 18, PRIMARY));
            l.setIconTextGap(8);
        }
        l.setBounds(40, y, 200, 36);
        card.add(l);
        return l;
    }

    /** Title and helper text at the top of a card. */
    public static void cardTitle(JPanel card, String title, String hint) {
        JLabel t = label(title, Font.BOLD, 22, TEXT);
        t.setBounds(30, 24, 640, 32);
        card.add(t);
        JLabel h = label(hint, Font.PLAIN, 14, MUTED);
        h.setBounds(30, 56, 640, 22);
        card.add(h);
    }

    // ---------- Buttons ----------

    /** Solid rounded button with optional icon. */
    public static JButton button(String text, String iconName, Color bg) {
        return new RoundButton(text, iconName == null ? null : icon(iconName, 20, Color.WHITE), bg, Color.WHITE, false);
    }

    /** Outlined rounded button with optional icon. */
    public static JButton outlineButton(String text, String iconName, Color color) {
        return new RoundButton(text, iconName == null ? null : icon(iconName, 20, color), CARD, color, true);
    }

    /** Large tile button with the icon above the text, used on the ATM menus. */
    public static JButton tile(String text, String iconName, Color color) {
        JButton b = new RoundButton(text, icon(iconName, 36, color), CARD, TEXT, true);
        b.setFont(font(Font.BOLD, 15));
        b.setVerticalTextPosition(SwingConstants.BOTTOM);
        b.setHorizontalTextPosition(SwingConstants.CENTER);
        b.setIconTextGap(8);
        return b;
    }

    // ---------- Inputs ----------

    private static Border fieldBorder(Color color) {
        return new CompoundBorder(new RoundBorder(color, 10), new EmptyBorder(4, 12, 4, 12));
    }

    public static void styleField(JTextField field) {
        field.setFont(font(Font.PLAIN, 15));
        field.setForeground(TEXT);
        field.setCaretColor(PRIMARY);
        field.setBackground(CARD);
        field.setBorder(fieldBorder(BORDER));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(fieldBorder(PRIMARY));
            }

            public void focusLost(FocusEvent e) {
                field.setBorder(fieldBorder(BORDER));
            }
        });
    }

    public static JTextField textField() {
        JTextField field = new JTextField();
        styleField(field);
        return field;
    }

    public static JPasswordField passwordField() {
        JPasswordField field = new JPasswordField();
        styleField(field);
        field.setEchoChar((char) 0x25CF);
        return field;
    }

    public static void styleCombo(JComboBox<?> combo) {
        combo.setFont(font(Font.PLAIN, 14));
        combo.setBackground(CARD);
        combo.setForeground(TEXT);
    }

    /** Radio buttons and check boxes: transparent background and theme font. */
    public static void styleToggle(AbstractButton b) {
        b.setOpaque(false);
        b.setFocusPainted(false);
        b.setFont(font(Font.PLAIN, 14));
        b.setForeground(TEXT);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // ---------- Custom components ----------

    /** White rounded card with a soft shadow. */
    public static class Card extends JPanel {
        public Card() {
            setOpaque(false);
            setLayout(null);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (int i = 0; i < 6; i++) {
                g2.setColor(new Color(15, 23, 42, 6));
                g2.fillRoundRect(i / 2, i, getWidth() - i, getHeight() - i, 22, 22);
            }
            g2.setColor(CARD);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 6, 20, 20);
            g2.dispose();
        }
    }

    /** Panel painted with a two-color gradient (diagonal or vertical). */
    public static class GradientPanel extends JPanel {
        private final Color from, to;
        private final boolean diagonal;

        public GradientPanel(Color from, Color to, boolean diagonal) {
            this.from = from;
            this.to = to;
            this.diagonal = diagonal;
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(0, 0, from, diagonal ? getWidth() : 0, getHeight(), to));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    /** Three numbered circles showing progress through the signup form. */
    private static class Stepper extends JComponent {
        private static final String[] NAMES = {"Personal", "Additional", "Account"};
        private final int step;

        Stepper(int step) {
            this.step = step;
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int r = 15, cy = 26, gap = 100, x0 = 35;
            g2.setStroke(new BasicStroke(2f));
            for (int i = 0; i < 2; i++) {
                g2.setColor(i + 1 < step ? Color.WHITE : new Color(255, 255, 255, 90));
                g2.drawLine(x0 + i * gap + r, cy, x0 + (i + 1) * gap - r, cy);
            }
            for (int i = 0; i < 3; i++) {
                int cx = x0 + i * gap;
                boolean done = i + 1 <= step;
                g2.setColor(done ? Color.WHITE : new Color(255, 255, 255, 90));
                if (done) {
                    g2.fillOval(cx - r, cy - r, 2 * r, 2 * r);
                } else {
                    g2.drawOval(cx - r, cy - r, 2 * r, 2 * r);
                }
                g2.setFont(font(Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                String n = String.valueOf(i + 1);
                g2.setColor(done ? PRIMARY : Color.WHITE);
                g2.drawString(n, cx - fm.stringWidth(n) / 2, cy + fm.getAscent() / 2 - 2);

                g2.setFont(font(i + 1 == step ? Font.BOLD : Font.PLAIN, 12));
                fm = g2.getFontMetrics();
                g2.setColor(i + 1 == step ? Color.WHITE : new Color(0xC7D6F5));
                g2.drawString(NAMES[i], cx - fm.stringWidth(NAMES[i]) / 2, cy + r + 20);
            }
            g2.dispose();
        }
    }

    private static class RoundBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        RoundBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(x + 1, y + 1, w - 3, h - 3, radius, radius));
            g2.dispose();
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(3, 3, 3, 3);
        }
    }

    private static class RoundButton extends JButton {
        private final Color bg, fg;
        private final boolean outline;
        private boolean hover, pressed;

        RoundButton(String text, Icon icon, Color bg, Color fg, boolean outline) {
            super(text, icon);
            this.bg = bg;
            this.fg = fg;
            this.outline = outline;
            setFont(font(Font.BOLD, 14));
            setForeground(fg);
            setIconTextGap(8);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    hover = false;
                    pressed = false;
                    repaint();
                }

                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    repaint();
                }

                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    repaint();
                }
            });
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 1, h = getHeight() - 1;
            if (outline) {
                g2.setColor(pressed ? PRIMARY_LIGHT.darker() : hover ? PRIMARY_LIGHT : bg);
                g2.fillRoundRect(0, 0, w, h, 14, 14);
                g2.setColor(hover ? PRIMARY : (fg == TEXT ? BORDER : fg));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 14, 14);
            } else {
                Color base = pressed ? bg.darker().darker() : hover ? bg.darker() : bg;
                g2.setColor(base);
                g2.fillRoundRect(0, 0, w, h, 14, 14);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
