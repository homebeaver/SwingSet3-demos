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
package org.pushingpixels.radiance.component.api.ribbon.model;

import org.pushingpixels.radiance.component.api.common.CommandButtonPresentationState;
import org.pushingpixels.radiance.component.api.common.model.ImmutablePresentationModel;
import org.pushingpixels.radiance.component.api.ribbon.JRibbonBand;

import java.util.Collections;
import java.util.Map;

public class RibbonGalleryPresentationModel implements ImmutablePresentationModel {
    private Map<JRibbonBand.PresentationPriority, Integer> preferredVisibleCommandCounts;

    /**
     * Preferred maximum number of button columns for the popup panel.
     */
    private int preferredPopupMaxCommandColumns;

    /**
     * Preferred maximum number of visible button rows for the popup panel.
     */
    private int preferredPopupMaxVisibleCommandRows;

    private CommandButtonPresentationState commandPresentationState;

    private String expandKeyTip;

    private RibbonGalleryPresentationModel() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<JRibbonBand.PresentationPriority, Integer> getPreferredVisibleCommandCounts() {
        return preferredVisibleCommandCounts;
    }

    public int getPreferredPopupMaxCommandColumns() {
        return preferredPopupMaxCommandColumns;
    }

    public int getPreferredPopupMaxVisibleCommandRows() {
        return preferredPopupMaxVisibleCommandRows;
    }

    public CommandButtonPresentationState getCommandPresentationState() {
        return commandPresentationState;
    }

    public String getExpandKeyTip() {
        return this.expandKeyTip;
    }

    public static class Builder {
        private Map<JRibbonBand.PresentationPriority, Integer> preferredVisibleCommandCounts;
        private int preferredPopupMaxCommandColumns = -1;
        private int preferredPopupMaxVisibleCommandRows = -1;
        private CommandButtonPresentationState commandPresentationState
                = CommandButtonPresentationState.BIG;
        private String expandKeyTip;

        public Builder setPreferredVisibleCommandCounts(
                Map<JRibbonBand.PresentationPriority, Integer> preferredVisibleCommandCounts) {
            this.preferredVisibleCommandCounts = preferredVisibleCommandCounts;
            return this;
        }

        public Builder setPreferredPopupMaxVisibleCommandRows(
                int preferredPopupMaxVisibleCommandRows) {
            this.preferredPopupMaxVisibleCommandRows = preferredPopupMaxVisibleCommandRows;
            return this;
        }

        public Builder setPreferredPopupMaxCommandColumns(
                int preferredPopupMaxCommandColumns) {
            this.preferredPopupMaxCommandColumns = preferredPopupMaxCommandColumns;
            return this;
        }

        public Builder setCommandPresentationState(
                CommandButtonPresentationState commandPresentationState) {
            this.commandPresentationState = commandPresentationState;
            return this;
        }

        public Builder setExpandKeyTip(String expandKeyTip) {
            this.expandKeyTip = expandKeyTip;
            return this;
        }

        public RibbonGalleryPresentationModel build() {
            RibbonGalleryPresentationModel presentationModel = new RibbonGalleryPresentationModel();
            presentationModel.preferredVisibleCommandCounts =
                    (this.preferredVisibleCommandCounts == null) ? null :
                            Collections.unmodifiableMap(this.preferredVisibleCommandCounts);
            presentationModel.preferredPopupMaxVisibleCommandRows =
                    this.preferredPopupMaxVisibleCommandRows;
            presentationModel.preferredPopupMaxCommandColumns =
                    this.preferredPopupMaxCommandColumns;
            presentationModel.commandPresentationState = this.commandPresentationState;
            presentationModel.expandKeyTip = this.expandKeyTip;
            return presentationModel;
        }
    }
}
