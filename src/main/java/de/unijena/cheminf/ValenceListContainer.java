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

package de.unijena.cheminf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO
 * TODO: link / reference!
 */
public class ValenceListContainer {

    /*
    TODO: implement tests
    TODO: solution that prevents changes on valence list and pointer matrix
    TODO: doc comments
     */

    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(ValenceListContainer.class.getName());

    /**
     * String of the path of the PubChem valence list text file in this project.
     * TODO: link / reference!
     */
    private static final String VALENCE_LIST_FILE_PATH = "src/main/resources/de/unijena/cheminf/curation/PubChem_Valence_list.txt";

    /**
     * Integer value of the highest atomic number contained in the "PubChem_Valence_list.txt".
     * @see #VALENCE_LIST_FILE_PATH
     */
    private static final int HIGHEST_ATOMIC_NUMBER_IN_LIST = 112;

    /**
     * Integer value of the number of rows contained by the PubChem valence list text file; 981 lines of content + one
     * headline.
     * @see #VALENCE_LIST_FILE_PATH
     */
    private static final int NUMBER_OF_ROWS_IN_FILE = 982;

    /**
     * Integer value of the number of columns per line of the PubChem valence list text file; the columns are: atomic
     * number (column 1), charge (column 2), number of π bonds (column 3), number of σ bonds (column 4), maximum number
     * of implicit hydrogens (column 5).
     * @see #VALENCE_LIST_FILE_PATH
     */
    private static final int NUMBER_OF_COLUMNS_PER_LINE_OF_FILE = 5;

    /**
     * Instance of ValenceListContainer; it is initialized at the first call of {@link ValenceListContainer#getInstance()}.
     */
    protected static ValenceListContainer INSTANCE;

    /**
     * Array list of integer arrays that stores the data held by the PubChem valence list text file. Every element in
     * the list is the equivalent to a line of the text file (except the headline); the values contained by the array
     * are:
     * atomic number (index 0), charge (index 1), number of π bonds (index 2), number of σ bonds (index 3) and the
     * maximum number of implicit hydrogens (index 4).
     * TODO: is there a way to make sure that no changes can be done to the values contained by the list?
     *          return deep-copies of the list instead?
     */
    private final ArrayList<int[]> valenceList; //TODO: int[][] instead?

    /**
     * Integer matrix that contains a pointer for every chemical element present in the valence list
     * ({@link #valenceList}) that points at the first entry in the list that regards to the element and the total
     * number of entries that belong to the specific element.
     * The values regarding a specific element may be accessed via:
     *      valenceList[ atomic number - 1 ] [0] ->  pointer;
     *      valenceList[ atomic number - 1 ] [1] ->  number of entries.
     */
    private final int[][] valenceListPointerMatrix;

    /**
     * Private constructor. Loads the text file "PubChem_Valence_list.txt", a list of valid valences and configurations
     * of atoms with respect to atomic number (column 1), charge (column 2), number of π bonds (column 3), number of σ
     * bonds (column 4) and the maximum number of implicit hydrogens (column 5).
     * TODO: link / reference!
     */
    private ValenceListContainer() {
        this.valenceList = new ArrayList<>(ValenceListContainer.NUMBER_OF_ROWS_IN_FILE - 1);
        this.valenceListPointerMatrix = new int[ValenceListContainer.HIGHEST_ATOMIC_NUMBER_IN_LIST][2];
        //
        try {
            File tmpValenceListFile = new File(ValenceListContainer.VALENCE_LIST_FILE_PATH);
            FileReader tmpFileReader = new FileReader(tmpValenceListFile);
            BufferedReader tmpBufferedReader = new BufferedReader(tmpFileReader);
            //
            //skip first line
            tmpBufferedReader.readLine();
            //
            String tmpLine;
            String[] tmpLineElements;
            int[] tmpListEntryArray;
            int tmpCurrentElementAtomicNumber = 0;
            int tmpListEntryIndex = 0;
            while ((tmpLine = tmpBufferedReader.readLine()) != null) {
                tmpLineElements = tmpLine.split("\t", ValenceListContainer.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE);
                tmpListEntryArray = new int[5];
                for (int i = 0; i < 5; i++) {
                    tmpListEntryArray[i] = Integer.parseInt(tmpLineElements[i]);
                }
                this.valenceList.add(tmpListEntryArray);
                //
                if (tmpListEntryArray[0] != tmpCurrentElementAtomicNumber) {
                    tmpCurrentElementAtomicNumber++;
                    this.valenceListPointerMatrix[tmpCurrentElementAtomicNumber - 1] = new int[]{tmpListEntryIndex, 0};
                }
                this.valenceListPointerMatrix[tmpCurrentElementAtomicNumber - 1][1]++;
                //
                tmpListEntryIndex++;
            }
            //
            tmpFileReader.close();
            tmpBufferedReader.close();
            //
        } catch (Exception anException) {     //TODO: ?!?
            ValenceListContainer.LOGGER.log(Level.SEVERE, anException.toString(), anException);
            //throw anException;
        }
    }

