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
// original: package org.pushingpixels.radiance.demo.component.svg;
package org.jdesktop.swingx.demos.svg;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.processing.Processor;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.jdesktop.swingx.icon.RadianceIcon;
import org.jdesktop.swingx.image.RadianceCC;
import org.pushingpixels.radiance.component.api.bcb.BreadcrumbBarCallBack;
import org.pushingpixels.radiance.component.api.common.CommandButtonPresentationState;
import org.pushingpixels.radiance.component.api.common.JCommandButtonPanel;
import org.pushingpixels.radiance.component.api.common.RichTooltip;
import org.pushingpixels.radiance.component.api.common.StringValuePair;
import org.pushingpixels.radiance.component.api.common.icon.EmptyRadianceIcon;
import org.pushingpixels.radiance.component.api.common.model.Command;
import org.pushingpixels.radiance.component.api.common.model.CommandGroup;
import org.pushingpixels.radiance.component.api.common.model.CommandPanelContentModel;
import org.pushingpixels.radiance.component.api.common.model.CommandPanelPresentationModel;
import org.pushingpixels.radiance.component.api.common.projection.CommandPanelProjection;
import org.pushingpixels.radiance.tools.svgtranscoder.api.SvgStreamTranscoder;
import org.pushingpixels.radiance.tools.svgtranscoder.api.java.JavaLanguageRenderer;

/**
 * Panel that hosts SVG-based gallery buttons.
 *
 * @author Kirill Grouchnikov
 * @author EUG https://github.com/homebeaver (transcodeToJava+dynCompile + show the Class)
 */
/*

JCommandButtonPanel that hosts command buttons. 

... it is highly recommended to use the CommandPanelContentModel 
and CommandPanelPresentationModel instances used to project the command button panel on screen for any dynamic manipulation of the state. 

Under the default CommandPanelPresentationModel.LayoutKind.ROW_FILL, (
the buttons are laid out in rows, never exceeding the available horizontal space. 
A vertical scroll bar will kick in once there is not enough vertical space to show all the buttons.
The schematic below shows a row-fill command button panel: 

 +-----------------------------+-+
 |                             |o|
 | +----+ +----+ +----+ +----+ |o|
 | | 01 | | 02 | | 03 | | 04 | |o|
 | +----+ +----+ +----+ +----+ |o|
 |                             | |
 | +----+ +----+ +----+ +----+ | |
 | | 05 | | 06 | | 07 | | 07 | | |
 | +----+ +----+ +----+ +----+ | |
 |                             | |
 | +----+ +----+ +----+ +----+ | |
 | | 09 | | 10 | | 11 | | 12 | | |
 | +----+ +----+ +----+ +----+ | |
 |                             | |
 | +----+ +----+ +----+ +----+ | |
 | | 13 | | 14 | | 15 | | 16 | | |
 +-----------------------------+-+
 

Each row hosts four buttons, and the vertical scroll bar allows scrolling the content up and down.

 */
@SuppressWarnings("serial")
public class SvgFileViewPanel extends JCommandButtonPanel {

	private static final Logger LOG = Logger.getLogger(SvgFileViewPanel.class.getName());
	private static final String TRANSCODER_TEMPLATE = "SvgTranscoderTemplateRadiance.templ";
	/**
     * Callback into the underlying breadcrumb bar.
     */
    private BreadcrumbBarCallBack<File> callback;

    /**
     * The main worker that loads the SVG images off EDT.
     */
    private SwingWorker<Void, StringValuePair<InputStream>> mainWorker;

    /**
     * Creates a new panel.
     *
     * @param callback          Callback into the underlying breadcrumb bar.
     * @param startingDimension Initial dimension for SVG icons.
     */
    public SvgFileViewPanel(BreadcrumbBarCallBack<File> callback, int startingDimension) {
        super(new CommandPanelProjection(new CommandPanelContentModel(new ArrayList<>()),
                CommandPanelPresentationModel.builder()
                        .setToShowGroupLabels(false)
                        .setLayoutKind(CommandPanelPresentationModel.LayoutKind.ROW_FILL)
                        .setCommandPresentationState(CommandButtonPresentationState.FIT_TO_ICON)
                        .setCommandIconDimension(startingDimension)
                        .build()));

        this.callback = callback;
    }

