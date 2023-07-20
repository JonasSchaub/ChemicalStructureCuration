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

package de.unijena.cheminf.curation.processingSteps.filters.propertyCheckers;

import de.unijena.cheminf.curation.enums.ErrorCodes;
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;

import java.util.Objects;

/**
 * Processing step that checks all given atom containers whether they have the optional ID property. If atom containers
 * do not have the respective property, it is reported to the reporter and the respective atom container is removed from
 * the returned atom container set. This class is meant tp be used to check the annotation of data sets.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class OptionalIDChecker extends PropertyChecker {

    /**
     * Constructor; calls the super constructor with the given reporter, the name string of the optional second
     * identifier property and the respective error code.
     *
     * @param anOptionalIDPropertyName name string of the atom container property containing the optional second
     *                                 identifier (e.g. the name or CAS registry number) which is used to enrich the
     *                                 report and is checked by this processing step; here: may not be null
     * @param aReporter                the reporter that is to be used when processing sets of structures
     * @throws NullPointerException     if the given reporter or property name is null
     * @throws IllegalArgumentException if the property name string is blank or empty
     */
    public OptionalIDChecker(String anOptionalIDPropertyName, IReporter aReporter)
            throws NullPointerException, IllegalArgumentException {
        super(anOptionalIDPropertyName, ErrorCodes.UNSET_OPTIONAL_ID_PROPERTY, aReporter);
    }

    /**
     * Constructor; calls the super constructor that initializes the reporter with an instance of {@link
     * MarkDownReporter} - initialized with the given report files directory path; passes the name string of the
     * optional second identifier property and the respective error code.
     *
     * @param anOptionalIDPropertyName  name string of the atom container property containing the optional second
     *                                  identifier (e.g. the name or CAS registry number) which is used to enrich the
     *                                  report and is checked by this processing step; here: may not be null
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException     if the given directory path or the property name is null
     * @throws IllegalArgumentException if the given file path is no directory path; if the property name string is
     *                                  blank or empty
     */
    public OptionalIDChecker(String anOptionalIDPropertyName, String aReportFilesDirectoryPath) {
        super(anOptionalIDPropertyName, ErrorCodes.UNSET_OPTIONAL_ID_PROPERTY, aReportFilesDirectoryPath);
    }

    /**
     * {@inheritDoc}
     * <p>
     *     The optional ID property name passed to the {@link OptionalIDChecker} may not be null.
     * </p>
     * @param anOptionalIDPropertyName String instance with the name of the atom container property
     * @throws NullPointerException if the given String is null
     */
    @Override
    public void setOptionalIDPropertyName(String anOptionalIDPropertyName) throws NullPointerException,
            IllegalArgumentException {
        Objects.requireNonNull(anOptionalIDPropertyName, "The optional ID passed to the OptionalIDChecker may" +
                " not be null.");
        super.setOptionalIDPropertyName(anOptionalIDPropertyName);
    }

}
