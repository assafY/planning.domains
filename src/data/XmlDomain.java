package data;

import global.Global;

import javax.xml.bind.annotation.*;
import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * Class for every planning domain that exists in the system.
 * Domain objects are created from xml metadata files.
 */
@XmlRootElement(name="metadata", namespace="http://planning.domains/")
public class XmlDomain implements Serializable{

    private File xmlFile;
    private XmlDomain.Domain domain;

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

    @XmlType(propOrder = {"title", "files_last_modified", "metadata_last_modified", "published",
            "link", "requirements", "properties", "problems"})
    public static class Domain implements Serializable {
        private String id;
        private String title;
        private Date filesModifiedDate;
        private Date metaModifiedDate;
        private Date publishedDate;
        private String link;
        private Requirements requirements;
        private Properties properties;
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

        @XmlElement(name="title")
        public void setTitle(String title) {
            this.title = title;
        }

        public Date getFiles_last_modified() {
            return filesModifiedDate;
        }

        @XmlElement(name="files_last_modified")
        public void setFiles_last_modified(Date date) {
            filesModifiedDate = date;
        }

        public Date getMetadata_last_modified() {
            return metaModifiedDate;
        }

        @XmlElement(name="metadata_last_modified")
        public void setMetadata_last_modified(Date date) {
            metaModifiedDate = date;
        }

        public Date getPublished() {
            return publishedDate;
        }

        @XmlElement(name="published")
        public void setPublished(Date date) {
            publishedDate = date;
        }

        public String getLink() {
            return link;
        }

        @XmlElement(name="link")
        public void setLink(String link) {
            this.link = link;
        }

        public Requirements getRequirements() {
            return requirements;
        }

        @XmlElement(name="requirements")
        public void setRequirements(Requirements requirements) {
            this.requirements = requirements;
        }

        public Properties getProperties() {
            return properties;
        }

        @XmlElement(name="properties")
        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        public Problems getProblems() {
            return problems;
        }

        @XmlElement(name="problems")
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
            private String timed = null; // timed_initial_literals
            private String equality = null;
            private String inequalities = null; // duration_inequalities
            private String adl = null;
            private String derived = null; //derived_predicates
            private String conditional = null; // conditional_effects
            private String action = null; // action_costs
            private String continuous = null; // continuous_effects
            private String constraints = null;
            private String disjunctive = null; // disjunctive_preconditions
            private String existential = null; // existential_preconditions
            private String goal = null; // goal_utilities
            private String negative = null; // negative_preconditions
            private String numeric = null; // numeric_fluents
            private String object = null; // object_fluents
            private String preferences = null;
            private String quantified = null; // quantified_preconditions
            private String time = null;
            private String universal = null; // universal_preconditions

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

            public String getDuration() {
                return inequalities;
            }

