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

import java.io.*;

/**
 *
 */
class Communicator implements Closeable {
    private final BufferedReader in;
    private final BufferedWriter out;

    public Communicator(InputStream in, OutputStream out) throws UnsupportedEncodingException {
        this.in = new BufferedReader(new InputStreamReader(in, "utf-8"));
        this.out = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
    }

    public int readLen() throws IOException {
        final String line = in.readLine();
        return line == null ? 0 : Integer.parseInt(line);
    }

    public String readStatus() throws IOException {
        return in.readLine();
    }

    public String readContent(int len) throws IOException {
        final char[] buf = new char[len];
        in.read(buf);
        return new String(buf);
    }

    public void writeStatus(String status) throws IOException {
        out.write(status);
        out.newLine();
    }

    public void writeLen(int len) throws IOException {
        out.write("" + len);
        out.newLine();
        out.flush();
    }

    public void writeContent(String content) throws IOException {
        writeLen(content.length());
        out.write(content);
        out.newLine();
        out.flush();
    }

    @Override
    public void close() {
        try {
            in.close();
        } catch (IOException e) {
        }
        try {
            out.close();
        } catch (IOException e) {
        }
    }
}
