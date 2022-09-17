/*
 * Copyright (c) 2005-2021 Radiance Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of the copyright holder nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.pushingpixels.radiance.component.api.common.model;

import org.pushingpixels.radiance.component.api.common.CommandButtonPresentationState;
import org.pushingpixels.radiance.component.api.common.JCommandButton;
import org.pushingpixels.radiance.component.internal.utils.WeakChangeSupport;

import javax.swing.event.ChangeListener;

public class CommandPanelPresentationModel implements MutablePresentationModel, ChangeAware {
    /**
     * Stores the listeners on this model.
     */
    private final WeakChangeSupport weakChangeSupport;

    /**
     * Maximum number of columns for the panel. Relevant only when the layout
     * kind is {@link LayoutKind#ROW_FILL}.
     */
    private int maxColumns = -1;

    /**
     * Maximum number of rows for the panel. Relevant only when the layout kind
     * is {@link LayoutKind#COLUMN_FILL}.
     */
    private int maxRows = -1;

    /**
     * If <code>true</code>, the panel will show group labels.
     */
    private boolean toShowGroupLabels = true;

    /**
     * Layout kind of the panel.
     */
    private LayoutKind layoutKind = LayoutKind.ROW_FILL;

    private CommandButtonPresentationState commandPresentationState;

    private Integer commandIconDimension;

    private int commandHorizontalAlignment;
    private boolean isMenu;
    private CommandButtonPresentationModel.PopupOrientationKind popupOrientationKind;

    /**
     * Enumerates the available layout kinds.
     *
     * @author Kirill Grouchnikov
     */
    public enum LayoutKind {
        /**
         * The buttons are laid out in rows respecting the available width.
         */
        ROW_FILL,

        /**
         * The buttons are laid out in columns respecting the available height.
         */
        COLUMN_FILL
    }

    private CommandPanelPresentationModel() {
        this.weakChangeSupport = new WeakChangeSupport(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public CommandButtonPresentationState getCommandPresentationState() {
        return commandPresentationState;
    }

    public void setCommandPresentationState(CommandButtonPresentationState commandPresentationState) {
        if (this.commandPresentationState != commandPresentationState) {
            this.commandPresentationState = commandPresentationState;
            if (this.commandPresentationState != CommandButtonPresentationState.FIT_TO_ICON) {
                this.commandIconDimension = -1;
            }
            this.fireStateChanged();
        }
    }

    public Integer getCommandIconDimension() {
        return commandIconDimension;
    }

    public void setCommandIconDimension(Integer commandIconDimension) {
        if (this.commandIconDimension != commandIconDimension) {
            this.commandIconDimension = commandIconDimension;
            if (this.commandIconDimension != -1) {
                this.commandPresentationState = CommandButtonPresentationState.FIT_TO_ICON;
            }
            this.fireStateChanged();
        }
    }

    public int getMaxColumns() {
        return this.maxColumns;
    }

    public void setMaxColumns(int maxColumns) {
        if (this.maxColumns != maxColumns) {
            this.maxColumns = maxColumns;
            this.fireStateChanged();
        }
    }

    public int getMaxRows() {
        return this.maxRows;
    }

    public void setMaxRows(int maxRows) {
        if (this.maxRows != maxRows) {
            this.maxRows = maxRows;
            this.fireStateChanged();
        }
    }

    public LayoutKind getLayoutKind() {
        return this.layoutKind;
    }

    public void setLayoutKind(LayoutKind layoutKind) {
        if (layoutKind == null) {
            throw new IllegalArgumentException("Layout kind cannot be null");
        }
        if ((layoutKind == LayoutKind.COLUMN_FILL)
                && this.isToShowGroupLabels()) {
            throw new IllegalArgumentException(
                    "Column fill layout is not supported when group labels are shown");
        }
        if (this.layoutKind != layoutKind) {
            this.layoutKind = layoutKind;
            this.fireStateChanged();
        }
    }

    public boolean isToShowGroupLabels() {
        return this.toShowGroupLabels;
    }

    public void setToShowGroupLabels(boolean toShowGroupLabels) {
        if ((this.getLayoutKind() == LayoutKind.COLUMN_FILL)
                && toShowGroupLabels) {
            throw new IllegalArgumentException(
                    "Column fill layout is not supported when group labels are shown");
        }
        if (this.toShowGroupLabels != toShowGroupLabels) {
            this.toShowGroupLabels = toShowGroupLabels;
            this.fireStateChanged();
        }
    }

    public int getCommandHorizontalAlignment() {
        return this.commandHorizontalAlignment;
    }

    public boolean isMenu() {
        return this.isMenu;
    }

    public CommandButtonPresentationModel.PopupOrientationKind getPopupOrientationKind() {
        return this.popupOrientationKind;
    }

    public void addChangeListener(ChangeListener l) {
        this.weakChangeSupport.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        this.weakChangeSupport.removeChangeListener(l);
    }

    private void fireStateChanged() {
        this.weakChangeSupport.fireStateChanged();
    }

    public static class Builder {
        private int maxColumns = -1;
        private int maxRows = -1;
        private boolean toShowGroupLabels = true;
        private LayoutKind layoutKind = LayoutKind.ROW_FILL;
        private CommandButtonPresentationState commandPresentationState;
        private Integer commandIconDimension = -1;
        private int commandHorizontalAlignment = JCommandButton.DEFAULT_HORIZONTAL_ALIGNMENT;
        private boolean isMenu = false;
        private CommandButtonPresentationModel.PopupOrientationKind popupOrientationKind =
                CommandButtonPresentationModel.PopupOrientationKind.DOWNWARD;

        public Builder setMaxColumns(int maxColumns) {
            this.maxColumns = maxColumns;
            return this;
        }

        public Builder setMaxRows(int maxRows) {
            this.maxRows = maxRows;
            return this;
        }

        public Builder setToShowGroupLabels(boolean toShowGroupLabels) {
            this.toShowGroupLabels = toShowGroupLabels;
            return this;
        }

        public Builder setLayoutKind(LayoutKind layoutKind) {
            this.layoutKind = layoutKind;
            return this;
        }

        public Builder setCommandPresentationState(
                CommandButtonPresentationState commandPresentationState) {
            this.commandPresentationState = commandPresentationState;
            return this;
        }

        public Builder setCommandIconDimension(Integer commandIconDimension) {
            this.commandIconDimension = commandIconDimension;
            return this;
        }

        public Builder setCommandHorizontalAlignment(int commandHorizontalAlignment) {
            this.commandHorizontalAlignment = commandHorizontalAlignment;
            return this;
        }

        public Builder setPopupOrientationKind(
                CommandButtonPresentationModel.PopupOrientationKind popupOrientationKind) {
            this.popupOrientationKind = popupOrientationKind;
            return this;
        }

        public Builder setMenu(boolean isMenu) {
            this.isMenu = isMenu;
            return this;
        }

        public CommandPanelPresentationModel build() {
            CommandPanelPresentationModel presentationModel = new CommandPanelPresentationModel();
            presentationModel.maxColumns = this.maxColumns;
            presentationModel.maxRows = this.maxRows;
            presentationModel.layoutKind = this.layoutKind;
            presentationModel.toShowGroupLabels = this.toShowGroupLabels;
            presentationModel.commandIconDimension = this.commandIconDimension;
            presentationModel.commandPresentationState = this.commandPresentationState;
            presentationModel.commandHorizontalAlignment = this.commandHorizontalAlignment;
            presentationModel.isMenu = this.isMenu;
            presentationModel.popupOrientationKind = this.popupOrientationKind;
            return presentationModel;
        }
    }
}
