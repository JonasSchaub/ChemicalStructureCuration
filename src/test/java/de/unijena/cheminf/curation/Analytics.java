package de.unijena.cheminf.curation;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Analytics {

    public Analytics() {
    }

    @Test
    public void compareSetOfIDsWithListByErtl() {
        //get HashSet of all IDs in the absolute SMILES list of the COCONUT
        HashSet<String> tmpHashSetOfIDs = Analytics.importCoconutAbsoluteSmilesIDs();
        if (tmpHashSetOfIDs == null) {
            System.out.println("importCoconutAbsoluteSmilesIDs() returned value was null");
            return;
        }
        //
        String tmpReadLine;
        String[] tmpSplitLine;
        //
        String[][] tmpErtlListOfIDsWithStrangeAtom = new String[2000][3];
        boolean[] tmpIDIsContained = new boolean[2000];
        int tmpCountOfNotContainedIDs = 0;
        int tmpErtlSetIDsCount = 0;
        //
        try (
                BufferedReader tmpBufferedReader = new BufferedReader(new FileReader("C:\\Users\\Behr\\Documents\\Chemical_Structure_Curation_Project\\PeterErtl_COCONUT_Feedback\\valence-errors.txt"))
                ){
            while ((tmpReadLine = tmpBufferedReader.readLine()) != null) {
                tmpSplitLine = tmpReadLine.split("\t", 3);
                if (tmpSplitLine[0].equals("molecules")) {
                    break;
                }
                tmpErtlListOfIDsWithStrangeAtom[tmpErtlSetIDsCount][0] = tmpSplitLine[1];
                tmpErtlListOfIDsWithStrangeAtom[tmpErtlSetIDsCount][1] = tmpSplitLine[2].split(" ", 4)[3];
                tmpErtlListOfIDsWithStrangeAtom[tmpErtlSetIDsCount][2] = tmpSplitLine[0];
                if (tmpHashSetOfIDs.contains(tmpSplitLine[1])) {
                    tmpIDIsContained[tmpErtlSetIDsCount] = true;
                } else {
                    tmpIDIsContained[tmpErtlSetIDsCount] = false;
                    tmpCountOfNotContainedIDs++;
                }
                tmpErtlSetIDsCount++;
            }
        } catch (IOException anIOException) {   //TODO
            System.out.println("An IOException has been thrown");
            System.out.println("returning null");
            System.out.println();
            System.out.println(anIOException.toString());
            return;
        }
        //
        HashMap<String, Integer> tmpContainedIDsListAtomTypeFrequency = new HashMap<>();
        HashMap<String, Integer> tmpNotContainedIDsListAtomTypeFrequency = new HashMap<>();
        HashMap<String, Integer> tmpAllIDsListAtomTypeFrequency = new HashMap<>();
        String tmpStrangeAtom;
        for (int i = 0; i < tmpErtlSetIDsCount; i++) {
            tmpStrangeAtom = tmpErtlListOfIDsWithStrangeAtom[i][1];
            if (tmpIDIsContained[i]) {
                if (tmpContainedIDsListAtomTypeFrequency.containsKey(tmpStrangeAtom)) {
                    tmpContainedIDsListAtomTypeFrequency.put(tmpStrangeAtom, tmpContainedIDsListAtomTypeFrequency.get(tmpStrangeAtom) + 1);
                } else {
                    tmpContainedIDsListAtomTypeFrequency.put(tmpStrangeAtom, 1);
                }
            } else {
                if (tmpNotContainedIDsListAtomTypeFrequency.containsKey(tmpStrangeAtom)) {
                    tmpNotContainedIDsListAtomTypeFrequency.put(tmpStrangeAtom, tmpNotContainedIDsListAtomTypeFrequency.get(tmpStrangeAtom) + 1);
                } else {
                    tmpNotContainedIDsListAtomTypeFrequency.put(tmpStrangeAtom, 1);
                }
            }
            if (tmpAllIDsListAtomTypeFrequency.containsKey(tmpStrangeAtom)) {
                tmpAllIDsListAtomTypeFrequency.put(tmpStrangeAtom, tmpAllIDsListAtomTypeFrequency.get(tmpStrangeAtom) + 1);
            } else {
                tmpAllIDsListAtomTypeFrequency.put(tmpStrangeAtom, 1);
            }
        }
        System.out.println("\n##################################\n");
        System.out.printf("All IDs contained in list by Peter Ertl: %d structures\n", tmpErtlSetIDsCount);
        System.out.println("\n##################################\n");
        System.out.printf("%6s\t%6s\t%6s\t%6s\n", "Atom", "All", "&&", "!&&");
        System.out.printf("\t\t(%4d)\t(%4d)\t(%4d)\n", tmpErtlSetIDsCount, (tmpErtlSetIDsCount - tmpCountOfNotContainedIDs), tmpCountOfNotContainedIDs);
        for (String tmpID :
                tmpAllIDsListAtomTypeFrequency.keySet()) {
            //System.out.printf("%6s\t%d\n", tmpID, tmpAllIDsListAtomTypeFrequency.get(tmpID));
            System.out.printf("%6s\t%6d\t%6d\t%6d\n", tmpID, tmpAllIDsListAtomTypeFrequency.get(tmpID),
                    tmpContainedIDsListAtomTypeFrequency.getOrDefault(tmpID, 0),
                    tmpNotContainedIDsListAtomTypeFrequency.getOrDefault(tmpID, 0));
        }
        System.out.println("\n\"system\" stands for \"Error in aromatic system\"");
        System.out.println();
        /*
        System.out.println("\n##################################\n");
        System.out.printf("All IDs of Ertl's list contained by both lists: %d structures\n", (tmpErtlSetIDsCount - tmpCountOfNotContainedIDs));
        System.out.println("\n##################################\n");
        for (String tmpID :
                tmpContainedIDsListAtomTypeFrequency.keySet()) {
            System.out.printf("%6s\t%d\n", tmpID, tmpContainedIDsListAtomTypeFrequency.get(tmpID));
        }
        System.out.println("\n##################################\n");
        System.out.printf("All IDs of Ertl's list not contained in my list: %d structures\n", tmpCountOfNotContainedIDs);
        System.out.println("\n##################################\n");
        for (String tmpID :
                tmpNotContainedIDsListAtomTypeFrequency.keySet()) {
            System.out.printf("%6s\t%d\n", tmpID, tmpNotContainedIDsListAtomTypeFrequency.get(tmpID));
        }
        */
        //
        //System.out.println("\n##################################\n");
        //System.out.println("Size of imported ID set: " + tmpHashSetOfIDs.size());
        //System.out.println("\n##################################\n");
        //System.out.println("tmpErtlSetIDsCount = " + tmpErtlSetIDsCount);
        //System.out.println("\n##################################\n");
        //System.out.println("tmpCountOfNotContainedIDs = " + tmpCountOfNotContainedIDs);
        //System.out.println("\n##################################\n");
        for (int i = 0; i < tmpErtlSetIDsCount; i++) {
            //print all
            //System.out.printf("%s\t%s\t%s\n", tmpErtlListOfIDsWithStrangeAtom[i][0], tmpErtlListOfIDsWithStrangeAtom[i][1], tmpErtlListOfIDsWithStrangeAtom[i][2]);
            //System.out.printf("%s\t%s\n", tmpErtlListOfIDsWithStrangeAtom[i][0], tmpErtlListOfIDsWithStrangeAtom[i][2]);
            //print contained entries
            if (tmpIDIsContained[i]) {
                //System.out.printf("%s\t%s\t%s\n", tmpErtlListOfIDsWithStrangeAtom[i][0], tmpErtlListOfIDsWithStrangeAtom[i][1], tmpErtlListOfIDsWithStrangeAtom[i][2]);
                //System.out.printf("%s\t%s\n", tmpErtlListOfIDsWithStrangeAtom[i][0], tmpErtlListOfIDsWithStrangeAtom[i][2]);
            }
            //print not contained entries
            if (!tmpIDIsContained[i]) {
                //System.out.printf("%s\t%s\t%s\n", tmpErtlListOfIDsWithStrangeAtom[i][0], tmpErtlListOfIDsWithStrangeAtom[i][1], tmpErtlListOfIDsWithStrangeAtom[i][2]);
                //System.out.printf("%s\t%s\n", tmpErtlListOfIDsWithStrangeAtom[i][0], tmpErtlListOfIDsWithStrangeAtom[i][2]);
            }
            //
            //check for frequency of occurrence of each atom type

        }
    }

    /**
     * Since this test returns false, the valence-errors-list by Peter Ertl does not contain any of the duplicates out
     * of the duplicates-list the duplicates-list might have been generated after extracting all structures of the
     * valence-errors-list
     */
    @Test
    public void searchForDuplicatesInListByErtl() {
        //get HashMap of duplicate IDs with a number given to the duplicate set
        HashMap<String, Integer> tmpIdWithDuplicateSetNumberHashMap = Analytics.importErtlDuplicatesList();
        if (tmpIdWithDuplicateSetNumberHashMap == null) {
            System.out.println("importErtlDuplicatesList() returned value was null");
            return;
        }
        int[] tmpCountOfDuplicatesInDataSet = new int[tmpIdWithDuplicateSetNumberHashMap.get("NumberOfDuplicateSets") - 1]; //do I need to initialize all values?
        //
        String tmpReadLine;
        String[] tmpSplitLine;
        boolean tmpMinOneIDContained = false;
        boolean tmpContainsDuplicates = false;
        //
        try (
                BufferedReader tmpBufferedReader = new BufferedReader(new FileReader("C:\\Users\\Behr\\Documents\\Chemical_Structure_Curation_Project\\PeterErtl_COCONUT_Feedback\\valence-errors.txt"))
        ){
            while ((tmpReadLine = tmpBufferedReader.readLine()) != null) {
                tmpSplitLine = tmpReadLine.split("\t", 3);
                if (tmpSplitLine[0].equals("molecules")) {
                    break;
                }
                if (tmpIdWithDuplicateSetNumberHashMap.containsKey(tmpSplitLine[1])) {
                    tmpMinOneIDContained = true;
                    if (++tmpCountOfDuplicatesInDataSet[tmpIdWithDuplicateSetNumberHashMap.get(tmpSplitLine[1]) - 1] > 1) {
                        tmpContainsDuplicates = true;
                    }
                }
            }
        } catch (IOException anIOException) {   //TODO
            System.out.println("An IOException has been thrown");
            System.out.println("returning null");
            System.out.println();
            System.out.println(anIOException.toString());
            return;
        }
        System.out.println("tmpMinOneIDContained = " + tmpMinOneIDContained);
        System.out.println("tmpContainsDuplicates = " + tmpContainsDuplicates);
    }

    public static HashSet<String> importCoconutAbsoluteSmilesIDs() {
        String tmpReadLine;
        String[] tmpSplitLine;
        HashSet<String> tmpHashSetOfIDs = new HashSet<>();
        try (
                BufferedReader tmpBufferedReader = new BufferedReader(new FileReader(
                        "C:\\Users\\Behr\\Documents\\Chemical_Structure_Curation_Project\\Analytics\\COCONUT_absoluteSmiles_structures_with_incorrect_valencies.txt"
                ))
                ){
            //
            for (int i = 0; i < 3; i++) {
                tmpBufferedReader.readLine();
            }
            //
            while ((tmpReadLine = tmpBufferedReader.readLine()) != null) {
                tmpSplitLine = tmpReadLine.split("\t", 2);
                tmpHashSetOfIDs.add(tmpSplitLine[0]);
            }
            //
        } catch (IOException anIOException) {
            System.out.println("An IOException has been thrown");
            System.out.println("returning null");
            System.out.println();
            System.out.println(anIOException.toString());
            return null;
        }
        return tmpHashSetOfIDs;
    }

    public static HashMap<String, Integer> importErtlDuplicatesList() {
        String tmpReadLine;
        String[] tmpSplitLine;
        int tmpLatestDuplicateCount = 2;    //a number > 1
        int tmpDuplicateSetCount = 0;
        HashMap<String, Integer> tmpIdWithDuplicateSetNumberHashMap = new HashMap<>();
        try (
                BufferedReader tmpBufferedReader = new BufferedReader(new FileReader(
                        "C:\\Users\\Behr\\Documents\\Chemical_Structure_Curation_Project\\PeterErtl_COCONUT_Feedback\\duplicates_withoutBlankSpaces.txt"
                ))
        ){
            //
            while ((tmpReadLine = tmpBufferedReader.readLine()) != null && !tmpReadLine.isBlank()) {
                tmpSplitLine = tmpReadLine.split("\t", 3);
                if (Integer.parseInt(tmpSplitLine[2]) < tmpLatestDuplicateCount) {
                    tmpLatestDuplicateCount = 1;
                    tmpDuplicateSetCount++;
                } else {
                    tmpLatestDuplicateCount++;
                }
                tmpIdWithDuplicateSetNumberHashMap.put(tmpSplitLine[1], tmpDuplicateSetCount);
            }
            //
        } catch (IOException anIOException) {
            System.out.println("An IOException has been thrown");
            System.out.println("returning null");
            System.out.println();
            System.out.println(anIOException.toString());
            return null;
        }
        tmpIdWithDuplicateSetNumberHashMap.put("NumberOfDuplicateSets", tmpDuplicateSetCount);
        return tmpIdWithDuplicateSetNumberHashMap;
    }

}
