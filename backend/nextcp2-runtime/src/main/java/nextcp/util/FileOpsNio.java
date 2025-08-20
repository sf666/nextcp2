package nextcp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Operationen auf Dateien. Dier hier vorstelligen Methoden verwenden alle die auf I/O optimierte NIO Api. 
 */
public class FileOpsNio
{
    private final static Random rand = new Random(System.currentTimeMillis());
    private final static AtomicLong nextVal = new AtomicLong();

    public interface LineReader
    {
        public void lineRead(String cs);
    }

    private static final int BUFSIZE = 0x32000;

    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * Daten werden Fehlertolerant eingelesen! Es wird der default charset "UTF-8" verwendet.
     * 
     * @param filename
     *            Der Dateiname der zu lesenden Datei.
     * @return Die Datei als String
     * @throws IOException
     */
    public static String readFile(String filename) throws IOException
    {
        return readFile(filename, DEFAULT_CHARSET);
    }

    /**
     * Daten werden Fehlertolerant eingelesen!
     * 
     * @param filename
     *            Der Dateiname der zu lesenden Datei.
     * @param encoding
     *            Das kann zum Beispiel "ASCII" oder "UTF-(" sein.
     * 
     * @return Die Datei als String
     * @throws IOException
     *             Falls es beim lesen der Datei zu Problemen kommt.
     * 
     * @return Die Datei als String
     * @throws IOException
     */
    public static String readFile(String filename, String encoding) throws IOException
    {
        return readFile(new FileInputStream(filename), encoding);

    }

    /**
     * Daten werden Fehlertolerant eingelesen! Es wird der default charset "UTF-8" verwendet.
     * 
     * @param in
     *            InputStream der zu lesenden Datei.
     * @return Die Datei als String
     * @throws IOException
     *             Falls es beim lesen der Datei zu Problemen kommt.
     */
    public static String readFile(FileInputStream in) throws IOException
    {
        return readFile(in, DEFAULT_CHARSET);
    }

    /**
     * Daten werden Fehlertolerant eingelesen! Es wird der default charset "UTF-8" verwendet.
     * 
     * @param f
     *            FileHandle der zu lesenden Datei
     * 
     * @return Die Datei als String
     * 
     * @throws IOException
     *             Falls es beim lesen der Datei zu Problemen kommt.
     */
    public static String readFile(File f) throws IOException
    {
        return readFile(new FileInputStream(f), DEFAULT_CHARSET);
    }

    public static String readFile(FileInputStream in, String encoding) throws IOException
    {
        StringWriter writer = new StringWriter();
        return readFile(in, encoding, writer);
    }

    /**
     * Daten werden Fehlertolerant eingelesen!
     * 
     * @param filename
     *            Der InputStream der zu lesenden Datei.
     * @param encoding
     *            Das kann zum Beispiel "ASCII" oder "UTF-8" sein.
     * 
     * @return Die Datei als String
     * @throws IOException
     *             Falls es beim lesen der Datei zu Problemen kommt.
     */
    public static String readFile(FileInputStream in, String encoding, Writer writer) throws IOException
    {
        ReadableByteChannel source = in.getChannel();

        Charset charset = Charset.forName(encoding);
        CharsetDecoder decoder = charset.newDecoder();

        // Daten Fehlertolerant einlesen.
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);

        // Puffer allokieren
        ByteBuffer bb = ByteBuffer.allocateDirect(BUFSIZE);
        CharBuffer cb = CharBuffer.allocate(BUFSIZE);

        CoderResult result = CoderResult.UNDERFLOW;
        boolean eof = false;

        // Daten in einer Schleife auslesen
        while (!eof)
        {
            if (result == CoderResult.UNDERFLOW)
            {
                bb.clear();
                eof = (source.read(bb) == -1);

                // Flippen um Buffer auszulesen
                bb.flip();
            }

            // Bytes zu Character decodieren
            result = decoder.decode(bb, cb, eof);

            if (result == CoderResult.OVERFLOW)
            {
                // Der Overflow Fall behandeln ... und Daten weiter auslesen
                readCharBuffer(writer, cb);
            }
        }

        // Buffer leeren ...

        while (decoder.flush(cb) == CoderResult.OVERFLOW)
        {
            readCharBuffer(writer, cb);
        }

        readCharBuffer(writer, cb);

        source.close();
        writer.flush();

        return writer.toString();
    }

    private static void readCharBuffer(Writer writer, CharBuffer cb) throws IOException
    {
        cb.flip();
        if (cb.hasRemaining())
        {
            writer.write(cb.toString());
        }
        cb.clear();
    }

    /**
     * @param in
     * @param encoding
     * @param lineReader
     * @return
     * @throws IOException
     */
    public static void readFile(FileInputStream in, String encoding, LineReader lineReader) throws IOException
    {
        BufferedReader reader = new BufferedReader(Channels.newReader(in.getChannel(), encoding));
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            lineReader.lineRead(line);
        }

        reader.close();
    }

    /**
     * Schreibt ein byte[] in eine Datei
     * 
     * @param filename
     *            Filename
     * @param data
     *            Die Daten
     * @throws IOException
     *             Falls etwas scheif geht
     */
    public static void writeFile(String filename, byte[] data) throws IOException
    {
        FileOutputStream fos = new FileOutputStream(filename);
        FileChannel fcout = fos.getChannel();
        ByteBuffer buf = ByteBuffer.wrap(data);
        fcout.write(buf);
        fos.close();
        fcout.close();
    }

    public static byte[] readFileAsByteArray(String filename) throws IOException
    {
        File f = new File(filename);
        FileInputStream in = new FileInputStream(f);
        FileChannel source = in.getChannel();

        byte[] b = new byte[(int) source.size()];
        // Puffer allokieren
        ByteBuffer bb = ByteBuffer.wrap(b);
        source.read(bb);
        source.close();
        in.close();
        return b;
    }

    /**
     * Erzeugt im TMP-Directory ein temporäres File mit dem Prefix <code>prefix<code>
     * 
     * @param prefix
     * @return
     * @throws IOException
     */
    public final static File createTmpFile(final String prefix) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        if (prefix != null)
        {
            sb.append(prefix);
        }
        sb.append(nextVal.getAndIncrement()).append(rand.nextLong());
        return File.createTempFile(sb.toString(), ".tmp");
    }
}
