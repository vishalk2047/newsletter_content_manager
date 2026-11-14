package com.newsletter.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {

    public enum Type { PRIMARY, SECONDARY, SIDEBAR, LINK }

    private static final Color NAVY         = new Color(0x2C3E50);
    private static final Color NAVY_HOVER   = new Color(0x3D566E);
    private static final Color NAVY_PRESSED = new Color(0x1F2D3B);
    private static final Color BORDER_GREY  = new Color(0xC5CCD3);

    private final Type type;
    private boolean hover, pressed, active;

    public ModernButton(String text) {
        this(text, Type.PRIMARY);
    }

    public ModernButton(String text, Type type) {
        super(text);
        this.type = type;
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e)  { hover = true;  repaint(); }
            @Override public void mouseExited(MouseEvent e)   { hover = false; pressed = false; repaint(); }
            @Override public void mousePressed(MouseEvent e)  { pressed = true;  repaint(); }
            @Override public void mouseReleased(MouseEvent e) { pressed = false; repaint(); }
        });
    }

    public void setActive(boolean active) {
        this.active = active;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight(), arc = 10;
        Color bg = null, fg = Color.WHITE;

        switch (type) {
            case PRIMARY:
                bg = pressed ? NAVY_PRESSED : hover ? NAVY_HOVER : NAVY;
                break;
            case SECONDARY:
                bg = pressed ? new Color(0xD5DBE1) : hover ? new Color(0xE4E8EC) : Color.WHITE;
                fg = NAVY;
                break;
            case SIDEBAR:
                bg = pressed ? NAVY_PRESSED : active ? NAVY_PRESSED : hover ? NAVY_HOVER : null;
                break;
            case LINK:
                break; // no background, keeps its own foreground colour
        }

        if (!isEnabled() && type != Type.LINK) {
            bg = new Color(0xB8C0C8);
            fg = Color.WHITE;
        }

        if (bg != null) {
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);
        }
        if (type == Type.SECONDARY) {
            g2.setColor(BORDER_GREY);
            g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);
        }
        if (type != Type.LINK) setForeground(fg);

        g2.dispose();
        super.paintComponent(g);   // draws the text on top
    }
}