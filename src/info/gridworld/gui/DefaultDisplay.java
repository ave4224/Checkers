/*
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2005-2006 Cay S. Horstmann (http://horstmann.com)
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * @author Cay Horstmann
 */
package info.gridworld.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

public class DefaultDisplay
        implements Display {
    private static final int MAX_TEXT_LENGTH = 8;
    public void draw(Object obj, Component comp, Graphics2D g2, Rectangle rect) {
        Color color = (Color) AbstractDisplay.getProperty(obj, "color");
        if ((color == null) && ((obj instanceof Color))) {
            color = (Color) obj;
        }
        Color textColor = (Color) AbstractDisplay.getProperty(obj, "textColor");
        if (textColor == null) {
            textColor = Color.BLACK;
        }
        if (color != null) {
            g2.setPaint(color);
            g2.fill(rect);
            if (color.equals(textColor)) {
                textColor = new Color(255 - textColor.getRed(), 255 - textColor.getGreen(), 255 - textColor.getBlue());
            }
        }
        String text = (String) AbstractDisplay.getProperty(obj, "text");
        if ((text == null) && (!(obj instanceof Color))) {
            text = "" + obj;
        }
        if (text == null) {
            return;
        }
        if (text.length() > 8) {
            text = text.substring(0, 8) + "...";
        }
        paintCenteredText(g2, text, rect, 0.8D, textColor);
    }
    protected void paintCenteredText(Graphics2D g2, String s, Rectangle rect, double fontHeight, Color color) {
        g2 = (Graphics2D) g2.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(color);
        Rectangle2D bounds = null;
        LineMetrics lm = null;
        boolean done = false;
        while (!done) {
            g2.setFont(new Font("SansSerif", 1, (int) (fontHeight * rect.height)));
            FontRenderContext frc = g2.getFontRenderContext();
            bounds = g2.getFont().getStringBounds(s, frc);
            if (bounds.getWidth() > rect.getWidth()) {
                fontHeight = fontHeight * Math.sqrt(2.0D) / 2.0D;
            } else {
                done = true;
                lm = g2.getFont().getLineMetrics(s, frc);
            }
        }
        float centerX = rect.x + rect.width / 2;
        float centerY = rect.y + rect.height / 2;
        float leftX = centerX - (float) bounds.getWidth() / 2.0F;
        float baselineY = centerY - lm.getHeight() / 2.0F + lm.getAscent();
        g2.drawString(s, leftX, baselineY);
        g2.dispose();
    }
}
