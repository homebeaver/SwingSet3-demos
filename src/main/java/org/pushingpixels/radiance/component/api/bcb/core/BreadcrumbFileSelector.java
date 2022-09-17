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
package org.pushingpixels.radiance.component.api.bcb.core;

import org.pushingpixels.radiance.component.api.bcb.BreadcrumbBarCallBack;
import org.pushingpixels.radiance.component.api.bcb.BreadcrumbBarModel;
import org.pushingpixels.radiance.component.api.bcb.BreadcrumbItem;
import org.pushingpixels.radiance.component.api.bcb.JBreadcrumbBar;
import org.pushingpixels.radiance.component.api.common.StringValuePair;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * Breadcrumb bar that allows browsing the local file system.
 *
 * @author Kirill Grouchnikov
 * @author Brian Young
 */
public class BreadcrumbFileSelector extends JBreadcrumbBar<File> {
    /**
     * If <code>true</code>, the path selectors will use native icons.
     */
    private boolean useNativeIcons;

    /**
     * Local file system specific implementation of the
     * {@link BreadcrumbBarCallBack}.
     *
     * @author Kirill Grouchnikov
     */
    private class DirCallback extends BreadcrumbBarCallBack<File> {
        /**
         * File system view.
         */
        private final FileSystemView fsv;

        /**
         * Creates a new callback.
         */
        public DirCallback() {
            this(FileSystemView.getFileSystemView());
        }

        /**
         * Creates a new callback.
         *
         * @param fileSystemView File system view to use.
         */
        public DirCallback(FileSystemView fileSystemView) {
            this.fsv = fileSystemView;
        }

        @Override
        public void setup() {
        }

        @Override
        public List<StringValuePair<File>> getPathChoices(List<BreadcrumbItem<File>> path) {
            synchronized (fsv) {
                if (path == null) {
                    LinkedList<StringValuePair<File>> bRoots = new LinkedList<>();
                    for (File root : fsv.getRoots()) {
                        if (fsv.isHiddenFile(root)) {
                            continue;
                        }
                        String systemName = fsv.getSystemDisplayName(root);
                        if (systemName.length() == 0) {
                            systemName = root.getAbsolutePath();
                        }
                        StringValuePair<File> rootPair = new StringValuePair<>(systemName, root);
                        if (useNativeIcons) {
                            rootPair.set("icon", fsv.getSystemIcon(root));
                        }
                        bRoots.add(rootPair);
                    }
                    return bRoots;
                }
                if (path.size() == 0) {
                    return null;
                }
                File lastInPath = path.get(path.size() - 1).getData();

                if (!lastInPath.exists()) {
                    return new ArrayList<>();
                }
                if (!lastInPath.isDirectory()) {
                    return null;
                }
                LinkedList<StringValuePair<File>> lResult = new LinkedList<>();
                for (File child : lastInPath.listFiles()) {
                    // ignore regular files and hidden directories
                    if (!child.isDirectory()) {
                        continue;
                    }
                    if (fsv.isHiddenFile(child)) {
                        continue;
                    }
                    String childFileName = fsv.getSystemDisplayName(child);
                    if ((childFileName == null) || childFileName.isEmpty()) {
                        childFileName = child.getName();
                    }
                    StringValuePair<File> pair = new StringValuePair<>(childFileName, child);
                    if (useNativeIcons) {
                        pair.set("icon", fsv.getSystemIcon(child));
                    }
                    lResult.add(pair);
                }
                lResult.sort(new Comparator<>() {
                    @Override
                    public int compare(StringValuePair<File> o1, StringValuePair<File> o2) {
                        String key1 = fsv.isFileSystemRoot(o1.getValue()) ?
                                o1.getValue().getAbsolutePath() : o1.getKey();
                        String key2 = fsv.isFileSystemRoot(o2.getValue()) ?
                                o2.getValue().getAbsolutePath() : o2.getKey();
                        return key1.toLowerCase().compareTo(key2.toLowerCase());
                    }

                    @Override
                    public boolean equals(Object obj) {
                        return super.equals(obj);
                    }
                });
                return lResult;
            }
        }

