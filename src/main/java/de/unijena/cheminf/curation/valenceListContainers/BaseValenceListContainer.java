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

package de.unijena.cheminf.curation.valenceListContainers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * Abstract class BaseValenceListContainer - implements IValenceListContainer.
 */
public class BaseValenceListContainer implements IValenceListContainer {

    /**
     * Integer value of the number of columns per line of a valence list text file; the columns are: atomic number
     * (column 1), charge (column 2), number of π bonds (column 3), number of σ bonds (column 4), maximum number of
     * implicit hydrogens (column 5).
     */
    protected static final int NUMBER_OF_COLUMNS_PER_LINE_OF_FILE = 5;

    /**
     * Two-dimensional matrix of integer values that stores the data loaded from a valence list text file. Every element
     * in the upper-level array of the matrix is the equivalent to a line of the text file (except the headline); the
     * values contained by the inner arrays are:
     * <br>
     * atomic number (index 0), charge (index 1), number of π bonds (index 2), number of σ bonds (index 3) and the
     * maximum number of implicit hydrogens (index 4).
     */
    protected final int[][] valenceListMatrix;

    /**
     * Integer matrix that contains a pointer for every chemical element present in the valence list matrix
     * ({@link #valenceListMatrix}). The pointer equals the index of the first entry in the upper-level array of the
     * valence list matrix that regards to the specific element. In addition to the pointer, the matrix stores the count
     * of how many entries in the valence list matrix regard to a specific element.
     * <br>
     * The values regarding a specific element may be accessed via:
     * <p>
     * [ atomic number - 1 ] [0] ->  pointer;<br>
     * [ atomic number - 1 ] [1] ->  number of entries.
     * </p>
     */
    protected final int[][] valenceListPointerMatrix;

