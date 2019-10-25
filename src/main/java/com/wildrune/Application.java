package com.wildrune;

import com.wildrune.gui.EditorFrame;
import org.pushingpixels.substance.api.skin.SubstanceNightShadeLookAndFeel;

import javax.swing.*;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 12/08/17
 */
public class Application {

    public static void main(String... args) {
        JFrame.setDefaultLookAndFeelDecorated(true);

        SwingUtilities.invokeLater(() -> {
            try {
                var laf = new SubstanceNightShadeLookAndFeel();
                UIManager.setLookAndFeel(laf);
            } catch (Exception e) {
                System.out.println("Substance skin failed to initialize");
            }
            EditorFrame application = new EditorFrame();
            application.setVisible(true);
        });
    }
}
