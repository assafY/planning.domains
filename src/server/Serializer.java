package server;

import data.Domain;
import data.Leaderboard;
import data.Planner;
import global.Settings;
//import org.nustaq.serialization.FSTObjectInput;
//import org.nustaq.serialization.FSTObjectOutput;

import java.io.*;
import java.util.ArrayList;


/**
 * Serializes and deserializes domain, planners and the leaderboard.
 * Crucial for storing plan results and the leaderboard.
 */
public class Serializer {

    public synchronized void serializeDomainList(ArrayList<Domain> domainList) {
        try {

            FileOutputStream fileOutput = new FileOutputStream(Settings.SERIALIZATION_DIR + Settings.DOMAINLIST_FILE);
            ObjectOutputStream objOutput = new ObjectOutputStream(fileOutput);
            objOutput.writeObject(domainList);
            objOutput.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
;        }
    }

    public ArrayList<Domain> deserializeDomainList() {
        ArrayList<Domain> domainList = null;
        try {

            FileInputStream fileInput = new FileInputStream(Settings.SERIALIZATION_DIR + Settings.DOMAINLIST_FILE);
            ObjectInputStream objInput = new ObjectInputStream(fileInput);
            domainList = (ArrayList<Domain>) objInput.readObject();
            objInput.close();
            fileInput.close();

        } catch (FileNotFoundException e) {
            System.err.println(Settings.ANSI_RED + "Saved domain list file not found. Creating domain objects for all domains in res folder." + Settings.ANSI_RESET);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return domainList;
    }

    public synchronized void serializePlannerList(ArrayList<Planner> plannerList) {
        try {

            FileOutputStream fileOutput = new FileOutputStream(Settings.SERIALIZATION_DIR + Settings.PLANNERLIST_FILE);
            ObjectOutputStream objOutput = new ObjectOutputStream(fileOutput);
            objOutput.writeObject(plannerList);
            objOutput.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Planner> deserializePlannerList() {
        ArrayList<Planner> plannerList = null;
        try {

            FileInputStream fileInput = new FileInputStream(Settings.SERIALIZATION_DIR + Settings.PLANNERLIST_FILE);
            ObjectInputStream objInput = new ObjectInputStream(fileInput);
            plannerList = (ArrayList<Planner>) objInput.readObject();
            objInput.close();
            fileInput.close();

        } catch (FileNotFoundException e) {
            System.err.println(Settings.ANSI_RED + "Saved planner list file not found. Creating planner objects for all domains in res folder." + Settings.ANSI_RESET);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return plannerList;
    }

}
