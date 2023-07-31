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

package de.unijena.cheminf.curation.processingSteps.filters;

import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import de.unijena.cheminf.curation.valenceListContainers.IValenceModel;
import de.unijena.cheminf.curation.valenceListContainers.PubChemValenceModel;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Has invalid valences filter to filter atom containers with atoms with invalid valences out of a set of atom
 * containers. The valences of the atoms are checked based on a valence model which is by default the {@link
 * PubChemValenceModel}.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 * @see IValenceModel
 */
public class HasInvalidValencesFilter extends HasAllValidValencesFilter {

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    /**
     * Constructor; initializes a {@link HasInvalidValencesFilter} with the given valence model and reporter and whether
     * to generally consider atoms with wildcard atomic number as having a valid valence.
     *
     * @param aValenceModel            the valence model to check the valences for their validity with
     * @param aConsiderWildcardAsValid boolean value whether to generally consider atoms with wildcard atomic number
     *                                 (zero) as having a valid valence
     * @param aReporter                the reporter to do the reporting with
     * @throws NullPointerException if the given valence model or reporter is null
     */
    public HasInvalidValencesFilter(IValenceModel aValenceModel, boolean aConsiderWildcardAsValid, IReporter aReporter)
            throws NullPointerException {
        super(aValenceModel, aConsiderWildcardAsValid, aReporter);
    }

    /**
     * Constructor; calls {@link #HasInvalidValencesFilter(IValenceModel, boolean, IReporter)} with an instance of
     * {@link PubChemValenceModel} as valence model.
     *
     * @param aConsiderWildcardAsValid boolean value whether to generally consider atoms with wildcard atomic number
     *                                 (zero) as having a valid valence
     * @param aReporter                the reporter to do the reporting with
     * @throws NullPointerException if the given reporter is null
     */
    public HasInvalidValencesFilter(boolean aConsiderWildcardAsValid, IReporter aReporter)
            throws NullPointerException {
        this(new PubChemValenceModel(), aConsiderWildcardAsValid, aReporter);
    }

    /**
     * Constructor; initializes a {@link HasInvalidValencesFilter} with an instance of {@link MarkDownReporter}
     * (initialized with the given directory path), the given valence model and reporter and whether to generally
     * consider atoms with wildcard atomic number as having a valid valence.
     *
     * @param aValenceModel             the valence model to check the valences for their validity with
     * @param aConsiderWildcardAsValid  boolean value whether to generally consider atoms with wildcard atomic number
     *                                  (zero) as having a valid valence
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the valence model or the directory path string is null
     * @throws IllegalArgumentException if the given file path is no directory path
     */
    public HasInvalidValencesFilter(IValenceModel aValenceModel, boolean aConsiderWildcardAsValid,
                                     String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        super(aValenceModel, aConsiderWildcardAsValid, aReportFilesDirectoryPath);
    }

    /**
     * Constructor; calls {@link #HasInvalidValencesFilter(IValenceModel, boolean, String)} with an instance of {@link
     * PubChemValenceModel} as valence model.
     *
     * @param aConsiderWildcardAsValid  boolean value whether to generally consider atoms with wildcard atomic number
     *                                  (zero) as having a valid valence
     * @param aReportFilesDirectoryPath the directory path for the MarkDownReporter to create the report files at
     * @throws NullPointerException if the directory path string is null
     * @throws IllegalArgumentException if the given file path is no directory path
     */
    public HasInvalidValencesFilter(boolean aConsiderWildcardAsValid, String aReportFilesDirectoryPath)
            throws NullPointerException, IllegalArgumentException {
        this(new PubChemValenceModel(), aConsiderWildcardAsValid, aReportFilesDirectoryPath);
    }
    //</editor-fold>

    @Override
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException, IllegalArgumentException {
        return !super.isFiltered(anAtomContainer);
    }

}
