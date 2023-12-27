package help.me.quiz.model;

import help.me.authentication.model.Account;
import jakarta.persistence.GeneratedValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("quiz")
public class QuizCached extends Quiz implements Serializable {
    @Id
    @GeneratedValue
    private long id;

    public QuizCached(Quiz quiz, Account creator) {
        super(quiz.getName(), quiz.getTopic(), quiz.getSubject(), quiz.getQuestions(), quiz.getAdditionalInfo(), creator);
    }
}
