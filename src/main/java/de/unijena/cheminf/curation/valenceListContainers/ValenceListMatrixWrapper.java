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

import org.openscience.cdk.interfaces.IElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * Imports a valence list out of a text file and wraps it into two matrices; one containing the data and a second for a
 * faster access on the data stored by the first matrix. It gives multiple methods for easily accessing the imported
 * data. To be imported, valence lists need to have the following format:
 * <ul>
 *     <li>one headline;</li>
 *     <Li>no empty / blank lines;</Li>
 *     <li>each line is the equivalent to one valid atom configuration and contains five columns separated by a
 *     tabulator ("\t");</li>
 *     <li>the columns are: atomic number (column 1), charge (column 2), number of π bonds (column 3), number of σ
 *     bonds (column 4) and the maximum number of implicit hydrogens (column 5);</li>
 *     <li>the list entries must be sorted according to their atomic numbers, starting with the lowest.</li>
 * </ul>
 * Exemplary for this format is the valence list published as part of the paper on PubChem standardization approaches
 * (link: <a href="https://doi.org/10.1186/s13321-018-0293-8">https://doi.org/10.1186/s13321-018-0293-8</a>).
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class ValenceListMatrixWrapper {

    /*
     TODO: allow wildcard atomic number (-> the atomic number equals the index of the pointer in the second matrix)
     TODO: Constructor taking the number of lines in file
     TODO: Constructor taking no only the list (-> runs threw the list and determines the number of lines in file before
      calling the main constructor)
     TODO: change the type of the thrown exceptions
     */

    /**
     * Integer value to be set as pointer in the {@link #valenceListPointerMatrix} if the respective element is not
     * present in the valence list.
     */
    public static final int DEFAULT_POINTER_VALUE = -1;

    /**
     * Integer value of the number of columns per line of a valence list text file; the columns are: atomic number
     * (column 1), charge (column 2), number of π bonds (column 3), number of σ bonds (column 4), maximum number of
     * implicit hydrogens (column 5).
     */
    protected static final int NUMBER_OF_COLUMNS_PER_LINE_OF_FILE = 5;

    /**
     * Integer value of the highest currently known atomic number (118).
     */
    protected static final int HIGHEST_KNOWN_ATOMIC_NUMBER = IElement.Og;   // 118

    /**
     * Integer value of the number of lines contained by the valence list text file (including the headline).
     */
    private final int numberOfLinesInFile;

    /**
     * Integer value of the lowest atomic number in the valence list.
     */
    private final int lowestAtomicNumberInList;

    /**
     * Integer value of the highest atomic number in the valence list.
     */
    private final int highestAtomicNumberInList;

    /**
     * Two-dimensional matrix of integer values that stores the data loaded from a valence list text file. Every element
     * in the upper-level array of the matrix is the equivalent to a line of the text file (except the headline); the
     * values contained by the inner arrays are:
     * <br>
     * atomic number (index 0), charge (index 1), number of π bonds (index 2), number of σ bonds (index 3), maximum
     * number of implicit hydrogens (index 4).
     */
    protected final int[][] valenceListMatrix;

    /**
     * Integer matrix that contains a pointer for every chemical element present in the valence list matrix
     * ({@link #valenceListMatrix}). The pointer equals the index of the first entry in the upper-level array of the
     * valence list matrix that regards to the specific element; for elements that are not present in the list matrix,
     * the pointer is set to the default pointer value. In addition to the pointer, the matrix stores the count
     * of how many entries in the valence list matrix regard to a specific element.
     * <br>
     * The values regarding a specific element may be accessed as follows:
     * <pre>{@code
     * // the atomic number of the element to access the valid atom configurations of
     * int tmpAtomicNumber = IElement.C;
     * //
     * int tmpPointerToFirstEntry = valenceListPointerMatrix[tmpAtomicNumber][0];
     * int tmpNumberOfEntries     = valenceListPointerMatrix[tmpAtomicNumber][1];
     * }</pre>
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
     * <li>no empty or blank lines;
     * <li>five columns per line, separated by a tabulator ("\t");
     * <li>stored data: atomic number (column 1), charge (column 2), number of π bonds (column 3), number of σ
     * bonds (column 4), maximum number of implicit hydrogens (column 5).
     * </ul>
     * The list entries must be sorted according to their atomic number, starting with the entries with the lowest
     * atomic number.
     *
     * @param aValenceListFilePath String of the file path of the valence list text file
     * @param aNumberOfLinesInFile Integer value of the number of lines in the file (including the headline)
     * @throws NullPointerException if the given file path string is null
     * @throws IllegalArgumentException if the given number of rows in file or the given value of the highest atomic
     * number in the list is less than zero
     * @throws IOException if a problem occurs reading the file, e.g. the file does not fit to the expected format
     */
    public ValenceListMatrixWrapper(String aValenceListFilePath, int aNumberOfLinesInFile)
            throws NullPointerException, IllegalArgumentException, IOException {
        Objects.requireNonNull(aValenceListFilePath, "aValenceListFilePath (instance of String) is null.");
        if (aNumberOfLinesInFile < 2) {
            throw new IllegalArgumentException("The given number of lines in file (aNumberOfLinesInFile) needs to be" +
                    " at least two.");
        }
        //
        this.numberOfLinesInFile = aNumberOfLinesInFile;
        this.valenceListMatrix = new int[this.numberOfLinesInFile - 1][ValenceListMatrixWrapper.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE];
        this.valenceListPointerMatrix = new int[ValenceListMatrixWrapper.HIGHEST_KNOWN_ATOMIC_NUMBER + 1][2];
        this.loadDataFromFile(aValenceListFilePath);
        //
        this.lowestAtomicNumberInList = this.valenceListMatrix[0][0];
        this.highestAtomicNumberInList = this.valenceListMatrix[this.valenceListMatrix.length - 1][0];
    }

    /**
     * TODO
     * @param aValenceListFilePath
     * @throws NullPointerException
     * @throws IllegalArgumentException if the file contains less than two lines
     * @throws IOException
     */
    public ValenceListMatrixWrapper(String aValenceListFilePath) throws NullPointerException, IllegalArgumentException,
            IOException {
        Objects.requireNonNull(aValenceListFilePath, "aValenceListFilePath (instance of String) is null.");
        //
        // determine the number of lines in the file
        File tmpValenceListFile = new File(aValenceListFilePath);
        FileReader tmpFileReader = new FileReader(tmpValenceListFile);
        BufferedReader tmpBufferedReader = new BufferedReader(tmpFileReader);
        int tmpLinesInFileCount = 0;
        while (tmpBufferedReader.readLine() != null) {
            tmpLinesInFileCount++;
        }
        if (tmpLinesInFileCount < 2) {
            throw new IllegalArgumentException("The number of lines in file needs to be at least two.");
        }
        tmpFileReader.close();
        tmpBufferedReader.close();
        this.numberOfLinesInFile = tmpLinesInFileCount;
        //
        this.valenceListMatrix = new int[this.numberOfLinesInFile - 1][ValenceListMatrixWrapper.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE];
        this.valenceListPointerMatrix = new int[ValenceListMatrixWrapper.HIGHEST_KNOWN_ATOMIC_NUMBER + 1][2];
        this.loadDataFromFile(aValenceListFilePath);
        //
        this.lowestAtomicNumberInList = this.valenceListMatrix[0][0];
        this.highestAtomicNumberInList = this.valenceListMatrix[this.valenceListMatrix.length - 1][0];
    }

    /**
     * Returns a specific value contained by the valence list matrix, a matrix containing valid valences and
     * configurations of atoms with regard to atomic number, charge, number of π bonds, number of σ bonds and the
     * maximum number of implicit hydrogens. The index of an entry equals its line in the imported valence list file
     * minus two. The entries in the list are sorted by their atomic number.
     * <br>
     * For the entry with the index x in the valence list, the values specifying the respective atom configuration may
     * be accessed via:
     * <ul>
     *      <li>(x, 0) -> atomic number</li>
     *      <li>(x, 1) -> charge</li>
     *      <li>(x, 2) -> number of π bonds</li>
     *      <li>(x, 3) -> number of σ bonds</li>
     *      <li>(x, 4) -> maximum number of implicit hydrogens.</li>
     * </ul>
     * For iterating over the entries of the valence list, use the method {@link #getLengthOfValenceList()} to receive
     * the total count of entries in the list. To directly access list entries regarding a specific chemical element,
     * see the methods {@link #getValenceListElementPointer(int)} and {@link #getAtomConfigurationsCountOfElement(int)}.
     *
     * @param aValenceListEntryIndex index of the valence list entry to be accessed
     * @param anAtomConfigurationArrayIndex index of the value in the atom configuration array to be returned
     * @return integer value
     * @throws IllegalArgumentException if the given valence list entry index exceeds {@link #getLengthOfValenceList()}
     * or the atom configuration array index exceeds the value four or if one of the given indices is of a negative value
     * @see #getValenceListEntry(int)
     * @see #getLengthOfValenceList()
     * @see #getValenceListElementPointer(int)
     * @see #getAtomConfigurationsCountOfElement(int)
     */
    public int getValenceListEntry(int aValenceListEntryIndex, int anAtomConfigurationArrayIndex) throws IllegalArgumentException {
        if (aValenceListEntryIndex < 0 || anAtomConfigurationArrayIndex < 0) {
            throw new IllegalArgumentException("A given index (aValenceListEntryIndex or anAtomConfigurationArrayIndex)" +
                    " is of negative value.");
        }
        if (aValenceListEntryIndex >= this.valenceListMatrix.length) {
            throw new IllegalArgumentException("The given valence list entry index (aValenceListEntryIndex) exceeds" +
                    " the length of the first dimension of the valence list matrix (" + this.valenceListMatrix.length
                    + ").");
        }
        if (anAtomConfigurationArrayIndex >= this.valenceListMatrix[0].length) {
            throw new IllegalArgumentException("The given array index (anAtomConfigurationArrayIndex) exceeds the" +
                    " length of the second dimension of the valence list matrix (" + this.valenceListMatrix[0].length
                    + ").");
        }
        return this.valenceListMatrix[aValenceListEntryIndex][anAtomConfigurationArrayIndex];
    }

    /**
     * Returns an array which is the equivalent to a line in the imported valence list file and contains info on a valid
     * configuration of an atom with regard to atomic number, charge, number of π bonds, number of σ bonds and the
     * maximum number of implicit hydrogens. The index of an entry equals its line in the file minus two. The entries
     * in the list are sorted by their atomic number.
     * <br>
     * The values contained by each of the five integer long arrays are:
     * <ul>
     *      <li>[0] -> atomic number,</li>
     *      <li>[1] -> charge,</li>
     *      <li>[2] -> number of π bonds,</li>
     *      <li>[3] -> number of σ bonds,</li>
     *      <li>[4] -> maximum number of implicit hydrogens.</li>
     * </ul>
     * To get the length of the valence list matrix (which equals the number of valid valences and configurations of
     * atoms contained by the valence list), use the method {@link #getLengthOfValenceList()}. To directly access
     * list entries regarding a specific chemical element, the methods {@link #getValenceListElementPointer(int)} and
     * {@link #getAtomConfigurationsCountOfElement(int)} might be used.
     * <br>
     * To avoid changes to the original data, the returned array is a clone of the original array.
     *
     * @param aValenceListEntryIndex index of the valence list entry to be returned
     * @return integer array of length five
     * @throws IllegalArgumentException if the given index is of a negative value or exceeds the length of the valence
     * list ({@link #getLengthOfValenceList()})
     * @see #getValenceListEntry(int, int)
     * @see #getLengthOfValenceList()
     * @see #getValenceListElementPointer(int)
     * @see #getAtomConfigurationsCountOfElement(int)
     */
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
     * Returns the index of the first entry in the valence list regarding a specific chemical element. The list entries
     * may be accessed via the methods {@link #getValenceListEntry}. To receive the count of how many entries in the
     * list regard the specific element, use the method {@link #getAtomConfigurationsCountOfElement(int)}. The entries
     * in the list are sorted by their atomic number.
     * <br>
     * The given atomic number is not checked for validity. If the given atomic number is not present in the valence
     * list, -1 is returned.
     *
     * @param anAtomicNumber atomic number of the element
     * @return index of first entry regarding the element or -1 if the atomic number is not present in the list
     * @see #getValenceListEntry(int, int)
     * @see #getValenceListEntry(int)
     * @see #getAtomConfigurationsCountOfElement(int)
     */
    public int getValenceListElementPointer(int anAtomicNumber) {
        if (anAtomicNumber < this.lowestAtomicNumberInList || anAtomicNumber > this.highestAtomicNumberInList) {
            return -1;
        }
        return this.valenceListPointerMatrix[anAtomicNumber][0];
    }

    /**
     * Returns the count of how many entries in the valence list regard to a specific chemical element. The list entries
     * may be accessed via the methods {@link #getValenceListEntry}. To get a pointer pointing at the first entry
     * regarding the specific element, use the method {@link #getValenceListElementPointer(int)}. The entries in the
     * list are sorted by their atomic number.
     * <br>
     * The given atomic number is not checked for validity. If the given atomic number is not included in the valence
     * list, zero is returned.
     *
     * @param anAtomicNumber atomic number of the element
     * @return count of entries regarding the element
     * @see #getValenceListEntry(int, int)
     * @see #getValenceListEntry(int)
     * @see #getValenceListElementPointer(int)
     */
    public int getAtomConfigurationsCountOfElement(int anAtomicNumber) {
        if (anAtomicNumber < this.lowestAtomicNumberInList || anAtomicNumber > this.highestAtomicNumberInList) {
            return 0;
        }
        return this.valenceListPointerMatrix[anAtomicNumber][1];
    }

    /**
     * Returns the number of entries in the valence list. This value may be used as upper bound when iterating over the
     * entries of the valence list using one of the {@link #getValenceListEntry} methods.
     *
     * @return integer value
     * @see #getValenceListEntry(int, int)
     * @see #getValenceListEntry(int)
     */
    public int getLengthOfValenceList() {
        return this.valenceListMatrix.length;
    }

    /**
     * Returns the lowest atomic number present in the valence list.
     *
     * @return integer value
     */
    public int getLowestAtomicNumberInList() {
        return this.lowestAtomicNumberInList;
    }

    /**
     * TODO
     * Returns the highest atomic number present in the valence list. This value may be used as upper bound when
     * iterating over the valence list element pointers ({@link #getValenceListElementPointer(int)} or the atom
     * configurations count of elements ({@link #getAtomConfigurationsCountOfElement(int)}.
     *
     * @return integer value
     * @see #getValenceListElementPointer(int)
     * @see #getAtomConfigurationsCountOfElement(int)
     */
    public int getHighestAtomicNumberInList() {
        return this.highestAtomicNumberInList;
    }

    //<editor-fold desc="private methods" defaultstate="collapsed">
    /**
     * Loads a valence list text file, a file containing a list of valid valences and configurations of atoms with
     * respect to atomic number (column 1), charge (column 2), number of π bonds (column 3), number of σ bonds (column
     * 4) and the maximum number of implicit hydrogens (column 5). The file needs to have a headline and each of the
     * following lines needs to contain the mentioned five columns in the specific order, separated by a tab ("\t").
     * The list entries must be sorted according to their atomic number, starting with the lowest.
     * <p>
     * If an atomic number is not present in the valence list, the pointer in the pointer matrix is set to the default
     * pointer value.
     *
     * @param aValenceListFilePath String of the file path of the valence list text file in the project
     * @throws NullPointerException if the given string containing the file path is null
     * @throws IllegalArgumentException if the given number of rows in file is a value less than one
     * @throws IOException if a problem occurs reading the file, e.g. the file does not fit to the expected format,
     * the list entries are not sorted according to their atomic numbers or the highest atomic number in the file
     * exceeds the length of the valence list pointer matrix
     */
    private void loadDataFromFile(String aValenceListFilePath) throws NullPointerException,
            IllegalArgumentException, IOException {
        Objects.requireNonNull(aValenceListFilePath, "aValenceListFilePath (instance of String) is null.");
        File tmpValenceListFile = new File(aValenceListFilePath);
        FileReader tmpFileReader = new FileReader(tmpValenceListFile);
        BufferedReader tmpBufferedReader = new BufferedReader(tmpFileReader);
        //
        // skip first line
        tmpBufferedReader.readLine();
        //
        String tmpLine;
        String[] tmpLineElements;
        int tmpCurrentElementAtomicNumber = -1;
        int tmpListEntryIndex = 0;
        for (int i = 0; i < this.numberOfLinesInFile - 1; i++) {
            if ((tmpLine = tmpBufferedReader.readLine()) == null) {
                throw new IOException("The valence list text file does not have the expected number of lines.");
            }
            if ((tmpLineElements = tmpLine.split("\t")).length
                    != ValenceListMatrixWrapper.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE) {
                throw new IOException("The valence list text file does not fit to the expected file format; an entry" +
                        " does not have the expected number of five columns per line.");
            }
            for (int j = 0; j < 5; j++) {
                this.valenceListMatrix[i][j] = Integer.parseInt(tmpLineElements[j]);
            }
            //
            if (this.valenceListMatrix[i][0] != tmpCurrentElementAtomicNumber) {
                if (this.valenceListMatrix[i][0] > ValenceListMatrixWrapper.HIGHEST_KNOWN_ATOMIC_NUMBER) {
                    throw new IOException("The atomic numbers contained by the valence list file exceed the highest" +
                            " known atomic number.");
                }
                if (this.valenceListMatrix[i][0] < 0) {
                    throw new IOException("The valence list contains atomic numbers with values below zero.");
                }
                if (this.valenceListMatrix[i][0] < tmpCurrentElementAtomicNumber) {
                    throw new IOException("Entries in the valence list file are not sorted according to their" +
                            " atomic number.");
                }
                tmpCurrentElementAtomicNumber++;
                while (this.valenceListMatrix[i][0] > tmpCurrentElementAtomicNumber) {
                    // sets the pointer to -1 if an atomic number is not present in the imported valence list
                    this.valenceListPointerMatrix[tmpCurrentElementAtomicNumber]
                            = new int[]{ValenceListMatrixWrapper.DEFAULT_POINTER_VALUE, 0};
                    tmpCurrentElementAtomicNumber++;
                }
                this.valenceListPointerMatrix[tmpCurrentElementAtomicNumber] = new int[]{tmpListEntryIndex, 0};
            }
            this.valenceListPointerMatrix[tmpCurrentElementAtomicNumber][1]++;
            //
            tmpListEntryIndex++;
        }
        if (tmpBufferedReader.readLine() != null) {
            throw new IOException("The valence list text file does not have the expected number of lines.");
        }
        tmpFileReader.close();
        tmpBufferedReader.close();
        //
        // set the remaining pointers of the pointer matrix to the default pointer value
        tmpCurrentElementAtomicNumber++;
        while (tmpCurrentElementAtomicNumber < this.valenceListPointerMatrix.length) {
            this.valenceListPointerMatrix[tmpCurrentElementAtomicNumber]
                    = new int[]{ValenceListMatrixWrapper.DEFAULT_POINTER_VALUE, 0};
            tmpCurrentElementAtomicNumber++;
        }
    }
    //</editor-fold>

}
