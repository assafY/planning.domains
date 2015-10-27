
package server;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class for every planning domain that exists in the system.
 * Domain objects are created from xml metadata files.
 */

@XmlRootElement(name="metadata")
public class XmlDomain {

    private XmlDomain.Domain domain;

    public XmlDomain.Domain getDomain() {
        return domain;
    }

    @XmlElement
    public void setDomain(XmlDomain.Domain domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return domain.toString();
    }

    public static class Domain {
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
        public static class Requirements {

            // Strings representing domain requirements
            private String strips = null;
            private String typing = null;
            private String durative = null; // durative-actions
            private String fluents = null;
            private String timed = null; // time-initial-literal
            private String equality = null;
            private String inequalities = null; // duration_inequalities

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

            @XmlElement(name = "durative-actions")
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

            @XmlElement(name = "timed-initial-literals")
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
                    toReturn += "durative-actions\n";
                }
                if (fluents != null) {
                    toReturn += "fluents\n";
                }
                if (timed != null) {
                    toReturn += "timed-initial-literals\n";
                }
                return toReturn;
            }
        }

        /**
         * This class handles the problems sequence.
         */
        public static class Problems {

            private ArrayList<Problem> problems;

            public ArrayList<Problem> getProblem() {
                return problems;
            }

            public void setProblem(ArrayList<Problem> problems) {
                this.problems = problems;
            }

            /**
             * This class handles the attributes in every problem element.
             */
            public static class Problem {

                private String domainFile;
                private int number;
                private String problemFile;

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

                @Override
                public String toString() {
                    return domainFile + ", " + number + ", " + problemFile + "\n";
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
