package help.me.quiz.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Quiz {
    private String name;
    private String topic;
    private QuizSubject subject;
    private List<Question> questions;
    private String additionalInfo;

    public Quiz(String name, String topic, QuizSubject subject, List<Question> questions, String additionalInfo) {
        this.name = name;
        this.topic = topic;
        this.subject = subject;
        this.questions = questions;
        this.additionalInfo = additionalInfo;
    }
}
