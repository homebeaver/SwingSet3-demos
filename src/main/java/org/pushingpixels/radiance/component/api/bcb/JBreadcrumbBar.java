/*
 * Copyright (c) 2003-2021 Radiance Kirill Grouchnikov
 * and <a href="http://www.topologi.com">Topologi</a>.
 * Contributed by <b>Rick Jelliffe</b> of <b>Topologi</b>
 * in January 2006.
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
package org.pushingpixels.radiance.component.api.bcb;

import org.pushingpixels.radiance.component.internal.theming.bcb.ui.RadianceBreadcrumbBarUI;
import org.pushingpixels.radiance.component.internal.ui.bcb.BreadcrumbBarUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Breadcrumb bar. It is basically a way of lazily navigating around a tree, but
 * just by manipulating the sections of a path.
 *
 * @param <T> Type of data associated with each breadcrumb bar item.
 * @author Kirill Grouchnikov
 */
public class JBreadcrumbBar<T> extends JComponent {
    /**
     * The breadcrumb bar model.
     */
    protected BreadcrumbBarModel<T> model;

    /**
     * Application callback. Used to retrieve choices for the activated
     * selector.
     */
    protected BreadcrumbBarCallBack<T> callback;

    /**
     * List of registered exception handlers.
     */
    private List<BreadcrumbBarExceptionHandler> exceptionHandlers;

    /**
     * The UI class ID string.
     */
    public static final String uiClassID = "BreadcrumbBarUI";

    /**
     * Creates a new breadcrumb bar.
     *
     * @param callback The application callback.
     */
    public JBreadcrumbBar(final BreadcrumbBarCallBack<T> callback) {
        super();
        this.model = new BreadcrumbBarModel<T>();
        this.callback = callback;

        if (this.callback != null) {
            this.callback.setup();
        }

        this.exceptionHandlers = new ArrayList<>();

        this.updateUI();
    }

    /**
     * Sets new path as the current path in <code>this</code> breadcrumb bar.
     *
     * @param newPath New path for <code>this</code> breadcrumb bar.
     */
    public void setPath(List<BreadcrumbItem<T>> newPath) {
        this.getModel().replace(newPath);
    }

    /**
     * Returns the application callback.
     *
     * @return The application callback.
     */
    public BreadcrumbBarCallBack<T> getCallback() {
        return this.callback;
    }

    /**
     * Sets the new UI delegate.
     *
     * @param ui New UI delegate.
     */
    public void setUI(BreadcrumbBarUI ui) {
        super.setUI(ui);
    }

    @Override
    public void updateUI() {
        setUI(RadianceBreadcrumbBarUI.createUI(this));
    }

    /**
     * Returns the UI delegate for this component.
     *
     * @return a <code>BreadcrumbBarUI</code> object
     * @see #setUI
     */
    public BreadcrumbBarUI getUI() {
        return (BreadcrumbBarUI) ui;
    }

    /**
     * Returns the name of the UI delegate for this component.
     *
     * @return the string "BreadcrumbBarUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    /**
     * Registers the specified exception handler.
     *
     * @param handler Exception handler.
     */
    public void addExceptionHandler(BreadcrumbBarExceptionHandler handler) {
        this.exceptionHandlers.add(handler);
    }

    /**
     * Unregisters the specified exception handler.
     *
     * @param handler Exception handler.
     */
    public void removeExceptionHandler(BreadcrumbBarExceptionHandler handler) {
        this.exceptionHandlers.remove(handler);
    }

    /**
     * Returns the list of currently registered exception handlers.
     *
     * @return List of currently registered exception handlers.
     */
    public List<BreadcrumbBarExceptionHandler> getExceptionHandlers() {
        return Collections.unmodifiableList(this.exceptionHandlers);
    }

    /**
     * Sets the indication whether the operations of this breadcrumb bar will
     * throw {@link BreadcrumbBarException}.
     *
     * @param throwsExceptions If <code>true</code>, the operations of this breadcrumb bar
     *                         will throw {@link BreadcrumbBarException}.
     */
    public void setThrowsExceptions(boolean throwsExceptions) {
        if (this.callback != null) {
            this.callback.setThrowsExceptions(throwsExceptions);
        }
    }

    /**
     * Returns the model of this breadcrumb bar.
     *
     * @return The model of this breadcrumb bar.
     */
    public BreadcrumbBarModel<T> getModel() {
        return this.model;
    }
}