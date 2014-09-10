package genetic_sorting.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alessandro Ronca
 */
public class SimpleLogger {

    private final List<? extends OutputStream> streams;
    private final Class                        srcClass;
    private final ArrayList<PrintStream>       writers;
    private final String                       srcClassString;

    public SimpleLogger (List<? extends OutputStream> streams) {
        this(streams, null);
    }

    public SimpleLogger (List<? extends OutputStream> streams, Class srcClass) {
        this.streams = streams;
        this.srcClass = srcClass;

        if (srcClass == null) {
            srcClassString = "";
        } else {
            String[] classTokens = srcClass.toString().split("\\.");
            this.srcClassString = classTokens[classTokens.length - 1];
        }

        this.writers = new ArrayList<>();
        for (OutputStream outStream : streams) {
            writers.add(new PrintStream(outStream));
        }
    }

    public Class getSrcClass () {
        return srcClass;
    }

    public List<? extends OutputStream> getStreams () {
        return new ArrayList<>(streams);
    }

    public synchronized void log (String msg) {
        String[] tokens = msg.split("\\n");
        String logHeader = "[" + new Date() + "] " + srcClassString + ": ";
        String space = "";
        for (int i = 0; i < logHeader.length(); i++) {
            space += " ";
        }

        for (PrintStream out : writers) {
            out.println(logHeader + tokens[0]);
            for (int i = 1; i < tokens.length; i++) {
                out.println(space + tokens[i]);
            }
            out.flush();
        }
    }
}
