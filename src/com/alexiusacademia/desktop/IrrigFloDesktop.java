package com.alexiusacademia.desktop;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class IrrigFloDesktop extends JFrame {

  private JDesktopPane desktopPane;
  private JPanel mainPanel;
  private JMenuBar menuBar;

  public IrrigFloDesktop(String title) {
    super(title);

    mainPanel = new JPanel();

    // Create the menus
    createMenuBar();
    this.setJMenuBar(menuBar);

    // Create horizontal panel
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

    // Add the project navigation on the left (First entry on BoxLayout)
    createProjectNavigationTree();

    // Add the desktop pane on the right
    createDesktopPane();

    // Add the main panel
    this.add(mainPanel);

    // Set the size
    this.setSize(600, 400);
    this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    this.setVisible(true);
  }

  private void createProjectNavigationTree() {
    // Create the root node
    DefaultMutableTreeNode nodeRoot = new DefaultMutableTreeNode("IrrigFlo");

    // Create children nodes
    DefaultMutableTreeNode nodeOpenChannel = new DefaultMutableTreeNode("Open Channel Flow");
    DefaultMutableTreeNode nodePipes = new DefaultMutableTreeNode("Pipes");
    DefaultMutableTreeNode nodeWeirs = new DefaultMutableTreeNode("Weirs / Diversion Dams");

    // Sub-children nodes
    // -- Open Channel
    DefaultMutableTreeNode nodeRectangularOpenChannel = new DefaultMutableTreeNode("Rectangular Open Channel");

    // Add the children to root node
    nodeRoot.add(nodeOpenChannel);
    nodeRoot.add(nodePipes);
    nodeRoot.add(nodeWeirs);

    // Add the sub-children
    nodeOpenChannel.add(nodeRectangularOpenChannel);

    JTree projectNavigationTree = new JTree(nodeRoot);

    // Hide the root node
    projectNavigationTree.setRootVisible(true);

    JScrollPane projectNavigationScroller = new JScrollPane(projectNavigationTree);

    JPanel navigationPanel = new JPanel();
    navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.PAGE_AXIS));
    navigationPanel.setPreferredSize(new Dimension(250, 600));
    navigationPanel.add(projectNavigationScroller);

    mainPanel.add(navigationPanel, BorderLayout.NORTH);

  }

  private void createMenuBar() {
    menuBar = new JMenuBar();

    JMenu menuFile = new JMenu();
    menuFile.setText("File");

    JMenu menuHelp = new JMenu();
    menuHelp.setText("Help");

    menuBar.add(menuFile);
    menuBar.add(menuHelp);
  }

  private void createDesktopPane() {
    desktopPane = new JDesktopPane();
    desktopPane.setBackground(Color.darkGray);
    mainPanel.add(desktopPane);
  }

  // Main method
  public static void main(String[] args) {
    new IrrigFloDesktop("IrrigFlo");
  }
}
