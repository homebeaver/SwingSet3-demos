package org.jdesktop.swingx.demos.svg;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.processing.Processor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXFrame.StartPosition;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.icon.SizingConstants;
import org.jdesktop.swingx.icon.ArrowIcon;
import org.jdesktop.swingx.icon.RadianceIcon;
import org.pushingpixels.radiance.tools.svgtranscoder.api.LanguageRenderer;
import org.pushingpixels.radiance.tools.svgtranscoder.api.SvgTranscoder;
import org.pushingpixels.radiance.tools.svgtranscoder.api.java.JavaLanguageRenderer;

import swingset.AbstractDemo;

public class SvgTranscoderDemo extends AbstractDemo {

	private static final long serialVersionUID = 5491188431493759568L;
	private static final Logger LOG = Logger.getLogger(SvgTranscoderDemo.class.getName());
	private static final String DESCRIPTION = "Demonstrates ... .";

    /**
     * main method allows us to run as a standalone demo.
     */
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {
    		static final boolean exitOnClose = true;
			@Override
			public void run() {
				JXFrame controller = new JXFrame("controller", exitOnClose);
				AbstractDemo demo = new SvgTranscoderDemo(controller);
				JXFrame frame = new JXFrame(DESCRIPTION, exitOnClose);
				frame.setStartPosition(StartPosition.CenterInScreen);
            	frame.getContentPane().add(demo);
            	frame.pack();
            	frame.setVisible(true);
				
				controller.getContentPane().add(demo.getControlPane());
				controller.pack();
				controller.setVisible(true);
			}		
    	});
    }

    private JTextArea textArea;

    /**
     * Constructor
     * @param frame controller Frame frame.title will be set
     */
    public SvgTranscoderDemo(Frame frame) {
    	super(new BorderLayout());
    	frame.setTitle(getBundleString("frame.title", DESCRIPTION));
    	super.setPreferredSize(PREFERRED_SIZE);
    	super.setBorder(new BevelBorder(BevelBorder.LOWERED));

    	textArea = new JTextArea();
		textArea.setColumns(20);
		textArea.setLineWrap(true);
		textArea.setRows(5);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
//		jScrollPane1 = new JScrollPane(textArea);
    	add(textArea);
    	
//    	JPanel buttonPanel = new JPanel(new BoxLayout());
        JPanel panel = new JXPanel(new GridLayout(0, 6, 1, 1)); // zero meaning any number of rows
        add(panel, BorderLayout.NORTH);
        RadianceIcon duke = Duke.factory().createNewIcon();
        duke.setDimension(new Dimension(SizingConstants.BUTTON_ICON, SizingConstants.BUTTON_ICON));
    	panel.add(new JButton("Duke", duke));
    	Icon activity = //new IconTactivity();
    			IconRactivity.factory().createNewIcon();
    	JButton activityButton = new JButton("activity", activity);
    	panel.add(activityButton);
    	panel.add(new JButton("airplay", IconRairplay.factory().createNewIcon()));
//    	panel.add(new JButton("alert_circle", IconRalert_circle.factory().createNewIcon()));
    	panel.add(new JButton("alert_octagon", IconRalert_octagon.factory().createNewIcon()));
    	panel.add(new JButton("alert_triangle", IconRalert_triangle.factory().createNewIcon()));
    	panel.add(new JButton("align_center", IconRalign_center.factory().createNewIcon()));
    	panel.add(new JButton("align_justify", IconRalign_justify.factory().createNewIcon()));
    	panel.add(new JButton("align_left", IconRalign_left.factory().createNewIcon()));
    	panel.add(new JButton("anchor", IconRanchor.factory().createNewIcon()));
    	panel.add(new JButton("aperture", IconRaperture.factory().createNewIcon()));
//    	panel.add(new JButton("archive", IconRarchive.factory().createNewIcon()));
//    	panel.add(new JButton("arrow_down", IconRarrow_down.factory().createNewIcon()));
//    	panel.add(new JButton("arrow_down_circle", IconRarrow_down_circle.factory().createNewIcon()));
//    	panel.add(new JButton("arrow_down_left", IconRarrow_down_left.factory().createNewIcon()));
//    	panel.add(new JButton("arrow_down_right", IconRarrow_down_right.factory().createNewIcon()));
//    	panel.add(new JButton("arrow_left", IconRarrow_left.factory().createNewIcon()));
//    	panel.add(new JButton("arrow_right", IconRarrow_right.factory().createNewIcon()));
    	panel.add(new JButton("arrow N", ArrowIcon.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON)));
    	RadianceIcon ne = ArrowIcon.of(RadianceIcon.ACTION_ICON, RadianceIcon.ACTION_ICON);
    	ne.setRotation(RadianceIcon.NORTH_EAST);
    	panel.add(new JButton("arrow NE", ne));
