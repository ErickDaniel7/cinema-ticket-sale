package org.cinematicket;

import org.cinematicket.app.CinemaApp;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CinemaApp().setVisible(true);
        });
    }
}
