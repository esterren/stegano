/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SteganoGUI.java
 *
 * Created on 20.03.2012, 15:06:27
 */
package ch.zhaw.swp2.stegano.gui;

/**
 *
 * @author rest
 */
public class SteganoGUI extends javax.swing.JFrame {

    /** Creates new form SteganoGUI */
    public SteganoGUI() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        _jMenuBar1 = new javax.swing.JMenuBar();
        _jMenuFile = new javax.swing.JMenu();
        _jMenuItemOB = new javax.swing.JMenuItem();
        jMenuItemIM = new javax.swing.JMenuItem();
        _jMenuEdit = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        _jMenuFile.setText("File");

        _jMenuItemOB.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        _jMenuItemOB.setText("Import Basefile");
        _jMenuFile.add(_jMenuItemOB);

        jMenuItemIM.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemIM.setText("Import Message-File");
        jMenuItemIM.setActionCommand("Import Message-File");
        _jMenuFile.add(jMenuItemIM);

        _jMenuBar1.add(_jMenuFile);

        _jMenuEdit.setText("Edit");
        _jMenuBar1.add(_jMenuEdit);

        setJMenuBar(_jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 904, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 525, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SteganoGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SteganoGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SteganoGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SteganoGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new SteganoGUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar _jMenuBar1;
    private javax.swing.JMenu _jMenuEdit;
    private javax.swing.JMenu _jMenuFile;
    private javax.swing.JMenuItem _jMenuItemOB;
    private javax.swing.JMenuItem jMenuItemIM;
    // End of variables declaration//GEN-END:variables
}
