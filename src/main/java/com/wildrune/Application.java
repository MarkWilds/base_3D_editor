package com.wildrune;

import com.wildrune.gui.EditorFrame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
                //SubstanceBusinessBlackSteelLookAndFeel
                //SubstanceCeruleanLookAndFeel
                //SubstanceGeminiLookAndFeel
                //SubstanceGraphiteLookAndFeel
                UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel");
            } catch (Exception e) {
                System.out.println("Substance skin failed to initialize");
            }
            EditorFrame application = new EditorFrame();
            application.setVisible(true);
        });
    }
}
