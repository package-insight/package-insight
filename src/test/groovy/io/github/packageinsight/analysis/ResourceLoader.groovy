package io.github.packageinsight.analysis

import java.nio.charset.StandardCharsets

class ResourceLoader {

    private ResourceLoader() {
    }


    static String[] resourceLines(final String s) {
        resource(s).split(/\n/)
    }

    static String resource(final String s) {
        final InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(s)
        if (inputStream == null) {
            throw new RuntimeException("Did not find resource: " + s)
        }
        try {
            streamToString(inputStream)
        } catch (IOException e) {
            throw new RuntimeException(e)
        }
    }

    private static String streamToString(InputStream inputStream) throws IOException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream()
        final byte[] buffer = new byte[1024]
        int length
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length)
        }
        result.toString(StandardCharsets.UTF_8.name())
    }
}