    /**
     * Private constructor. Loads a valence list text file, a file containing a list of valid valences and
     * configurations of atoms.
     * <br>
     * The list, in which each line stands for one valid atom configuration, is expected to have the following format
     * and contain the following information:
     * <ul>
     * <li>one headline;
     * <li>five columns per line, separated by tab ("\t");
     * <li>stored data: atomic number (column 1), charge (column 2), number of π bonds (column 3), number of σ
     * bonds (column 4), maximum number of implicit hydrogens (column 5).
     * </ul>
     * The list entries must be sorted according to their atomic number, starting with the entries with the lowest
     * atomic number.
     *
     * @param aValenceListFilePath String of the file path of the valence list text file in the project
     * @param aNumberOfRowsInFile Integer value of the number of rows in the file (including the headline)
     * @param aHighestAtomicNumberInList Integer value of the highest atomic number contained by the list
     * @throws NullPointerException if the given file path string is null
     * @throws IllegalArgumentException if the given number of rows in file or the given value of the highest atomic
     * number in the list is less than zero
     * @throws IOException if a problem occurs reading the file, e.g. the file does not fit to the expected format
     */
    public BaseValenceListContainer(String aValenceListFilePath, int aNumberOfRowsInFile, int aHighestAtomicNumberInList)
            throws NullPointerException, IllegalArgumentException, IOException {
        Objects.requireNonNull(aValenceListFilePath, "aValenceListFilePath (instance of String) is null.");
        if (aNumberOfRowsInFile < 1) {
            throw new IllegalArgumentException("The given number of rows in file (aNumberOfRowsInFile) needs to be" +
                    " at least one.");
        }
        if (aHighestAtomicNumberInList < 1) {
            throw new IllegalArgumentException("The given value of the highest atomic number in the valence list" +
                    " (aHighestAtomicNumberInList) needs to be greater than or equal to one.");
        }
        this.valenceListMatrix = new int[aNumberOfRowsInFile - 1][BaseValenceListContainer.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE];
        this.valenceListPointerMatrix = new int[aHighestAtomicNumberInList][2];
        //
        this.loadDataFromFile(aValenceListFilePath, aNumberOfRowsInFile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getValenceListEntry(int aValenceListEntryIndex, int anAtomConfigurationArrayIndex) throws IllegalArgumentException {
        if (aValenceListEntryIndex < 0 || anAtomConfigurationArrayIndex < 0) {
            throw new IllegalArgumentException("One of the given indices (aValenceListEntryIndex," +
                    " anAtomConfigurationArrayIndex) is of a negative value.");
        }
        if (aValenceListEntryIndex >= this.valenceListMatrix.length) {
            throw new IllegalArgumentException("The given valence list entry index (aValenceListEntryIndex) exceeds" +
                    " the length of the valence list matrix first dimension.");
        }
        if (anAtomConfigurationArrayIndex >= this.valenceListMatrix[0].length) {
            throw new IllegalArgumentException("The given array index (anAtomConfigurationArrayIndex) exceeds the" +
                    " atom configuration array length of five.");
        }
        return this.valenceListMatrix[aValenceListEntryIndex][anAtomConfigurationArrayIndex];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getValenceListEntry(int aValenceListEntryIndex) throws IllegalArgumentException {
        if (aValenceListEntryIndex < 0) {
            throw new IllegalArgumentException("The given valence list entry index (aValenceListEntryIndex) is of a" +
                    " negative value.");
        }
        if (aValenceListEntryIndex >= this.valenceListMatrix.length) {
            throw new IllegalArgumentException("The given valence list entry index (aValenceListEntryIndex) exceeds" +
                    " the length of the valence list matrix first dimension.");
        }
        return this.valenceListMatrix[aValenceListEntryIndex].clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLengthOfValenceList() {
        return this.valenceListMatrix.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getValenceListElementPointer(int anAtomicNumber) {
        if (anAtomicNumber < 1 || anAtomicNumber > this.valenceListPointerMatrix.length) {
            return -1;
        }
        return this.valenceListPointerMatrix[anAtomicNumber - 1][0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAtomConfigurationsCountOfElement(int anAtomicNumber) {
        if (anAtomicNumber < 1 || anAtomicNumber > this.valenceListPointerMatrix.length) {
            return 0;
        }
        return this.valenceListPointerMatrix[anAtomicNumber - 1][1];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHighestAtomicNumberInList() {
        return this.valenceListPointerMatrix.length;
    }

    /**
     * Loads a valence list text file, a file containing a list of valid valences and configurations of atoms with
     * respect to atomic number (column 1), charge (column 2), number of π bonds (column 3), number of σ bonds
     * (column 4) and the maximum number of implicit hydrogens (column 5). The file needs to have a headline and
     * each of the following lines needs to contain the mentioned five columns, each separated by a tab ("\t") and in
     * the specific order. The list entries must be sorted according to their atomic number, starting with the lowest.
     * <p>
     * If an atomic number is not present in the valence list, the pointer in the pointer matrix is set to -1.
     *
     * @param aValenceListFilePath String of the file path of the valence list text file in the project
     * @param aNumberOfRowsInFile Integer value of the number of rows in the file (including the headline)
     * @throws NullPointerException if the given string containing the file path is null
     * @throws IllegalArgumentException if the given number of rows in file is a value less than one
     * @throws IOException if a problem occurs reading the file, e.g. the file does not fit to the expected format,
     * the list entries are not sorted according to their atomic numbers or the highest atomic number in the file
     * exceeds the length of the valence list pointer matrix
     */
    private void loadDataFromFile(String aValenceListFilePath, int aNumberOfRowsInFile) throws NullPointerException,
            IllegalArgumentException, IOException {
        Objects.requireNonNull(aValenceListFilePath, "aValenceListFilePath (instance of String) is null.");
        if (aNumberOfRowsInFile < 1) {
            throw new IllegalArgumentException("The given number of rows in file (aNumberOfRowsInFile) needs to be" +
                    " at least one.");
        }
        File tmpValenceListFile = new File(aValenceListFilePath);
        FileReader tmpFileReader = new FileReader(tmpValenceListFile);
        BufferedReader tmpBufferedReader = new BufferedReader(tmpFileReader);
        //
        //skip first line
        tmpBufferedReader.readLine();
        //
        String tmpLine;
        String[] tmpLineElements;
        int tmpCurrentElementAtomicNumber = 0;
        int tmpListEntryIndex = 0;
        for (int i = 0; i < aNumberOfRowsInFile - 1; i++) {
            if ((tmpLine = tmpBufferedReader.readLine()) == null) {
                throw new IOException("The valence list text file does not have the expected number of lines.");
            }
            if ((tmpLineElements = tmpLine.split("\t")).length
                    != BaseValenceListContainer.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE) {
                throw new IOException("The valence list text file does not fit to the expected file format; an entry" +
                        " does not have the expected number of five columns per line.");
            }
            for (int j = 0; j < 5; j++) {
                this.valenceListMatrix[i][j] = Integer.parseInt(tmpLineElements[j]);
            }
            //
            if (this.valenceListMatrix[i][0] != tmpCurrentElementAtomicNumber) {
                if (this.valenceListMatrix[i][0] > this.valenceListPointerMatrix.length) {
                    throw new IOException("The atomic numbers contained by the valence list file exceed the expected" +
                            " highest atomic number.");
                }
                if (this.valenceListMatrix[i][0] < tmpCurrentElementAtomicNumber) {
                    throw new IOException("Entries in the valence list file are not sorted according to their" +
                            " atomic number.");
                }
                tmpCurrentElementAtomicNumber++;
                while (this.valenceListMatrix[i][0] > tmpCurrentElementAtomicNumber) {
                    //sets the pointer to -1 if an atomic number is not present in the imported valence list
                    this.valenceListPointerMatrix[tmpCurrentElementAtomicNumber - 1] = new int[]{-1, 0};
                    tmpCurrentElementAtomicNumber++;
                }
                this.valenceListPointerMatrix[tmpCurrentElementAtomicNumber - 1] = new int[]{tmpListEntryIndex, 0};
            }
            this.valenceListPointerMatrix[tmpCurrentElementAtomicNumber - 1][1]++;
            //
            tmpListEntryIndex++;
        }
        if (tmpBufferedReader.readLine() != null) {
            throw new IOException("The valence list text file does not have the expected number of lines.");
        }
        //
        tmpFileReader.close();
        tmpBufferedReader.close();
    }

}