        @Override
        public List<StringValuePair<File>> getLeafs(List<BreadcrumbItem<File>> path) {
            synchronized (fsv) {
                if ((path == null) || (path.size() == 0)) {
                    return null;
                }
                File lastInPath = path.get(path.size() - 1).getData();
                if (!lastInPath.exists()) {
                    return new ArrayList<>();
                }
                if (!lastInPath.isDirectory()) {
                    return null;
                }
                LinkedList<StringValuePair<File>> lResult = new LinkedList<>();
                for (File child : lastInPath.listFiles()) {
                    // ignore directories and hidden directories
                    if (child.isDirectory()) {
                        continue;
                    }
                    if (fsv.isHiddenFile(child)) {
                        continue;
                    }
                    String childFileName = fsv.getSystemDisplayName(child);
                    if ((childFileName == null) || childFileName.isEmpty()) {
                        childFileName = child.getName();
                    }
                    StringValuePair<File> pair = new StringValuePair<>(childFileName, child);
                    if (useNativeIcons) {
                        pair.set("icon", fsv.getSystemIcon(child));
                    }
                    lResult.add(pair);
                }
                lResult.sort(new Comparator<>() {
                    @Override
                    public int compare(StringValuePair<File> o1,
                            StringValuePair<File> o2) {
                        return o1.getKey().toLowerCase().compareTo(
                                o2.getKey().toLowerCase());
                    }

                    @Override
                    public boolean equals(Object obj) {
                        return super.equals(obj);
                    }
                });
                return lResult;
            }
        }

        @Override
        public InputStream getLeafContent(File leaf) {
            try {
                return new FileInputStream(leaf);
            } catch (FileNotFoundException fnfe) {
                return null;
            }
        }
    }

    /**
     * Creates a new breadcrumb bar file selector that uses native icons and the
     * default file system view.
     */
    public BreadcrumbFileSelector() {
        this(true);
    }

    /**
     * Creates a new breadcrumb bar file selector that uses the default file
     * system view.
     *
     * @param useNativeIcons If <code>true</code>, the path selectors will use native
     *                       icons.
     */
    public BreadcrumbFileSelector(boolean useNativeIcons) {
        this(FileSystemView.getFileSystemView(), useNativeIcons);
    }

    /**
     * Creates a new breadcrumb bar file selector.
     *
     * @param fileSystemView File system view.
     * @param useNativeIcons If <code>true</code>, the path selectors will use native
     *                       icons.
     */
    public BreadcrumbFileSelector(FileSystemView fileSystemView, boolean useNativeIcons) {
        super(null);

        this.model = new BreadcrumbBarModel<>();
        this.useNativeIcons = useNativeIcons;
        this.callback = new DirCallback(fileSystemView);
        this.callback.setup();

        this.updateUI();
    }

    /**
     * Sets indication whether the path selectors should use native icons.
     *
     * @param useNativeIcons If <code>true</code>, the path selectors will use native
     *                       icons.
     */
    public void setUseNativeIcons(boolean useNativeIcons) {
        this.useNativeIcons = useNativeIcons;
    }

    /**
     * Sets the selected path based of the specified file. If this file is
     * either <code>null</code> or not a directory, the home directory is
     * selected.
     *
     * @param dir Points to a directory to be selected.
     */
    public void setPath(File dir) {
        final FileSystemView fsv = FileSystemView.getFileSystemView();

        if ((dir == null) || !dir.isDirectory()) {
            dir = fsv.getHomeDirectory();
        }

        ArrayList<BreadcrumbItem<File>> path = new ArrayList<>();
        File parent = dir;
        BreadcrumbItem<File> bci = new BreadcrumbItem<>(fsv.getSystemDisplayName(dir), dir);
        bci.setIcon(fsv.getSystemIcon(dir));
        path.add(bci);
        while (true) {
            parent = fsv.getParentDirectory(parent);
            if (parent == null) {
                break;
            }
            bci = new BreadcrumbItem<>(fsv.getSystemDisplayName(parent), parent);
            bci.setIcon(fsv.getSystemIcon(parent));
            path.add(bci);
        }
        Collections.reverse(path);
        this.setPath(path);
    }
}
