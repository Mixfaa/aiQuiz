package help.me.aiQuiz.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue
    private long id;

    private String caption;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Option> options;

    public boolean haveFewAnswers() {
        return options.stream().filter(Option::isAnswer).count() > 1;
    }
}


