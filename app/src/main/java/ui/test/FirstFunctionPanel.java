package ui.test;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ui.model.ButtonBean;
import ui.panels.FunctionPanel;


/**
 * Created by ty on 2017/6/23.
 */

public class FirstFunctionPanel extends FunctionPanel {

    public FirstFunctionPanel(ButtonBean buttonBean) {
        super(buttonBean);

        JPanel controlPanel = createControlPanel();
        add(controlPanel);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(layout);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.insets = new Insets(20, 10, 0, 10);
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        JLabel searchLabel = new JLabel("搜索线路或里程");
        panel.add(searchLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 1.0;
        constraints.insets.top = 0;
        constraints.insets.bottom = 12;
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        // c.fill = GridBagConstraints.HORIZONTAL;
        JTextField filterField = new JTextField(24);
        filterField.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {

                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {

                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {

                    }
                });
        panel.add(filterField, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        // constraints.insets.right = 24;
        // constraints.insets.left = 12;
        constraints.weightx = 0.0;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.fill = GridBagConstraints.NONE;
        JCheckBox winnersCheckbox = new JCheckBox("只显示xxx");
        winnersCheckbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

            }
        });
        panel.add(winnersCheckbox, constraints);
        return panel;
    }

}
