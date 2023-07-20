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

package de.unijena.cheminf.curation;

import de.unijena.cheminf.curation.processingSteps.filters.BaseFilter;
import de.unijena.cheminf.curation.processingSteps.CurationPipeline;
import de.unijena.cheminf.curation.processingSteps.filters.IFilter;
import de.unijena.cheminf.curation.reporter.IReporter;
import de.unijena.cheminf.curation.reporter.MarkDownReporter;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Collection of utils for different test classes.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class TestUtils {

    /**
     * The path name of the directory to create the report files at.
     */
    public static final String REPORT_FILES_DIRECTORY_PATH_NAME = "Processing_Reports" + File.separator;

    /**
     * Parses a set of given SMILES strings into a set of atom containers.
     *
     * @param aSmilesStrings Strings containing SMILES codes
     * @return Set of AtomContainers resulting out of parsing the SMILES strings
     * @throws NullPointerException if one of the given strings is null
     * @throws InvalidSmilesException if one of the given SMILES strings is not parsable
     */
    public static IAtomContainerSet parseSmilesStrings(String... aSmilesStrings) throws NullPointerException, InvalidSmilesException {
        for (String tmpString : aSmilesStrings) {
            Objects.requireNonNull(tmpString, "element of aSmilesStrings (instance of String) is null.");
        }
        SmilesParser tmpSmilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
        IAtomContainer tmpAtomContainer;
        IAtomContainerSet tmpACSet = new AtomContainerSet();
        for (String tmpSmilesString :
                aSmilesStrings) {
            tmpAtomContainer = tmpSmilesParser.parseSmiles(tmpSmilesString);
            tmpACSet.addAtomContainer(tmpAtomContainer);
        }
        return tmpACSet;
    }

    /**
     * Parses a given SMILES string into an atom container.
     *
     * @param aSmilesString String containing a SMILES code
     * @return AtomContainer of the parsed SMILES string
     * @throws NullPointerException if the given string is null
     * @throws InvalidSmilesException if the given SMILES string can not be parsed
     */
    public static IAtomContainer parseSmilesString(String aSmilesString) throws NullPointerException, InvalidSmilesException {
        Objects.requireNonNull(aSmilesString, "aSmilesString (instance of String) is null.");
        SmilesParser tmpSmilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
        return tmpSmilesParser.parseSmiles(aSmilesString);
    }

    /**
     * Returns an atom container set containing a given number of empty atom containers.
     *
     * @param aNumberOfAtomContainers Integer value of the number of atom containers
     * @return AtomContainerSet containing the given number of empty atom containers
     * @throws IllegalArgumentException if the given integer number is less than zero
     */
    public static IAtomContainerSet getSetOfEmptyAtomContainers(int aNumberOfAtomContainers) throws IllegalArgumentException {
        if (aNumberOfAtomContainers < 0) {
            throw new IllegalArgumentException("aNumberOfAtomContainers (integer value) is less than zero.");
        }
        IAtomContainerSet tmpAtomContainerSet = new AtomContainerSet();
        for (int i = 0; i < aNumberOfAtomContainers; i++) {
            tmpAtomContainerSet.addAtomContainer(new AtomContainer());
        }
        return tmpAtomContainerSet;
    }

    /**
     * Returns a Filter instance whose .getsFiltered() method always returns a default value.
     *
     * @return instance of Filter
     */
    public static BaseFilter getAllTrueOrFalseFilter() {
        boolean tmpDefaultValue = false;
        return TestUtils.getAllTrueOrFalseFilter(tmpDefaultValue);
    }

    /**
     * Returns a Filter instance whose .isFiltered() method always returns the boolean value given to this method.
     *
     * @param aIsFilteredReturnValue Boolean value to be returned by the .isFiltered() method of the returned
     *                               Filter instance
     * @return instance of Filter
     */
    public static BaseFilter getAllTrueOrFalseFilter(boolean aIsFilteredReturnValue) {
        return new AllTrueOrFalseFilter(aIsFilteredReturnValue);
    }

    /**
     * A filter that always filters or not.
     */
    private static class AllTrueOrFalseFilter extends BaseFilter {
        /**
         * The boolean value to be returned by the isFiltered() method.
         */
        private final boolean isFilteredReturnValue;
        //
        /**
         * Constructor; just sets the return value of the isFiltered() method.
         *
         * @param anIsFilteredReturnValue the value that shall be returned by the isFiltered() method
         */
        public AllTrueOrFalseFilter(boolean anIsFilteredReturnValue) {
            super(TestUtils.getDefaultReporterInstance(), null);
            this.isFilteredReturnValue = anIsFilteredReturnValue;
        }
        /**
         * Returns a boolean value independent of the given atom container.
         *
         * @param anAtomContainer IAtomContainer instance to be checked
         * @return Boolean value
         * @throws NullPointerException if the given IAtomContainer instance is null
         */
        @Override
        public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException {
            Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
            return this.isFilteredReturnValue;
        }
        //
        /**
         * Does nothing.
         *
         * @param anAtomContainer the atom container the issue refers to
         * @param anException the thrown exception
         */
        @Override
        protected void reportIssue(IAtomContainer anAtomContainer, Exception anException) {
            //empty method
        }
    }

    /**
     * Tests whether the .filter() method of class FilterPipeline filters a given atom container set with a specific
     * filter as expected.
     *
     * @param aFilter IFilter instance the .filter() method of class FilterPipeline should be tested with
     * @param anAtomContainerSet IAtomContainerSet instance to do the test with
     * @param anIsFilteredBooleanArray array that contains a boolean value for every atom container of the given atom
     *                                 container set and specifies whether it is meant to be filtered by the given
     *                                 filter
     * @throws NullPointerException if one of the given parameters is null
     * @throws IllegalArgumentException if the length of the given boolean array does not equal the count of atom
     * containers in the given atom container set
     * @throws Exception if something unexpected went wrong
     */
    public static void curationPipeline_processMethodTest_testsBehaviorOfMethodWithSpecificFilter(
            IFilter aFilter, IAtomContainerSet anAtomContainerSet, boolean[] anIsFilteredBooleanArray
    ) throws NullPointerException, IllegalArgumentException, Exception {
        Objects.requireNonNull(aFilter, "aFilter (instance of Filter) is null.");
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        Objects.requireNonNull(anIsFilteredBooleanArray, "anIsFilteredBooleanArray (instance of boolean[]) is null.");
        if (anAtomContainerSet.getAtomContainerCount() != anIsFilteredBooleanArray.length) {
            throw new IllegalArgumentException("The length of anIsFilteredBooleanArray (boolean[]) does not equal the " +
                    "count of atom containers in anAtomContainerSet (instance of IAtomContainerSet).");
        }
        //
        CurationPipeline tmpCurationPipeline = new CurationPipeline(TestUtils.getDefaultReporterInstance()).addProcessingStep(aFilter);
        IAtomContainerSet tmpReturnedACSet = tmpCurationPipeline.process(anAtomContainerSet, true);
        int tmpIndexInReturnedSet = 0;
        for (int i = 0; i < anIsFilteredBooleanArray.length; i++) {
            if (anIsFilteredBooleanArray[i]) {
                continue;
            }
            Assertions.assertSame(
                    anAtomContainerSet.getAtomContainer(i).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME),
                    tmpReturnedACSet.getAtomContainer(tmpIndexInReturnedSet).getProperty(CurationPipeline.MOL_ID_PROPERTY_NAME)
            );
            tmpIndexInReturnedSet++;
        }
        Assertions.assertEquals(tmpIndexInReturnedSet, tmpReturnedACSet.getAtomContainerCount());
    }

    /**
     * Returns an instance of the default reporter. As default, {@link MarkDownReporter} is used, but this might be
     * changed to test other IReporter implementations. The returned reporter generates its report files at {@link
     * #REPORT_FILES_DIRECTORY_PATH_NAME}.
     *
     * @return instance of IReporter
     */
    public static IReporter getDefaultReporterInstance() {
        IReporter tmpReporter = new MarkDownReporter();
        //TODO: the option to set a file path is missing in this MarkDownReporter version!
        return tmpReporter;
    }

    //TODO: method that gets a "TestReporter":
    //  creates no file;
    //  throws exceptions if anything is appended;
    //  can be given ErrorCodes that are allowed (-> no exception)

    /** TODO: call this method in every test class that creates reports?
     * Clears the report files directory ({@link #REPORT_FILES_DIRECTORY_PATH_NAME}).
     *
     * @throws NullPointerException if the given File is null
     * @throws IllegalArgumentException if directory does not exist or is not a directory
     * @throws IOException if an I/O error occurs
     */
    public static void clearReportFilesDirectory() throws IOException {
        FileUtils.cleanDirectory(new File(TestUtils.REPORT_FILES_DIRECTORY_PATH_NAME));
    }

}
