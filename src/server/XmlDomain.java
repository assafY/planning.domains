package server;

import global.Global;

import javax.xml.bind.annotation.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Class for every planning domain that exists in the system.
 * Domain objects are created from xml metadata files.
 */
@XmlRootElement(name="metadata", namespace="http://planning.domains/")
public class XmlDomain implements Serializable{

    private File xmlFile;
    private XmlDomain.Domain domain;
    private HashMap<Planner, Result> resultMap = new HashMap<>();

    public XmlDomain.Domain getDomain() {
        return domain;
    }

    public void setXmlFile(File file) {
        xmlFile = file;
    }

    public File getXmlFile() {
        return xmlFile;
    }

    @XmlElement
    public void setDomain(XmlDomain.Domain domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return domain.toString();
    }

    public static class Domain implements Serializable {
        private String id;
        private String title;
        private Date filesModifiedDate;
        private Date metaModifiedDate;
        private Date publishedDate;
        private String link;
        private Requirements requirements;
        private Problems problems;

        public String getId() {
            return id;
        }

        public String getShortId() {
            return id.substring(id.indexOf(':') + 1).replaceAll("/", "--");
        }

        @XmlAttribute
        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        @XmlElement
        public void setTitle(String title) {
            this.title = title;
        }

        public Date getFiles_last_modified() {
            return filesModifiedDate;
        }

        @XmlElement
        public void setFiles_last_modified(Date date) {
            filesModifiedDate = date;
        }

        public Date getMetadata_last_modified() {
            return metaModifiedDate;
        }

        @XmlElement
        public void setMetadata_last_modified(Date date) {
            metaModifiedDate = date;
        }

        public Date getPublished() {
            return publishedDate;
        }

        @XmlElement
        public void setPublished(Date date) {
            publishedDate = date;
        }

        public String getLink() {
            return link;
        }

        @XmlElement
        public void setLink(String link) {
            this.link = link;
        }

        public Requirements getRequirements() {
            return requirements;
        }

        @XmlElement
        public void setRequirements(Requirements requirements) {
            this.requirements = requirements;
        }

        public Problems getProblems() {
            return problems;
        }

        @XmlElement
        public void setProblems(Problems problems) {
            this.problems = problems;
        }

        @Override
        public String toString() {
            return "Title: " + title + "\n\n" + "Files modified: " + filesModifiedDate + "\n\n"
                    + "Metadata modified: " + metaModifiedDate + "\n\n" +
                    "Published: " + publishedDate + "\n\n" + "Link: " + link + "\n\n" +
                    requirements + "\n" + problems;
        }

        /**
         * This class handles the requirements sequence in the xml files.
         * The elements in the sequence don't contain information, so we
         * simply check which elements exist out of all possible requirements.
         */
        public static class Requirements implements Serializable {

            // Strings representing domain requirements
            private String strips = null;
            private String typing = null;
            private String durative = null; // durative_actions
            private String fluents = null;
            private String timed = null; // timed_initial_literal
            private String equality = null;
            private String inequalities = null; // duration_inequalities
            private String adl = null;
            private String derived = null; //derived_predicates
            private String conditional = null; // conditional_effects

            public String getStrips() {
                return strips;
            }

            @XmlElement(name = "strips")
            public void setStrips(String strips) {
                this.strips = strips;
            }

            public String getTyping() {
                return typing;
            }

            @XmlElement(name = "typing")
            public void setTyping(String typing) {
                this.typing = typing;
            }

            public String getDurative() {
                return durative;
            }

            @XmlElement(name = "durative_actions")
            public void setDurative(String durative) {
                this.durative = durative;
            }

            public String getFluents() {
                return fluents;
            }

            @XmlElement(name = "fluents")
            public void setFluents(String fluents) {
                this.fluents = fluents;
            }

            public String getTimed() {
                return timed;
            }

