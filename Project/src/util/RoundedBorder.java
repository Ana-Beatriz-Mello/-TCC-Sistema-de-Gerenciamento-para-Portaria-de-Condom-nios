package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedBorder extends JButton {

	private static final long serialVersionUID = 3213410207432798181L;
	
	private final Color bg = new Color(175, 111, 191); // COR DE FUNDO DO BOTÃO
	private final Color border = new Color(0,0,0); // COR DA BORDA DO BOTÃO
	private final Color textC = new Color(255, 255, 255); // COR DO TEXTO DO BOTÃO
	
	private int radius;
    @SuppressWarnings("unused")
	private boolean isHovered; 

    public RoundedBorder(String text, int radius) {
        super(text);
        this.radius = radius;
        this.isHovered = false;

        setContentAreaFilled(false); 
        setOpaque(false);  

        setForeground(textC);
        setBackground(bg);  
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
        
        
        
    }

    @Override
    protected void paintComponent(Graphics g) {

        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);  

        super.paintComponent(g); 
    }

    @Override
    protected void paintBorder(Graphics g) {
    	Color borderColor = border; // COR DA BORDA
    	
    	if (isHovered) { 
            borderColor = new Color(255, 255, 0);
        }
    	
        g.setColor(borderColor);  
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
    }
}
