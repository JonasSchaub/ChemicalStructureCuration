/*
 * MIT License
 *
 * Copyright (c) 2023 Samuel Behr, Felix Baensch, Jonas Schaub, Christoph Steinbeck, and Achim Zielesny
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.unijena.cheminf.reporter;

import java.io.IOException;

/**
 * Interface of all reporter classes.
 */
public interface IReporter {

    /**
     * TODO
     * (method might not be necessary / be replaced with a method that just switches the file destination)
     *
     * @param aFileDestination
     * @throws IOException
     */
    public void initializeNewReport(String aFileDestination) throws IOException;

    /**
     * TODO
     * (appends a report data object to a list of report data objects)
     *
     * @param aReportDataObject the report to append
     */
    public void appendReport(ReportDataObject aReportDataObject);

    /**
     * TODO
     * (creates the report file at the chosen destination)
     */
    public void report();

    /**
     * TODO
     * (clears the list of appended reports)
     */
    public void clear();

}
