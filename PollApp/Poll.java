public class Poll {
    private int id;
    private String question;
    private String option1, option2, option3, option4;
    private boolean isActive;

    public Poll(int id, String q, String o1, String o2, String o3, String o4, boolean isActive) {
        this.id = id;
        this.question = q;
        this.option1 = o1;
        this.option2 = o2;
        this.option3 = o3;
        this.option4 = o4;
        this.isActive = isActive;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public boolean isActive() {
        return isActive;
    }

    // This is used to display the poll nicely in the JList
    @Override
    public String toString() {
        return String.format("ID: %d - %s [%s]",
                id,
                question.length() > 50 ? question.substring(0, 50) + "..." : question,
                isActive ? "ACTIVE" : "INACTIVE");
    }
}