            @XmlElement(name = "timed_initial_literals")
            public void setTimed(String timed) {
                this.timed = timed;
            }

            public String getEquality() {
                return equality;
            }

            @XmlElement(name = "equality")
            public void setEquality(String equality) {
                this.equality = equality;
            }

            public String getInequalities() {
                return inequalities;
            }

            @XmlElement(name = "duration_inequalities")
            public void setInequalities(String inequalities) {
                this.inequalities = inequalities;
            }

            public String getAdl() {
                return adl;
            }

            @XmlElement(name = "adl")
            public void setAdl(String adl) {
                this.adl = adl;
            }

            public String getDerived() {
                return derived;
            }

            @XmlElement(name = "derived_predicates")
            public void setDerived(String derived) {
                this.derived = derived;
            }

            public String getConditional() {
                return conditional;
            }

            @XmlElement(name ="conditional_effects")
            public void setConditional(String conditional) {
                this.conditional = conditional;
            }



            @Override
            public String toString() {
                String toReturn = "Requirements:\n";
                if (strips != null) {
                    toReturn += "strips\n";
                }
                if (typing != null) {
                    toReturn += "typing\n";
                }
                if (durative != null) {
                    toReturn += "durative_actions\n";
                }
                if (fluents != null) {
                    toReturn += "fluents\n";
                }
                if (timed != null) {
                    toReturn += "timed_initial_literals\n";
                }
                if (equality != null) {
                    toReturn += "equality\n";
                }
                if (inequalities != null) {
                    toReturn += "duration_inequalities\n";
                }
                if (adl != null) {
                    toReturn += "adl\n";
                }
                if (derived != null) {
                    toReturn += "derived_predicates\n";
                }
                if (conditional != null) {
                    toReturn += "conditional_effects\n";
                }
                return toReturn;
            }
        }

        /**
         * This class handles the problems sequence.
         */
        public static class Problems implements Serializable{

            private ArrayList<Problem> problems;

            public ArrayList<Problem> getProblem() {
                return problems;
            }

            public void setProblem(ArrayList<Problem> problems) {
                this.problems = problems;
            }

            /**
             * This class handles the attributes in every problem element.
             * Each problem contains a map containing pairs of planners which
             * were ran on it and its result.
             */
            public static class Problem implements Serializable {

                private String domainFile;
                private int number;
                private String problemFile;

                // a map for results of running different planners on this problem
                private HashMap<Planner, Integer> resultMap = new HashMap<>();

                // a map for the factorized results of planners on all problems
                private HashMap<Planner, Double> leaderBoard;

                public String getDomain_file() {
                    return domainFile;
                }

                @XmlAttribute(name = "domain_file")
                public void setDomain_file(String domainFile) {
                    this.domainFile = domainFile;
                }

                public int getNumber() {
                    return number;
                }

                @XmlAttribute
                public void setNumber(int number) {
                    this.number = number;
                }

                public String getProblem_file() {
                    return problemFile;
                }

                @XmlAttribute(name = "problem_file")
                public void setProblem_file(String problemFile) {
                    this.problemFile = problemFile;
                }

                /**
                 * Adds the value of the best plan a planner produced for this
                 * problem to a map of all results. Then calls a method creating
                 * a new leader board and assigns it to the local leader board.
                 *
                 * @param planner the planner that produced the result
                 * @param result the result produced by running the planner on this problem
                 */
                public void addResult(Planner planner, int result) {
                    resultMap.put(planner, result);
                    leaderBoard = Global.getProblemLeaderboard(resultMap);
                }

                @Override
                public String toString() {
                    return problemFile.replace(".pddl", "");
                }
            }

            @Override
            public String toString() {
                String allProblems = "Problem files:\n";
                for (Problem p : problems) {
                    allProblems += p.getNumber() + ") Domain file: " + p.getDomain_file()
                            + ", problem file: " + p.getProblem_file() + "\n";
                }
                return allProblems;
            }
        }
    }

}
