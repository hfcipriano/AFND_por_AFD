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
package guru.nidi.graphviz.model;

/**
 *
 */
public class NodePoint implements LinkTarget {
    final Node node;
    final String record;
    final Compass compass;

    private NodePoint(Node node, String record, Compass compass) {
        this.node = node;
        this.record = record;
        this.compass = compass;
    }

    public static NodePoint ofLoc(String record) {
        return ofLoc(record, null);
    }

    public static NodePoint ofLoc(Compass compass) {
        return ofLoc(null, compass);
    }

    public static NodePoint ofLoc(String record, Compass compass) {
        return new NodePoint(null, record, compass);
    }

    public static NodePoint of(Node node) {
        return new NodePoint(node, null, null);
    }

    public NodePoint loc(String record) {
        return new NodePoint(node, record, compass);
    }

    public NodePoint loc(Compass compass) {
        return new NodePoint(node, record, compass);
    }

    public NodePoint loc(String record, Compass compass) {
        return new NodePoint(node, record, compass);
    }

    @Override
    public Label getName() {
        return node.label;
    }

    @Override
    public Link linkFrom() {
        return Link.to(this);
    }
}
