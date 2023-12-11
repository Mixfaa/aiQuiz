package help.me.aiQuiz.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Quiz {
    @Id
    @GeneratedValue
    private long id;
    private String name;

    private String topic;

    @Enumerated(EnumType.STRING)
    private QuizSubject subject;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Question> questions;


}
