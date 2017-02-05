/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * A button used to choose a file/folder.
 *
 * @author Fernando Correa da Conceição
 *
 * @todo All this class need test.
 */
public class ButtonChooseFile extends JButton {

    /**
     * Used for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Text field that will have the value changed when the user chooses a file
     * with this button.
     */
    private JTextField textField = null;

    /**
     * Table to add the result.
     */
    private JTable tableField = null;

    /**
     * The model to add the result.
     */
    DefaultTableModel modelTable = null;

    /**
     * Constructor.
     */
    public ButtonChooseFile() {
        super();
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonActionPerformed(event);
            }
        });
    }

    /**
     * Constructor.
     *
     * @param label The text of the button.
     */
    public ButtonChooseFile(String label) {
        super(label);
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                buttonActionPerformed(event);
            }
        });
    }

    /**
     * Event when the button is clicked.
     *
     * @param event The click event.
     */
    private void buttonActionPerformed(ActionEvent event) {
        File path;
        FileTypeFilter filter = new FileTypeFilter();

        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int returnVal = fc.showDialog(this, "Ok");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (this.textField != null) {
                this.textField.setText(fc.getSelectedFile().getAbsolutePath());
            } else if (this.tableField != null) {
                this.modelTable.addRow(
                        new Object[]{fc.getSelectedFile().getAbsolutePath()});
            }
        }
    }

    /**
     * Set the table used by button.
     *
     * Set the table to use. When this is set and the user choose a file, will
     * insert a line in the table with the filename.
     *
     * @param tableField The table control.
     * @param modelTable The model of the table.
     */
    public void setTable(JTable tableField, DefaultTableModel modelTable) {
        this.tableField = tableField;
        this.modelTable = modelTable;
    }

    /**
     * Return the text field that contains the filename.
     *
     * @return The textField
     */
    public JTextField getTextField() {
        return textField;
    }

    /**
     * Set the text field.
     *
     * When this is set and the user chooses a file, this text field change with
     * the full path of choosen file/folder.
     *
     * @param textField the textField to set
     */
    public void setTextField(JTextField textField) {
        this.textField = textField;
    }
}
