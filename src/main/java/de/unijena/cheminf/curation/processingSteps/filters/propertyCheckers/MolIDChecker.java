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
import de.unijena.cheminf.curation.processingSteps.IProcessingStep;
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;

/**
 * Processing step that checks all given atom containers whether they have a property of name {@link
 * #MOL_ID_PROPERTY_NAME}.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class MolIDChecker extends BasePropertyChecker {

    /**
     * Constructor; calls the super constructor with the given reporter, the name string of the MolID property ({@link
     * #MOL_ID_PROPERTY_NAME}) and the respective error code.
     *
     * @param aReporter the reporter that is to be used when processing sets of structures
     * @throws NullPointerException if the given IReporter instance is null
     */
    public MolIDChecker(IReporter aReporter) {
        super(aReporter, IProcessingStep.MOL_ID_PROPERTY_NAME, ErrorCodes.UNSET_MOL_ID_PROPERTY);
    }

    /**
     * Constructor; calls the super constructor that initializes the reporter with an instance of {@link
     * MarkDownReporter} - initialized with the given report files directory path; passes the name string of the MolID
     * property ({@link #MOL_ID_PROPERTY_NAME}) and the respective error code.
     *
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the given directory path is null
     * @throws IllegalArgumentException if the given file path is no directory path
     */
    public MolIDChecker(String aReportFilesDirectoryPath) {
        super(aReportFilesDirectoryPath, IProcessingStep.MOL_ID_PROPERTY_NAME, ErrorCodes.UNSET_MOL_ID_PROPERTY);
    }

}
