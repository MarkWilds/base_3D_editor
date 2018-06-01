package com.wildrune.gui;

import com.esotericsoftware.tablelayout.swing.Table;
import com.wildrune.drawables.AxisIndicatorDrawable;
import com.wildrune.drawables.CrosshairDrawable;
import com.wildrune.drawables.CubeHandlesDrawable;
import com.wildrune.drawables.Grid;
import com.wildrune.graphics.GL2Renderer;
import com.wildrune.gui.viewport.GLViewport;
import com.wildrune.gui.viewport.OrthoViewport;
import com.wildrune.gui.viewport.PerspViewport;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

/**
 * @author Mark van der Wal
 * @web www.markvanderwal.nl
 * @since 12/08/17
 */
public class EditorFrame extends JFrame {

    private final static int toolAndStatusBarHeight = 28;

    public EditorFrame() {
        super("Experimental Editor");

        setSize(1600, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("icon.png");
        setIconImage(icon.getImage());

        createMenu();

        Table editorContent = new Table();
//        createToolbar(editorContent);
//        editorContent.row();
        createGLWidget(editorContent);
//        createPropertiesPanel(editorContent);
//        editorContent.row();
//        createStatusbar(editorContent);
        add(editorContent);
    }

    private void createGLWidget(Table table) {
        Grid viewportGrid = new Grid(512);
        CubeHandlesDrawable chd = new CubeHandlesDrawable();
        AxisIndicatorDrawable aid = new AxisIndicatorDrawable();

        GL2Renderer renderer = new GL2Renderer();
        GLViewport perspViewport = new PerspViewport(renderer);
        GLViewport topViewport = new OrthoViewport(renderer, GLViewport.ViewportType.ORTHO_TOP);
        GLViewport sideViewport = new OrthoViewport(renderer, GLViewport.ViewportType.ORTHO_SIDE);
        GLViewport frontViewport = new OrthoViewport(renderer, GLViewport.ViewportType.ORTHO_FRONT);

        // add drawables
        topViewport.addDrawable(viewportGrid);
        sideViewport.addDrawable(viewportGrid);
        frontViewport.addDrawable(viewportGrid);
        topViewport.addDrawable(chd);
        sideViewport.addDrawable(chd);
        frontViewport.addDrawable(chd);
        topViewport.addDrawable(aid);
        sideViewport.addDrawable(aid);
        frontViewport.addDrawable(aid);
        perspViewport.addDrawable(new CrosshairDrawable());

        // add to layout
        Table gridTable = new Table();
        gridTable.addCell(perspViewport).expand().fill().space(1);
        gridTable.addCell(topViewport).expand().fill().space(1);
        gridTable.row();
        gridTable.addCell(sideViewport).expand().fill().space(1);
        gridTable.addCell(frontViewport).expand().fill().space(1);

        table.addCell(gridTable).expand().fill().pad(1);
    }

    private void createPropertiesPanel(Table editorContent) {
        JTabbedPane propertiesPane = new JTabbedPane();

        JPanel geometryPanel = new JPanel();
        JPanel otherPanel = new JPanel();

        propertiesPane.addTab("Geometry", geometryPanel);
        propertiesPane.addTab("Other", otherPanel);

        editorContent.addCell(propertiesPane).width(196).expandY().fillY();
    }

    private void createStatusbar(Table editorContent) {
        JPanel statusBar = new JPanel();
        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));

        editorContent.addCell(statusBar).height(toolAndStatusBarHeight).expandX().fill().colspan(2);
    }

    private void createToolbar(Table editorContent) {
        JToolBar editorToolbar = new JToolBar();
        editorToolbar.setFloatable(false);
        editorToolbar.setLayout(new GridLayout());

        ImageIcon newIcon = new ImageIcon(getClass().getResource("/toolbar.icons/new.png"));
        editorToolbar.add(new AbstractAction("New", newIcon) {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        ImageIcon openIcon = new ImageIcon(getClass().getResource("/toolbar.icons/open.png"));
        editorToolbar.add(new AbstractAction("Open", openIcon) {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        ImageIcon saveIcon = new ImageIcon(getClass().getResource("/toolbar.icons/save.png"));
        editorToolbar.add(new AbstractAction("Save", saveIcon) {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        editorToolbar.addSeparator();

        editorContent.addCell(editorToolbar).height(toolAndStatusBarHeight).expandX().left().colspan(2);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu viewMenu = new JMenu("View");
        JMenu toolsMenu = new JMenu("Tools");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem menuItem = new JMenuItem("Exit");
        menuItem.addActionListener(event -> System.exit(0));

        fileMenu.add(menuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
}
