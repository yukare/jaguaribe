/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package br.net.jaguaribe.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Format the log output as HTML
 *
 * Based on the work at http://www.vogella.com/articles/Logging/article.html
 *
 */
public class FormatterHTML extends Formatter {

    /**
     * This method is called for every log records.
     *
     * @param rec The log record.
     *
     * @return The formated version of log record to append in log file.
     */
    @Override
    public String format(LogRecord rec) {
        StringBuilder buf = new StringBuilder(1000);
        // Bold any levels >= WARNING
        buf.append("<tr><td>");

        if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
            buf.append("<b>");
            buf.append(rec.getLevel());
            buf.append("</b>");
        } else {
            buf.append(rec.getLevel());
        }
        buf.append("</td><td>");
        buf.append(calcDate(rec.getMillis()));
        buf.append("</td><td>");
        buf.append("Class ");
        buf.append(rec.getSourceClassName());
        buf.append("<br>Method ");
        buf.append(rec.getSourceMethodName());
        buf.append("<br>Thread ");
        buf.append(rec.getThreadID());
        buf.append("</td><td>");
        buf.append(formatMessage(rec));
        buf.append("\n</td><td>\n");
        if (rec.getThrown() != null) {
            buf.append(rec.getThrown().getLocalizedMessage());
            buf.append("<br/>\r\n");
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            rec.getThrown().printStackTrace(printWriter);
            buf.append(result.toString().replace("\r", "<br/>\r"));
        }
        return buf.toString();
    }

    /**
     * Convert a date from miliseconts to a human format.
     *
     * @param millisecs The date(from LogRecord.getMillis()).
     *
     * @return A string in human readble format(something like Feb 24,2013
     * 15:45).
     *
     * @todo Make the date format a configuration in a file.
     */
    private String calcDate(long millisecs) {
        SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(millisecs);
        return date_format.format(resultdate);
    }

    /**
     * This method is called just after the handler using this formatter is
     * created.
     *
     * @param h The handler.
     *
     * @return The content to put at the start of the file.
     */
    @Override
    public String getHead(Handler h) {
        return "<HTML>\n<HEAD>\n" + (new Date()) + "\n</HEAD>\n<BODY>\n<PRE>\n"
                + "<table border>\n  "
                + "<tr><th>Level</th><th>Time</th><th>Source</th><th>Log Message</th><th>Exception</th></tr>\n";
    }

    /**
     * This method is called just after the handler using this formatter is
     * closed.
     *
     * @param h The handler.
     *
     * @return The content to put at the end of the file.
     */
    @Override
    public String getTail(Handler h) {
        return "</table>\n  </PRE></BODY>\n</HTML>\n";
    }
}
