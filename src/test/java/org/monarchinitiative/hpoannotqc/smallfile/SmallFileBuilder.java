package org.monarchinitiative.hpoannotqc.smallfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is intended to make it easier to make tests for small file conversion and it follows the Builder pattern.
 * The builder builds a List of Strings that can be written to a temporary file by a test class, whereby the list
 * of Strings are equivalent to a small file that we can test. Most of the entries are defaults, and the code from
 * the test class can set those entries that it wants to test.
 * The intended use is something like this
 * TODO develop this test to make V2 small file entries for testing.
 * <pre>
 * List<String> annots=new ArrayList<>();
 * annots.add(SmallFileBuilder.getHeader());
 * SmallFileBuilder builder = new SmallFileBuilder().
 * diseaseId("OMIM:220220").
 * diseaseName("220220 DANDY-WALKER MALFORMATION WITH POSTAXIAL POLYDACTYLY");
 * annots.add(builder.build());
 * (... and so on for each line you want to add)
 * </pre>
 */
class SmallFileBuilder {


    /**
     * These are the header fields for most of our small files.
     */
    private static final String[] headerFields = {"Disease ID", "Disease Name", "Gene ID", "Gene Name",
            "Genotype", "Gene Symbol(s)", "Phenotype ID", "Phenotype Name", "Age of Onset ID",
            "Age of Onset Name", "Evidence ID", "Evidence Name", "Frequency", "Sex ID",
            "Sex Name", "Negation ID", "Negation Name", "Description",
            "Pub", "Assigned by", "Date Created"};


    private String diseaseId = "OMIM:101850";
    private String diseaseName = "ACROKERATOELASTOIDOSIS";
    private String geneID = "";
    private String geneName = "";
    private String genotype = "";
    private String genesymbol = "";
    private String phenotypeId = "HP:0000962";
    private String phenotypeName = "Hyperkeratosis";
    private String ageOfOnsetId = "";
    private String ageOfOnsetName = "";
    private String evidenceID = "IEA";
    private String evidenceName = "IEA";
    private String frequency = "";
    private String sexID = "";
    private String sexName = "";
    private String negationID = "";
    private String negationName = "";
    private String description = "";
    private String pub = "OMIM:101850";
    private String assignedBy = "HPO";
    private String dateCreated = "17.02.2009";

    /**
     * @return a tab separated String with the standard header line.
     */
    static String getHeader() {
        return Arrays.stream(headerFields).collect(Collectors.joining("\t"));
    }

    SmallFileBuilder() {

    }

    SmallFileBuilder diseaseId(String id) {
        this.diseaseId = id;
        return this;
    }

    SmallFileBuilder diseaseName(String name) {
        this.diseaseName = name;
        return this;
    }

    SmallFileBuilder description(String d) {
        this.description=d;
        return this;
    }

    SmallFileBuilder hpoId(String id) {
        this.phenotypeId=id;
        return this;
    }

    SmallFileBuilder hpoName(String name) {
        this.phenotypeName=name;
        return this;
    }

    SmallFileBuilder pub(String pb) {
        this.pub=pb;
        return this;
    }

    SmallFileBuilder evidence(String e) {
        this.evidenceID=e;
        return this;
    }

    SmallFileBuilder dateCreated(String dc) {
        this.dateCreated=dc;
        return this;
    }

    SmallFileBuilder frequency(String f) {
        this.frequency=f;
        return this;
    }

    String build() {
        List<String> entry = new ArrayList<>();
        for (String f : headerFields) {
            switch (f) {
                case "Disease ID":
                    entry.add(diseaseId);
                    break;
                case "Disease Name":
                    entry.add(diseaseName);
                    break;
                case "Gene ID":
                    entry.add(geneID);
                    break;
                case "Gene Name":
                    entry.add(geneName);
                    break;
                case "Genotype":
                    entry.add(genotype);
                    break;
                case "Gene Symbol(s)":
                    entry.add(genesymbol);
                    break;
                case "Phenotype ID":
                    entry.add(phenotypeId);
                    break;
                case "Phenotype Name":
                    entry.add(phenotypeName);
                    break;
                case "Age of Onset ID":
                    entry.add(ageOfOnsetId);
                    break;
                case "Age of Onset Name":
                    entry.add(ageOfOnsetName);
                    break;
                case "Evidence ID":
                    entry.add(evidenceID);
                    break;
                case "Evidence Name":
                    entry.add(evidenceName);
                    break;
                case "Frequency":
                    entry.add(frequency);
                    break;
                case "Sex ID":
                    entry.add(sexID);
                    break;
                case "Sex Name":
                    entry.add(sexName);
                    break;
                case "Negation ID":
                    entry.add(negationID);
                    break;
                case "Negation Name":
                    entry.add(negationName);
                    break;
                case "Description":
                    entry.add(description);
                    break;
                case "Pub":
                    entry.add(pub);
                    break;
                case "Assigned by":
                    entry.add(assignedBy);
                    break;
                case "Date Created":
                    entry.add(dateCreated);
                    break;
                default:
                    System.err.println("Did not recongize field name/should never happen: " + f);
                    System.exit(1);
            }
        }

        return String.join("\t",entry);

    }
}