    /**
     * Returns an instance of ValenceListContainer.
     *
     * @return ValenceListContainer
     */
    public static ValenceListContainer getInstance() {
        if (ValenceListContainer.INSTANCE == null) {
            //TODO: throw exception if the file could not be read?
            ValenceListContainer.INSTANCE = new ValenceListContainer();
        }
        return ValenceListContainer.INSTANCE;
    }

    /**
     * Returns an array list of integer arrays that stores the data held by the PubChem valence list, a list of valid
     * valences and configurations of atoms. The values contained by each of the five integers long arrays are:
     *      [0] -> atomic number,
     *      [1] -> charge,
     *      [2] -> number of π bonds,
     *      [3] -> number of σ bonds,
     *      [4] -> maximum number of implicit hydrogens.
     * The {@link ValenceListContainer#valenceListPointerMatrix} (see
     * {@link ValenceListContainer#getValenceListPointerMatrix()}) might be used for direct access on list entries
     * regarding a specific chemical element.
     *
     * @return array list of integer arrays
     * @see ValenceListContainer#getValenceListPointerMatrix()
     */
    public ArrayList<int[]> getValenceList() {
        return valenceList;
    }

    /**
     * TODO
     * @param aValenceListIndex
     * @param anAtomConfigurationArrayIndex
     * @return
     * @throws IllegalArgumentException
     */
    public int getValenceListEntry(int aValenceListIndex, int anAtomConfigurationArrayIndex) throws IllegalArgumentException {
        if (aValenceListIndex >= ValenceListContainer.NUMBER_OF_ROWS_IN_FILE - 1)
            throw new IllegalArgumentException("The given valence list index (aValenceListIndex) exceeds the size" +
                    " of the list.");
        if (anAtomConfigurationArrayIndex >= ValenceListContainer.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE)
            throw new IllegalArgumentException("The given array index (anAtomConfigurationArrayIndex) exceeds the" +
                    " array length of five.");
        return this.valenceList.get(aValenceListIndex)[anAtomConfigurationArrayIndex];
        //TODO: negative values?
    }

    /**
     * Returns an integer matrix that contains a pointer for every chemical element present in the valence list (see
     * {@link ValenceListContainer#getValenceList()}) that points at the first entry in the list that regards to the
     * element and the total number of entries that belong to the specific element.
     * The values regarding a specific element may be accessed via:
     *      valenceList[ atomic number - 1 ] [0] ->  pointer;
     *      valenceList[ atomic number - 1 ] [1] ->  number of entries.
     *
     * @return two-dimensional matrix of integer values
     * @see ValenceListContainer#getValenceList()
     */
    public int[][] getValenceListPointerMatrix() {
        return valenceListPointerMatrix;
    }

    /**
     * TODO
     * @param anAtomicNumber
     * @return
     * @throws IllegalArgumentException
     */
    public int getValenceListElementPointer(int anAtomicNumber) throws IllegalArgumentException {
        if (anAtomicNumber < 1 || anAtomicNumber >= ValenceListContainer.HIGHEST_ATOMIC_NUMBER_IN_LIST)
            throw new IllegalArgumentException("The given atomic number (anAtomicNumber) needs to be an integer" +
                    " value between one and 112."); //TODO: does this sound like one and 112 are included?
        return this.valenceListPointerMatrix[anAtomicNumber - 1][0];
    }

    /**
     * TODO
     * @param anAtomicNumber
     * @return
     * @throws IllegalArgumentException
     */
    public int getValenceListElementAtomConfigurationsCount(int anAtomicNumber) throws IllegalArgumentException {
        if (anAtomicNumber < 1 || anAtomicNumber >= ValenceListContainer.HIGHEST_ATOMIC_NUMBER_IN_LIST)
            throw new IllegalArgumentException("The given atomic number (anAtomicNumber) needs to be an integer" +
                    " value between one and 112."); //TODO: does this sound like one and 112 are included?
        return this.valenceListPointerMatrix[anAtomicNumber - 1][1];
    }

}
