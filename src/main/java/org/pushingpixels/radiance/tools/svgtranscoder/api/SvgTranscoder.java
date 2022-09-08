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
package org.pushingpixels.radiance.tools.svgtranscoder.api;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SVG to Java2D transcoder.
 *
 * @author Kirill Grouchnikov.
 */
public class SvgTranscoder extends SvgBaseTranscoder {
    /**
     * URI of the SVG image.
     */
    protected String uri;

    /**
     * Creates a new transcoder.
     *
     * @param uri              URI of the SVG image.
     * @param classname        Classname for the generated Java2D code.
     * @param languageRenderer Language renderer for the generated Java2D code.
     */
    public SvgTranscoder(String uri, String classname, LanguageRenderer languageRenderer) {
        super(classname, languageRenderer);
        this.uri = uri;
    }

    /**
     * Transcodes the SVG image into Java2D code. Does nothing if the
     * {@link #listener} is <code>null</code>.
     *
     * @param templateStream Stream with the template content
     */
    public Document transcode(InputStream templateStream) {
        if (this.listener == null)
            return null;

/* see internals.md

Transcoding has three parts:

- traversing the Batik tree
- parsing out the information to create matching Java2D instructions
- printing those instructions in Java / Kotlin syntax

### Traversing the Batik tree

The 'GraphicsNode' that we get from Batik's `GVTBuilder` is the root of a 
hierarchical representation of the original SVG content. 
Here it is important to note that it is *not* the DOM tree of the original SVG document - 
some of the SVG elements such as `<style>` or `<defs>` might be in a different place 
in the `GraphicsNode` hierarchy - "attached" to the specific elements that use them.

The traversal itself looks at two main types of nodes - `GraphicsNode` and `Paint`. 
Each supported sub-class of these two classes is transcoded in its own method. 
For example, `PatternPaint` is transcoded by the `transcodePatternPaint` 
and `TextNode` is transcoded by `transcodeTextNode`.

The traversal is depth-first, visiting every supported node in the tree, querying the information to create Java2D instructions (next section) and traversing children nodes where appropriate.

Important note - there is special handling of raster image, text and pattern content. See addendum sections in this document for more details.
        
        
 */
        
        UserAgentAdapter ua = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(ua);

        BridgeContext batikBridgeContext = new BridgeContext(ua, loader);
        batikBridgeContext.setDynamicState(BridgeContext.DYNAMIC);
        ua.setBridgeContext(batikBridgeContext);

        GVTBuilder builder = new GVTBuilder();
        Document svgDoc;
        try {
            svgDoc = loader.loadDocument(this.uri);
            GraphicsNode gvtRoot = builder.build(batikBridgeContext, svgDoc);

            this.transcode(gvtRoot, templateStream);
            return svgDoc;
        } catch (IOException ex) {
            Logger.getLogger(SvgTranscoder.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                loader.dispose();
                batikBridgeContext.dispose();
            } catch (Throwable t) {
                Logger.getLogger(SvgTranscoder.class.getName()).log(Level.SEVERE, null, t);
            }
        }
    }
}