            @XmlElement(name = "duration_inequalities")
            public void setDuration(String inequalities) {
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

            public String getAction() {
                return action;
            }

            @XmlElement(name ="action_costs")
            public void setAction(String action) {
                this.action = action;
            }

            public String getContinuous() {
                return continuous;
            }

            @XmlElement(name ="continuous_effects")
            public void setContinuous(String continuous) {
                this.continuous = continuous;
            }

            public String getConstraints() {
                return constraints;
            }

            @XmlElement(name ="constraints")
            public void setConstraints(String constraints) {
                this.constraints = constraints;
            }

            public String getDisjunctive() {
                return disjunctive;
            }

            @XmlElement(name ="disjunctive_preconditions")
            public void setDisjunctive(String disjunctive) {
                this.disjunctive = disjunctive;
            }

            public String getExistential() {
                return existential;
            }

            @XmlElement(name ="existential_preconditions")
            public void setExistential(String existential) {
                this.existential = existential;
            }

            public String getGoal() {
                return goal;
            }

            @XmlElement(name ="goal_utilities")
            public void setGoal(String goal) {
                this.goal = goal;
            }

            public String getNegative() {
                return negative;
            }

            @XmlElement(name ="negative_preconditions")
            public void setNegative(String negative) {
                this.negative = negative;
            }

            public String getNumeric() {
                return numeric;
            }

            @XmlElement(name ="numeric_fluents")
            public void setNumeric(String conditional) {
                this.numeric = numeric;
            }

            public String getObject() {
                return object;
            }

            @XmlElement(name ="object_fluents")
            public void setObject(String object) {
                this.object = object;
            }

            public String getPreferences() {
                return preferences;
            }

            @XmlElement(name ="preferences")
            public void setPreferences(String preferences) {
                this.preferences = preferences;
            }

            public String getQuantified() {
                return quantified;
            }

            @XmlElement(name ="quantified_preconditions")
            public void setQuantified(String quantified) {
                this.quantified = quantified;
            }

            public String getTime() {
                return time;
            }

            @XmlElement(name ="time")
            public void setTime(String time) {
                this.time = time;
            }

            public String getUniversal() {
                return universal;
            }

            @XmlElement(name ="universal_preconditions")
            public void setUniversal(String universal) {
                this.universal = universal;
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
                if (action != null) {
                    toReturn += "action_costs\n";
                }
                if (continuous != null) {
                    toReturn += "continuous_effects\n";
                }
                if (constraints != null) {
                    toReturn += "constraints\n";
                }
                if (disjunctive != null) {
                    toReturn += "disjunctive_preconditions\n";
                }
                if (existential != null) {
                    toReturn += "existential_preconditions\n";
                }
                if (goal != null) {
                    toReturn += "goal_utilities\n";
                }
                if (negative != null) {
                    toReturn += "negative_preconditions\n";
                }
                if (numeric != null) {
                    toReturn += "numeric_fluents\n";
                }
                if (object != null) {
                    toReturn += "object_fluents\n";
                }
                if (preferences != null) {
                    toReturn += "preferences\n";
                }
                if (quantified != null) {
                    toReturn += "quantified_preconditions\n";
                }
                if (time != null) {
                    toReturn += "time\n";
                }
                if (universal != null) {
                    toReturn += "universal_preconditions\n";
                }
                return toReturn;
            }
        }

        public static class Properties implements Serializable {

            // Strings representing domain properties
            private String dead = null; // dead_ends
            private String solvable = null;
            private String zero = null; // zero_cost_actions
            private String required = null; // required_concurrency
            private String complexity = null;

            public String getDead() {
                return dead;
            }

            @XmlElement(name = "dead_ends")
            public void setDead(String dead) {
                this.dead = dead;
            }

            public String getSolvable() {
                return solvable;
            }

            @XmlElement(name = "solvable")
            public void setSolvable(String solvable) {
                this.solvable = solvable;
            }

            public String getRequired() {
                return required;
            }

            @XmlElement(name = "required_concurrency")
            public void setRequired(String required) {
                this.required = required;
            }

            public String getZero() {
                return zero;
            }

            @XmlElement(name = "zero_cost_actions")
            public void setZero(String zero) {
                this.zero = zero;
            }

            public String getComplexity() {
                return complexity;
            }

            @XmlElement(name = "complexity")
            public void setComplexity(String complexity) {
                this.complexity = complexity;
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
                }

                public HashMap<Planner, Integer> getResultMap() {
                    System.out.println("getting result map of size " + resultMap.size());
                    return resultMap;
                }

                /**
                 * Get the planner with the best result for this problem, as
                 * well as the result
                 *
                 * @return Result, a planner integer pair
                 */
                public Result getBestResult() {

                    Iterator iter = resultMap.entrySet().iterator();
                    Map.Entry bestResult = (Map.Entry) iter.next();
                    while (iter.hasNext()) {
                        Map.Entry currentResult = (Map.Entry) iter.next();
                        if ((Integer) currentResult.getValue() < (Integer) bestResult.getValue()) {
                            bestResult = currentResult;
                        }
                    }

                    return new Result((Planner) bestResult.getKey(), (Integer) bestResult.getValue());
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