    private Class<?> transcodeToJava(String name, SvgBatikRadianceIcon svgIcon) throws FileNotFoundException {
        String svgClassName = name.substring(0, name.lastIndexOf('.'));
        svgClassName = svgClassName.replace('-', '_');
        // siehe [#74] unten
        svgClassName = "TMP"+svgClassName.replace(' ', '_');

        String javaClassFilename = System.getProperty("java.io.tmpdir")
                + File.separator + svgClassName + ".java";
        LOG.info("transcode "+name+" to "+javaClassFilename);
        PrintWriter pw = new PrintWriter(javaClassFilename); // throws FileNotFoundException

        SvgStreamTranscoder transcoder = new SvgStreamTranscoder(
                new ByteArrayInputStream(svgIcon.getSvgBytes()), svgClassName,
                new JavaLanguageRenderer());
        String packageName = "org.jdesktop.swingx.demos.svg";
        transcoder.setPackageName(packageName);
        transcoder.setPrintWriter(pw);
        transcoder.transcode(this.getClass().getResourceAsStream(
                "/org/pushingpixels/radiance/tools/svgtranscoder/api/java/" + TRANSCODER_TEMPLATE));
        JOptionPane.showMessageDialog(
                SwingUtilities.getWindowAncestor(SvgFileViewPanel.this),
                "Finished with '" + javaClassFilename + "'");
    	// der SvgStreamTranscoder kann nur in PrintWriter schreiben, also müssen wir vom file lesen
        // TODO in stream schreiben als pipe lesen
        String className = packageName+"."+svgClassName;
        File inFile = new File(javaClassFilename);
        InputStream inStream = new FileInputStream(inFile);
        Class<?> compiledClass = dynCompile(inStream, className);
        return compiledClass;
    }
	private Class<?> dynCompile(InputStream inSrc, String className) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		// https://dzone.com/articles/how-to-compile-a-class-at-runtime-with-java-8-and
		if (compiler == null)
			throw new RuntimeException("No compiler was provided by ToolProvider.getSystemJavaCompiler()."
					+ "Make sure the jdk.compiler module is available.");
		ClassFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(null, null, null));
		Lookup lookup = MethodHandles.lookup();
		ClassLoader cl = lookup.lookupClass().getClassLoader();
		List<CharSequenceJavaFileObject> files = new ArrayList<>();
		LOG.info("compile className " + className);
		
		String content = new BufferedReader(new InputStreamReader(inSrc, StandardCharsets.UTF_8))
				.lines()
				.collect(Collectors.joining("\n"));
		LOG.info("content : " + content);
		files.add(new CharSequenceJavaFileObject(className, content));
		
		StringWriter out = new StringWriter();
		CompileOptions compileOptions = new CompileOptions();
		List<String> options = new ArrayList<>(compileOptions.options);
		if (!options.contains("-classpath")) {
			StringBuilder classpath = new StringBuilder();
			String separator = System.getProperty("path.separator");
			String cp = System.getProperty("java.class.path");
			String mp = System.getProperty("jdk.module.path");

			if (cp != null && !"".equals(cp))
				classpath.append(cp);
			if (mp != null && !"".equals(mp))
				classpath.append(mp);

			if (cl instanceof URLClassLoader) {
				for (URL url : ((URLClassLoader) cl).getURLs()) {
					if (classpath.length() > 0)
						classpath.append(separator);

					if ("file".equals(url.getProtocol()))
						try {
							classpath.append(new File(url.toURI()));
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			}

			options.addAll(Arrays.asList("-classpath", classpath.toString()));
		}
	
		CompilationTask task = compiler.getTask(out, fileManager, null, options, null, files);
		if (!compileOptions.processors.isEmpty())
			task.setProcessors(compileOptions.processors);
		task.call();
		boolean expectResult = true;
		if (fileManager.isEmpty()) {
			if (!expectResult) {
				LOG.info("expectResult == false ==> return null");
				return null;
			}
			throw new RuntimeException("Compilation error: " + out);
		}
		LOG.info("Compilation OK fileManager:" + fileManager.toString());

		Class<?> result = null;
		Class<?> caller = StackWalker
				.getInstance(RETAIN_CLASS_REFERENCE)
				.walk(s -> s
						.skip(2)
						.findFirst()
						.get()
						.getDeclaringClass());

		// If the compiled class is in the same package as the caller class,
		// then we can use the private-access Lookup of the caller class
		if (className.startsWith(caller.getPackageName() + ".")
	            // [#74] This heuristic is necessary to prevent classes in subpackages of the caller to be loaded
	            //       this way, as subpackages cannot access private content in super packages.
	            //       The heuristic will work only with classes that follow standard naming conventions.
	            //       A better implementation is difficult at this point.
				&& Character.isUpperCase(className.charAt(caller.getPackageName().length() + 1))) {
			LOG.info("compiled class is in the same package as the caller class");
			try {
				Lookup privateLookup = MethodHandles.privateLookupIn(caller, lookup);
				result = fileManager.loadAndReturnMainClass(className, 
						(name, bytes) -> privateLookup.defineClass(bytes)
					);
				LOG.info("result class:"+result);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Otherwise, use an arbitrary class loader. This approach doesn't allow for
		// loading private-access interfaces in the compiled class's type hierarchy
		else {
			LOG.info("compiled class is in different package as the caller class");
			ByteArrayClassLoader c = new ByteArrayClassLoader(fileManager.classes());
			try {
				result = fileManager.loadAndReturnMainClass(className, (name, bytes) -> c.loadClass(name));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	// aus https://github.com/jOOQ/jOOR
	static final class ByteArrayClassLoader extends ClassLoader {
		private final Map<String, byte[]> classes;

		ByteArrayClassLoader(Map<String, byte[]> classes) {
			super(ByteArrayClassLoader.class.getClassLoader());
			this.classes = classes;
		}

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			byte[] bytes = classes.get(name);
			if (bytes == null)
				return super.findClass(name);
			else
				return defineClass(name, bytes, 0, bytes.length);
		}
	}

    static final class JavaFileObject extends SimpleJavaFileObject {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        JavaFileObject(String name, JavaFileObject.Kind kind) {
            super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
        }

        byte[] getBytes() {
            return os.toByteArray();
        }

        @Override
        public OutputStream openOutputStream() {
            return os;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return new String(os.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    @FunctionalInterface
    interface ThrowingBiFunction<T, U, R> {
        R apply(T t, U u) throws Exception;
    }

    static final class CharSequenceJavaFileObject extends SimpleJavaFileObject {
        final CharSequence content;

        public CharSequenceJavaFileObject(String className, CharSequence content) {
            super(URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
            this.content = content;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return content;
        }
    }

    static final class ClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {
        private final Map<String, JavaFileObject> fileObjectMap;
        private Map<String, byte[]> classes;

        ClassFileManager(StandardJavaFileManager standardManager) {
            super(standardManager);

            fileObjectMap = new LinkedHashMap<>();
        }

        @Override
        public JavaFileObject getJavaFileForOutput(
            JavaFileManager.Location location,
            String className,
            JavaFileObject.Kind kind,
            FileObject sibling
        ) {
            JavaFileObject result = new JavaFileObject(className, kind);
            fileObjectMap.put(className, result);
            return result;
        }

        boolean isEmpty() {
            return fileObjectMap.isEmpty();
        }

        Map<String, byte[]> classes() {
            if (classes == null) {
                classes = new LinkedHashMap<>();

                for (Entry<String, JavaFileObject> entry : fileObjectMap.entrySet())
                    classes.put(entry.getKey(), entry.getValue().getBytes());
            }

            return classes;
        }

        Class<?> loadAndReturnMainClass(String mainClassName, ThrowingBiFunction<String, byte[], Class<?>> definer) throws Exception {
            Class<?> result = null;

            // [#117] We don't know the subclass hierarchy of the top level
            //        classes in the compilation unit, and we can't find out
            //        without either:
            //
            //        - class loading them (which fails due to NoClassDefFoundError)
            //        - using a library like ASM (which is a big and painful dependency)
            //
            //        Simple workaround: try until it works, in O(n^2), where n
            //        can be reasonably expected to be small.
            Deque<Entry<String, byte[]>> queue = new ArrayDeque<>(classes().entrySet());
            int n1 = queue.size();

            // Try at most n times
            for (int i1 = 0; i1 < n1 && !queue.isEmpty(); i1++) {
                int n2 = queue.size();

                for (int i2 = 0; i2 < n2; i2++) {
                    Entry<String, byte[]> entry = queue.pop();

                    try {
                        Class<?> c = definer.apply(entry.getKey(), entry.getValue());

                        if (mainClassName.equals(entry.getKey()))
                            result = c;
                    }
                    // public class ReflectException extends RuntimeException {
                    catch (RuntimeException e) {
                        queue.offer(entry);
                    }
                }
            }

            return result;
        }
    }
    
	public final class CompileOptions {

		final List<? extends Processor> processors;
		final List<String> options;

		public CompileOptions() {
			this(Collections.emptyList(), Collections.emptyList());
		}

		private CompileOptions(List<? extends Processor> processors, List<String> options) {
			this.processors = processors;
			this.options = options;
		}

		public final CompileOptions processors(Processor... newProcessors) {
			return processors(Arrays.asList(newProcessors));
		}

		public final CompileOptions processors(List<? extends Processor> newProcessors) {
			return new CompileOptions(newProcessors, options);
		}

		public final CompileOptions options(String... newOptions) {
			return options(Arrays.asList(newOptions));
		}

		public final CompileOptions options(List<String> newOptions) {
			return new CompileOptions(processors, newOptions);
		}

		final boolean hasOption(String opt) {
			for (String option : options)
				if (option.equalsIgnoreCase(opt))
					return true;

			return false;
		}
	}

    /**
     * Sets the current files to show. The current contents of the panel are discarded. The file
     * list is scanned for files ending with <code>.svg</code> or <code>.svgz</code>. For each such
     * file a new {@link Command} with an SVG-based implementation of
     * {@link RadianceIcon} is added to the panel content model.
     *
     * @param leafs Information on the files to show in the panel.
     */
    public void setFolder(final java.util.List<StringValuePair<File>> leafs) {
    	LOG.info("List<StringValuePair<File>> leafs:"+leafs);
        this.getProjection().getContentModel().removeAllCommandGroups();

        List<Command> commands = new ArrayList<>();

        /*

newCommands key : icon name , value : transcode-command / transcode svg to java

die icons zum "icon name"s werden in mainWorker.process gebildet

         */
        final Map<String, Command> newCommands = new HashMap<>();
        for (StringValuePair<File> leaf : leafs) {
            String name = leaf.getKey();
            // key : filename , value : File
//            LOG.info("leaf.Key="+name + ", Value/File:"+leaf.getValue());
            if (!name.endsWith(".svg") && !name.endsWith(".svgz")) {
                continue;
            }

            Command svgCommand = Command.builder()
                    .setText(name)
                    .setIconFactory(EmptyRadianceIcon.factory())
                    .setAction(commandActionEvent -> {
                    	// transcode svg to java and compile it
                        try {
                            RadianceIcon icon = commandActionEvent.getCommand().getIconFactory().createNewIcon();
                            if (!(icon instanceof SvgBatikRadianceIcon)) {
                                return;
                            }
                            SvgBatikRadianceIcon svgIcon = (SvgBatikRadianceIcon) icon;
                            Class<?> compiledIcon = transcodeToJava(name, svgIcon);
                            Method method = compiledIcon.getMethod("factory");
                            RadianceIcon.Factory f = (RadianceIcon.Factory) method.invoke(null);
                            RadianceIcon ri = f.createNewIcon();
                            LOG.info("compiled RadianceIcon.Factory:"+ri);
                            LOG.info("compiled RadianceIcon IconHeight:"+ri.getIconHeight());
                            ri.setDimension(getIconDimension());
                            ri.setRotation(SwingConstants.NORTH_EAST);
                            // Das kolorieren von bunten svg's macht sie monochrom TODO
                            // Radience bietet keine Möglichkeit bunte icons zu erkennen!!!
                            // evtl. svg nach "color:" scannen - geht nicht bei Duke.svg
//                            ri.setColorFilter(color -> Color.RED);
                            Command c = newCommands.get(name);
                            c.setIconFactory(() -> ri); // RadianceIcon createNewIcon()
                            c.setAction(null); // prevent to compile twice
                        } catch (Throwable exc) {
                            exc.printStackTrace();
                        }
                    })
                    .setActionRichTooltip(RichTooltip.builder().setTitle("Transcode")
                            .addDescriptionSection("Click to generate Java2D class").build())
                    .build();

            commands.add(svgCommand);

            newCommands.put(name, svgCommand);
        }

        this.getProjection().getContentModel().addCommandGroup(new CommandGroup(commands));

        mainWorker = new SwingWorker<Void, StringValuePair<InputStream>>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (final StringValuePair<File> leafPair : leafs) {
                    if (isCancelled()) {
                        break;
                    }
                    final String name = leafPair.getKey();
                    if (!name.endsWith(".svg") && !name.endsWith(".svgz")) {
                        continue;
                    }
                    InputStream stream = callback.getLeafContent(leafPair.getValue());
                    StringValuePair<InputStream> pair = new StringValuePair<>(name, stream);
                    publish(pair); // Sends data chunks to the process method. 
                }
                return null;
            }

            @Override
            protected void process(List<StringValuePair<InputStream>> pairs) {
                for (final StringValuePair<InputStream> pair : pairs) {
                    final String name = pair.getKey();
                    InputStream svgStream = pair.getValue();
                    Dimension svgDim = getIconDimension();

                    double scale = RadianceCC.getScaleFactor(SvgFileViewPanel.this);
                    final SvgBatikRadianceIcon svgIcon = name.endsWith(".svg")
                            ? SvgBatikRadianceIcon.getSvgIcon(svgStream, scale, svgDim)
                            : SvgBatikRadianceIcon.getSvgzIcon(svgStream, scale, svgDim);

                    // die IconFactory hat nur eine Methode, daher kann sie so aufgerufen werden:
                    newCommands.get(name).setIconFactory(() -> svgIcon); // RadianceIcon createNewIcon()
                }
            }
        };
        mainWorker.execute();
    }

    /**
     * Changes the current dimension of all displayed icons.
     *
     * @param dimension New dimension for the icons.
     */
    public void setIconDimension(int dimension) {
        this.getProjection().getPresentationModel().setCommandIconDimension(dimension);
    }
    public Dimension getIconDimension() {
        int iconDimension = getProjection().getPresentationModel().getCommandIconDimension();
        return new Dimension(iconDimension, iconDimension);
    }

    /**
     * Cancels the main worker.
     */
    public void cancelMainWorker() {
        if (this.mainWorker == null) {
            return;
        }
        if (this.mainWorker.isDone() || this.mainWorker.isCancelled()) {
            return;
        }
        this.mainWorker.cancel(false);
    }
}
