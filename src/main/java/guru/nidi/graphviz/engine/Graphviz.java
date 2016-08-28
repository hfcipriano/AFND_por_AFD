/*
 * Copyright (C) 2015 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.graphviz.engine;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Serializer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;

/**
 *
 */
public class Graphviz {
    private static GraphvizEngine engine;
    private final String dot;
    private final String format;
    private final int targetWidth, targetHeight;

    private Graphviz(String dot, String format, int targetWidth, int targetHeight) {
        this.dot = dot;
        this.format = format;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
    }

    public static void useEngine(GraphvizEngine engine) {
        Graphviz.engine = engine;
    }

    public static void initEngine() {
        engine = new GraphvizV8Engine(e ->
                engine = new GraphvizServerEngine(e1 ->
                        engine = new GraphvizJdkEngine()));
    }

    public static void releaseEngine() {
        if (engine != null) {
            engine.release();
        }
    }

    public static Graphviz fromString(String dot) {
        return new Graphviz(dot, null, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public static Graphviz fromFile(File dot) throws IOException {
        try (final InputStream in = new FileInputStream(dot)) {
            return fromString(IoUtils.readStream(in));
        }
    }

    public static Graphviz fromGraph(Graph graph) {
        return fromString(new Serializer(graph).serialize());
    }

    public Graphviz targetSize(int targetWidth, int targetHeight) {
        return new Graphviz(dot, format, targetWidth, targetHeight);
    }

    public Graphviz format(String format) {
        return new Graphviz(dot, format, targetWidth, targetHeight);
    }

    public String createSvg() {
        if (engine == null) {
            initEngine();
        }
        return engine.execute(dot);
    }

    public void renderToGraphics(Graphics2D graphics) {
        renderToGraphics(createDiagram(), graphics);
    }

    public void renderToFile(File output) {
        final SVGDiagram diagram = createDiagram();
        double scale = Math.min(targetWidth / diagram.getWidth(), targetHeight / diagram.getHeight());
        if (scale > 1000) {
            scale = 1;
        }
        final BufferedImage img = new BufferedImage((int) Math.ceil(scale * diagram.getWidth()), (int) Math.ceil(scale * diagram.getHeight()), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = img.createGraphics();
        if (scale != 1) {
            g.scale(scale, scale);
        }
        renderToGraphics(diagram, g);
        final String f = format == null
                ? output.getName().substring(output.getName().lastIndexOf('.') + 1)
                : format;
        writeToFile(output, f, img);
    }

    private void writeToFile(File output, String format, BufferedImage img) {
        try {
            ImageIO.write(img, format, output);
        } catch (IOException e) {
            throw new GraphvizException("Problem writing to file", e);
        }
    }

    private void renderToGraphics(SVGDiagram diagram, Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        try {
            diagram.render(graphics);
        } catch (SVGException e) {
            throw new GraphvizException("Problem rendering SVG", e);
        }
    }

    private SVGDiagram createDiagram() {
        final SVGUniverse universe = new SVGUniverse();
        final URI uri = universe.loadSVG(new StringReader(createSvg()), "graph");
        final SVGDiagram diagram = universe.getDiagram(uri);
        diagram.setIgnoringClipHeuristic(true);
        return diagram;
    }
}
