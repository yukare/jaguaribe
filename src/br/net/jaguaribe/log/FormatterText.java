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
import java.util.logging.LogRecord;

/**
 * This custom formatter formats parts of a log record to a single line.
 */
public class FormatterText extends Formatter {

    /**
     * This method is called for every log records.
     *
     * @param rec The log record.
     *
     * @return A string with a formated version of log record.
     */
    @Override
    public String format(LogRecord rec) {
        StringBuilder buf = new StringBuilder(1000);

        buf.append("\r\nLevel: ");
        buf.append(rec.getLevel());
        buf.append("\r\nDate: ");
        buf.append(calcDate(rec.getMillis()));
        buf.append("\r\nMessage: ");
        buf.append(formatMessage(rec));
        buf.append("\r\n");
        //stacktrace & printing everything nicely formatted
        if (rec.getThrown() != null) {
            buf.append(rec.getThrown().getLocalizedMessage());
            buf.append("\r\n");
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            rec.getThrown().printStackTrace(printWriter);
            buf.append(result.toString());
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
}
