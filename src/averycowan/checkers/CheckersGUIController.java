/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package averycowan.checkers;

import info.gridworld.gui.DisplayMap;
import info.gridworld.gui.GUIController;
import info.gridworld.gui.GridPanel;
import info.gridworld.gui.WorldFrame;
import java.util.ResourceBundle;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class extends GUIController to convert the JSlider into a difficulty
 * slider rather than a time slider.
 *
 * @author avecowa
 */
public class CheckersGUIController<T> extends GUIController<T> {
    /**
     * The minimum allowed depth on the slider
     */
    protected static final int MIN_ITERS = 3;
    /**
     * The maximum allowed depth on the slider
     */
    protected static final int MAX_ITERS = 8;
    /**
     * The default depth on the slider
     */
    protected static final int INITIAL_ITERS = 6;
    /**
     * Constructs a JSlider with a custom configuration
     *
     * @param guic The instance of GUIController that the JSlider will belong to
     */
    public static JSlider constructSpeedSlider(GUIController guic) {
        if (!(guic instanceof CheckersGUIController)) {
            JSlider speedSlider = new JSlider(MIN_DELAY_MSECS, MAX_DELAY_MSECS, INITIAL_DELAY);
            speedSlider.setInverted(true);
            speedSlider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent evt) {
                    guic.timer.setDelay(((JSlider) evt.getSource()).getValue());
                }
            });
            return speedSlider;
        }
        JSlider speedSlider = new JSlider(MIN_ITERS, MAX_ITERS, INITIAL_ITERS);
        speedSlider.setLabelTable(speedSlider.createStandardLabels(1, 3));
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                ((CheckersWorld) guic.parentFrame.getWorld()).setIters(((JSlider) evt.getSource()).getValue());
            }
        });
        return speedSlider;
    }
    public CheckersGUIController(WorldFrame parent, GridPanel disp, DisplayMap displayMap, ResourceBundle res) {
        super(parent, disp, displayMap, res);
    }
    /**
     * Alters the CheckersWorld to start auto-playing. Does not start the timer
     */
    @Override
    public void run() {
        display.setToolTipsEnabled(false); // hide tool tips while running
        parentFrame.setRunMenuItemsEnabled(false);
        stopButton.setEnabled(true);
        stepButton.setEnabled(false);
        runButton.setEnabled(false);
        //numStepsSoFar = 0;
        //timer.start();
        running = true;
        if (parentFrame.getWorld() instanceof CheckersWorld) {
            ((CheckersWorld) parentFrame.getWorld()).run();
        }
    }
    /**
     * Alters the CheckersWorld to stop auto-playing. Does not stop the timer.
     */
    @Override
    public void stop() {
        display.setToolTipsEnabled(true);
        parentFrame.setRunMenuItemsEnabled(true);
        //timer.stop();
        stopButton.setEnabled(false);
        runButton.setEnabled(true);
        stepButton.setEnabled(true);
        running = false;
        if (parentFrame.getWorld() instanceof CheckersWorld) {
            ((CheckersWorld) parentFrame.getWorld()).stop();
        }
    }
}
