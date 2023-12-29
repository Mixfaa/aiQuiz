package help.me.quiz.model;

import help.me.authentication.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizCached extends Quiz implements Serializable {
    private String id;

    public QuizCached(Quiz quiz, Account creator) {
        super(quiz.getName(), quiz.getTopic(), quiz.getSubject(), quiz.getQuestions(), quiz.getAdditionalInfo(), creator);
    }
}
