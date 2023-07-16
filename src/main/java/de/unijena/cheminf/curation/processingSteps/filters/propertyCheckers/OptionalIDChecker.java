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

/**
 * Processing step that checks all given atom containers whether they have the optional ID property.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class OptionalIDChecker extends BasePropertyChecker {

    /**
     * Constructor; calls the super constructor with the given reporter, the name string of the optional second
     * identifier property and the respective error code.
     *
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @param anOptionalIDPropertyName name string of the atom container property containing an optional second
     *                                 identifier (e.g. the name or CAS registry number) for each structure; if this
     *                                 field gets specified (not null), every atom container processed by this
     *                                 processing step is expected to have a property with the respective name; the info
     *                                 is then included in the report
     * @throws NullPointerException if the given reporter or property name is null
     * @throws IllegalArgumentException if the property name string is blank or empty
     */
    public OptionalIDChecker(IReporter aReporter, String anOptionalIDPropertyName)
            throws NullPointerException, IllegalArgumentException {
        super(aReporter, anOptionalIDPropertyName, ErrorCodes.UNSET_OPTIONAL_ID_PROPERTY);
    }

    /**
     * Constructor; calls the super constructor that initializes the reporter with an instance of {@link
     * MarkDownReporter} - initialized with the given report files directory path; passes the name string of the
     * optional second identifier property and the respective error code.
     *
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @param anOptionalIDPropertyName name string of the atom container property containing an optional second
     *                                 identifier (e.g. the name or CAS registry number) for each structure; if this
     *                                 field gets specified (not null), every atom container processed by this
     *                                 processing step is expected to have a property with the respective name; the info
     *                                 is then included in the report
     * @throws NullPointerException if the given directory path or the property name is null
     * @throws IllegalArgumentException if the given file path is no directory path; if the property name string is
     *                                  blank or empty
     */
    public OptionalIDChecker(String aReportFilesDirectoryPath, String anOptionalIDPropertyName) {
        super(aReportFilesDirectoryPath, anOptionalIDPropertyName, ErrorCodes.UNSET_OPTIONAL_ID_PROPERTY);
    }

}