//    	IconRarrowXXX east = (IconRarrowXXX)IconRarrowXXX.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON);
//    	east.setDirection(SizingConstants.EAST);
//    	panel.add(new JButton("arrow E", east));
//    	IconRarrowXXX se = (IconRarrowXXX)IconRarrowXXX.of(SizingConstants.ACTION_ICON, SizingConstants.ACTION_ICON);
//    	se.setDirection(SizingConstants.SOUTH_EAST);
//    	panel.add(new JButton("arrow SE", se));
//    	panel.add(new JButton("XS award",  IconRaward.of(SizingConstants.XS, SizingConstants.XS)));

        RadianceIcon duke_waving = Duke_waving.factory().createNewIcon();
        duke_waving.setDimension(new Dimension(SizingConstants.LAUNCHER_ICON, SizingConstants.LAUNCHER_ICON));
    	add(new JButton("Duke_waving", duke_waving), BorderLayout.SOUTH);

        InputStream in = getClass().getResourceAsStream("resources/Duke.svg");
        try {
        	LOG.info("read content.txt");
            textArea.read(new InputStreamReader(in), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        /**
         * Creates a new transcoder.
         *
         * @param uri              URI of the SVG image.
         * @param classname        Classname for the generated Java2D code.
         * @param languageRenderer Language renderer for the generated Java2D code.
         */
        LanguageRenderer lr = new JavaLanguageRenderer();
    	LOG.info("LanguageRenderer:"+lr);
        SvgTranscoder st = new SvgTranscoder("resources/activity.svg", "SVGIcon", lr);
//        InputStream templateStream
        st.transcode(in);
        
        String svgName = "at_sign"; // ohne fileType
        String baseName = "IconR"+svgName;
        String className = "org.jdesktop.swingx.demos.svg."+baseName;
//        Class<?> test = dynCompile(getClass().getResourceAsStream("resources/IconRat_sign.java"));
        Class<?> test = dynCompile(getClass().getResourceAsStream("resources/"+baseName+".java"), className);
    	LOG.info("Hurra und jetzt ...");
		try {
			Constructor<?> ctor = test.getDeclaredConstructor();
			ctor.setAccessible(true); // ist private
			Object o = ctor.newInstance();
	    	panel.add(new JButton(svgName, (RadianceIcon)o));
		} catch (IllegalArgumentException | SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        try {
//			Object instance = test.getDeclaredConstructor().newInstance();
//			
//			// geht auch so:
//        	Supplier interfaceType = (Supplier)test.getDeclaredConstructor().newInstance();
//        	LOG.info("Hurra");
//			
//			if(instance instanceof Supplier s) {
//				LOG.info("Hurra und jetzt ...");
//				LOG.info("get() ..."+s.get()); // funktioniert mit Hello World!
//			}
//		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
//				| NoSuchMethodException | SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

    }

/*

// Compiling source 
File root = new File("scripts");
File sourceFile = new File(root, "Test.java");
JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
compiler.run(null, null, null, sourceFile.getPath());

 */
    private Class<?> dynCompile(InputStream inSrc, String className) {
//        InputStream inSrc = getClass().getResourceAsStream("resources/IconRaward.java");
//        inSrc.readAllBytes(); // byte[]
    	JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    	// https://dzone.com/articles/how-to-compile-a-class-at-runtime-with-java-8-and
    	if (compiler == null)
            throw new RuntimeException("No compiler was provided by ToolProvider.getSystemJavaCompiler()." +
            		"Make sure the jdk.compiler module is available.");
    	ClassFileManager fileManager = new ClassFileManager(
    		compiler.getStandardFileManager(null, null, null)
    	);
    	Lookup lookup = MethodHandles.lookup();
    	ClassLoader cl = lookup.lookupClass().getClassLoader();
    	List<CharSequenceJavaFileObject> files = new ArrayList<>();
//    	String className = "com.example.CompileTest";
//    	String content = "package com.example;\n" +
//    			"public class CompileTest implements java.util.function.Supplier<String> {\n" +
//    			"  public String get() {\n" +
//    			"    return \"Hello World!\";\n" +
//    			"  }\n" +
//    			"}\n";
    	LOG.info("compile className "+className);
    	String content = new BufferedReader(
    		      new InputStreamReader(inSrc, StandardCharsets.UTF_8))
    		        .lines()
    		        .collect(Collectors.joining("\n"));
    	LOG.info("content : "+content);
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
            	LOG.info("expectResult == false ==>return null");
            	return null;
            }
            throw new RuntimeException("Compilation error: " + out);
        }
        LOG.info("Compilation OK!!!! fileManager:"+fileManager.toString());
        Class<?> result = null;
        // This works if we have private-access to the interfaces in the class hierarchy
//        if (Reflect.CACHED_LOOKUP_CONSTRUCTOR != null) {
//            result = fileManager.loadAndReturnMainClass(className,
//                (name, bytes) -> Reflect.on(cl).call("defineClass", name, bytes, 0, bytes.length).get());
//        } else {
        /* [java-11] */

        // Lookup.defineClass() has only been introduced in Java 9. 
        // It is required to get private-access to interfaces in the class hierarchy
        
        // This method is called by client code from two levels up the current stack frame
        // We need a private-access lookup from the class in that stack frame in order to get
        // private-access to any local interfaces at that location.
        Class<?> caller = StackWalker
            .getInstance(RETAIN_CLASS_REFERENCE)
            .walk(s -> s
                .skip(2)
                .findFirst()
                .get()
                .getDeclaringClass());
        // If the compiled class is in the same package as the caller class, 
        // then we can use the private-access Lookup of the caller class
        if (className.startsWith(caller.getPackageName() + ".") &&

            // [#74] This heuristic is necessary to prevent classes in subpackages of the caller to be loaded
            //       this way, as subpackages cannot access private content in super packages.
            //       The heuristic will work only with classes that follow standard naming conventions.
            //       A better implementation is difficult at this point.
            Character.isUpperCase(className.charAt(caller.getPackageName().length() + 1))) {
        	LOG.info("compiled class is in the same package as the caller class");
			try {
				Lookup privateLookup = MethodHandles.privateLookupIn(caller, lookup);
				result = fileManager.loadAndReturnMainClass(className,
						(name, bytes) -> privateLookup.defineClass(bytes));
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
	            result = fileManager.loadAndReturnMainClass(className,
	                    (name, bytes) -> c.loadClass(name));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return result;
    	
//    	// run(InputStream in, OutputStream out, OutputStream err, String... arguments)
//    	int rc = compiler.run(inSrc, null, null, null);
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
        final List<String>              options;

        public CompileOptions() {
            this(
                Collections.emptyList(),
                Collections.emptyList()
            );
        }

        private CompileOptions(
            List<? extends Processor> processors,
            List<String> options
        ) {
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

    @Override
	public JXPanel getControlPane() {
		return emptyControlPane();
	}


}
