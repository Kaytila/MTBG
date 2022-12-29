package net.ck.game.demos;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SuperConstructor extends JFrame
{
    private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

    public SuperConstructor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(300, 300));
        setTitle("Super constructor");
        Container cp = getContentPane();
        JButton b = new JButton("Show dialog");
        b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                FirstDialog firstDialog = new FirstDialog(SuperConstructor.this);
            }
        });
        cp.add(b, BorderLayout.SOUTH);
        JButton bClose = new JButton("Close");
        bClose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });
        add(bClose, BorderLayout.NORTH);
        pack();
        setVisible(true);
    }

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                SuperConstructor superConstructor = new SuperConstructor();
            }
        });
    }

    private class FirstDialog extends JDialog {

        private static final long serialVersionUID = 1L;

        FirstDialog(final Frame parent) {
            super(parent, "FirstDialog");
            setPreferredSize(new Dimension(200, 200));
            setLocationRelativeTo(parent);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
            JButton bNext = new JButton("Show next dialog");
            bNext.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    SecondDialog secondDialog = new SecondDialog(parent, false);
                }
            });
            add(bNext, BorderLayout.NORTH);
            JButton bClose = new JButton("Close");
            bClose.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    setVisible(false);
                }
            });
            add(bClose, BorderLayout.SOUTH);
            pack();
            setVisible(true);
        }
    }
    private int i;

    private class SecondDialog extends JDialog {

        private static final long serialVersionUID = 1L;

        SecondDialog(final Frame parent, boolean modal) {
            //super(parent); // < --- Makes this dialog 
            //unfocusable as long as FirstDialog is visible
            setPreferredSize(new Dimension(200, 200));
            setLocation(300, 50);
            setModal(modal);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setTitle("SecondDialog " + (i++));
            JButton bClose = new JButton("Close");
            bClose.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    setVisible(false);
                }
            });
            add(bClose, BorderLayout.SOUTH);
            pack();
            setVisible(true);
        }
    }
}
