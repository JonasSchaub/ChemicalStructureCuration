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

package de.unijena.cheminf.curation.processingSteps.filters.hasProperty;

import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;

import java.util.Objects;

/**
 * Processing step to filter atom containers that possess the property that is defined to contain an external
 * identifier (see {@link #getExternalIDPropertyName()}). It is an extension of the {@link HasPropertyFilter} that
 * specifically checks for the existence of this specific property. To receive the atom containers of a set that miss
 * this specific property, see the counterpart of this filter, the {@link HasNoExternalIDFilter}.
 * <br>
 * This class is meant to be used to check the annotation of data sets.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 * @see HasNoExternalIDFilter
 */
public class HasExternalIDFilter extends HasPropertyFilter {

    /**
     * Constructor; calls the super constructor with the given reporter and the external ID property name string. Sets
     * the external ID property name field to the given value ({@link #setExternalIDPropertyName(String)}).
     *
     * @param anExternalIDPropertyName name string of the atom container property containing a second, external
     *                                 identifier (e.g. the name or CAS registry number) of the structures which is used
     *                                 to enrich the report and the existence of which is checked by this processing
     *                                 step; here: may not be null
     * @param aReporter                the reporter that is to be used when processing sets of structures
     * @throws NullPointerException     if the given reporter or property name is null
     * @throws IllegalArgumentException if the property name string is blank or empty
     * @see HasPropertyFilter (String, IReporter)
     */
    public HasExternalIDFilter(String anExternalIDPropertyName, IReporter aReporter)
            throws NullPointerException, IllegalArgumentException {
        super(anExternalIDPropertyName, aReporter);
        this.setExternalIDPropertyName(anExternalIDPropertyName);
    }

    /**
     * Constructor; calls the respective super passing the given directory path and the external ID property name
     * string. The super initializes the reporter with an instance of {@link MarkDownReporter}.
     *
     * @param anExternalIDPropertyName  name string of the atom container property containing a second, external
     *                                  identifier (e.g. the name or CAS registry number) of the structures which is
     *                                  used to enrich the report and the existence of which is checked by this
     *                                  processing step; here: may not be null
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException     if the given directory path or the property name is null
     * @throws IllegalArgumentException if the given file path is no directory path; if the property name string is
     *                                  blank or empty
     * @see HasPropertyFilter (String, String)
     */
    public HasExternalIDFilter(String anExternalIDPropertyName, String aReportFilesDirectoryPath) {
        super(anExternalIDPropertyName, aReportFilesDirectoryPath);
    }

    /**
     * {@inheritDoc}
     * <p>
     *     The external ID property name passed to the {@link HasExternalIDFilter} may not be null. Also sets the name of
     *     the property to check for to the given name (see {@link HasPropertyFilter#setNameOfProperty(String)}).
     * </p>
     *
     * @param anExternalIDPropertyName String instance with the name of the atom container property
     * @throws NullPointerException if the given String is null
     */
    @Override
    public void setExternalIDPropertyName(String anExternalIDPropertyName) throws NullPointerException,
            IllegalArgumentException {
        Objects.requireNonNull(anExternalIDPropertyName, "The external ID passed to the ExternalIDChecker may" +
                " not be null.");
        super.setExternalIDPropertyName(anExternalIDPropertyName);
        this.setNameOfProperty(anExternalIDPropertyName);
    }

}
