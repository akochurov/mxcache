package com.maxifier.mxcache.ideaplugin;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: kochurov
 * Date: 02.03.12
 * Time: 12:56
 */
public class MxCacheConfiguration implements ProjectComponent, Configurable {
    private final StaticInstrumentorInstaller installer;
    private JCheckBox enabledCheckbox;

    public MxCacheConfiguration(StaticInstrumentorInstaller installer) {
        this.installer = installer;
    }

    @Override
    public void projectOpened() {
    }

    @Override
    public void projectClosed() {
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "MxCacheSettings";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "MxCache";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public JComponent createComponent() {
        JPanel p = new JPanel(new BorderLayout());
        enabledCheckbox = new JCheckBox("Enable instrumentation");
        reset();
        p.add(enabledCheckbox, BorderLayout.NORTH);
        return p;
    }

    @Override
    public boolean isModified() {
        return enabledCheckbox.isSelected() != installer.isEnabled();
    }

    @Override
    public void apply() throws ConfigurationException {
        installer.setEnabled(enabledCheckbox.isSelected());
    }

    @Override
    public void reset() {
        enabledCheckbox.setSelected(installer.isEnabled());
    }

    @Override
    public void disposeUIResources() {
        enabledCheckbox = null;
    }
}